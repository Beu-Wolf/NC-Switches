package spatial.tests.feature.transfers

import argon.static.Sym
import spatial.dsl._

@spatial class Loopback512FFMult extends SpatialTest {
  import spatial.lang.{AxiStream512, AxiStream512Bus}
  // @struct case class AxiStream512(tdata: U512, tstrb: U64, tkeep: U64, tlast: Bit, tid: U8, tdest: U8, tuser: U64)
  def main(args: Array[String]): Unit = {
    // In/out buses here have type AxiStream512, meaning you can access all the axis fields in the Spatial source code (tdata, tstrb, tkeep, tlast, tid, tdest, tuser)
    //  If you only care about the tdata field, you should use type U512 instead of AxiStream512
    val inbus = StreamIn[AxiStream512](AxiStream512Bus(tid = 0, tdest = 0))
    val outbus = StreamOut[AxiStream512](AxiStream512Bus(tid = 0, tdest = 1))

    Accel {
      val packet_use1 = FIFO[AxiStream512](2)
      val packet_use2 = FIFO[AxiStream512](10)
      val coefs = LUT.fromFile[U8](4)("/home/daniel/Desktop/Taurus/mapreduce/app/test.csv")
      val irred = 29.to[U8] // Irreducible polynomial of GF(2^8)

      def FFMult(a: U8, b: U8): U8 = {
        val result = SRAM[U8](9)
        val a_reg = SRAM[U8](9)
        val b_reg = SRAM[U8](9)

        a_reg(0) = a
        b_reg(0) = b
        // Needs to be sequential, as the result from the previous operation might alter the value of a (line 35)
        Sequential.Foreach(8 by 1) { i => 
          if (b_reg(i).bit(0) == 1) {
            result(i+1) = result(i) ^ a_reg(i)
          }

          if (a_reg(i).bit(7) == 1) {
            a_reg(i+1) = (a_reg(i) << 1) ^ irred
          } else {
            a_reg(i+1) = a_reg(i) << 1
          }

          b_reg(i+1) = b_reg(i) >> 1
        }
        result(8)
      }

      Stream.Foreach(*) { stream_idx =>

        val input = SRAM[U8](4) // Lets assume 4 multiplications for now
        val result = Reg[U8]

        Pipe {
          val packet = inbus.value
          packet_use1.enq(packet)
          packet_use2.enq(packet)
        }

        Pipe {
          // Get the necessary values from the packet by shifting. See the AD example
          val packet = packet_use1.deq()
          Parallel {
            input(0) = packet.bits(7::0).as[U8]
            input(1) = packet.bits(15::8).as[U8]
            input(2) = packet.bits(23::16).as[U8]
            input(3) = packet.bits(31::24).as[U8]
          }
        }
        
        Pipe {

          result := Reduce(Reg[U8](0))(4 by 1 par 4){i => 
            FFMult(coefs(i), input(i))
          }{_^_}
        }

        Pipe {
          val packet = packet_use2.deq()
          val newPacket = AxiStream512((packet.tdata.as[U512]) | (result.as[U512] << 504) , packet.tstrb, packet.tkeep, packet.tlast, packet.tid, 1, 0)
          outbus := newPacket
        }
         
      }
    }
    assert(1 == 1) // Assert keeps spatial happy
  }
}
