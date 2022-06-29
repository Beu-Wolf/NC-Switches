package spatial.tests.feature.transfers

import argon.static.Sym
import spatial.dsl._

@spatial class FFMult_It_Pipe extends SpatialTest {
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
      val dummy_input_fifo = FIFO[U8](2)
      val dummy_stage0_fifo = FIFO[U8](2)
      val dummy_stage1_fifo = FIFO[U8](2)
      val dummy_stage2_fifo = FIFO[U8](2)
      val dummy_stage3_fifo = FIFO[U8](2)
      val dummy_stage4_fifo = FIFO[U8](2)
      val dummy_stage5_fifo = FIFO[U8](2)
      val dummy_stage6_fifo = FIFO[U8](2)
      val dummy_stage7_fifo = FIFO[U8](2)
      //val coefs = LUT.fromFile[U8](4)("/home/daniel/Desktop/Taurus/mapreduce/app/test.csv")
      val irred = 0x1b.to[U8] // Irreducible polynomial of GF(2^8)

      val result_0 = SRAM[U8](2)
      val a_reg_0 = SRAM[U8](2)
      val b_reg_0 = SRAM[U8](2)

      val result_1 = SRAM[U8](2)
      val a_reg_1 = SRAM[U8](2)
      val b_reg_1 = SRAM[U8](2)

      val result_2 = SRAM[U8](2)
      val a_reg_2 = SRAM[U8](2)
      val b_reg_2 = SRAM[U8](2)

      val result_3 = SRAM[U8](2)
      val a_reg_3 = SRAM[U8](2)
      val b_reg_3 = SRAM[U8](2)

      val result_4 = SRAM[U8](2)
      val a_reg_4 = SRAM[U8](2)
      val b_reg_4 = SRAM[U8](2)

      val result_5 = SRAM[U8](2)
      val a_reg_5 = SRAM[U8](2)
      val b_reg_5 = SRAM[U8](2)

      val result_6 = SRAM[U8](2)
      val a_reg_6 = SRAM[U8](2)
      val b_reg_6 = SRAM[U8](2)

      val result_7 = SRAM[U8](2)
      val a_reg_7 = SRAM[U8](2)
      val b_reg_7 = SRAM[U8](2)

      Stream.Foreach(*) { stream_idx =>

        val input = SRAM[U8](2) // Lets assume 4 multiplications for now
        val result = Reg[U8]

        Pipe {
          val packet = inbus.value
          packet_use1.enq(packet)
          packet_use2.enq(packet)
        }

        Pipe {
          // Get the necessary values from the packet. See the AD example
          val packet = packet_use1.deq()
          Parallel {
            input(0) = packet.bits(7::0).as[U8]
            input(1) = packet.bits(15::8).as[U8]
          }
          dummy_input_fifo.enq(input(1))

        }
        
        Pipe {
          val dummy = dummy_input_fifo.deq()

          a_reg_0(0) = input(0)
          b_reg_0(0) = input(1)
          result_0(0) = 0 

          if (b_reg_0(0).bit(0) == 1) {
            result_0(1) = result_0(0) ^ a_reg_0(0)
          }

          if (a_reg_0(0).bit(7) == 1) {
            a_reg_0(1) = (a_reg_0(0) << 1) ^ irred
          } else {
            a_reg_0(1) = a_reg_0(0) << 1
          }

          b_reg_0(1) = b_reg_0(0) >> 1

          // OUTPUT a_reg_0(1) b_reg_0(1) result_0(1)
          dummy_stage0_fifo.enq(result_0(1))
        }

        Pipe {
          val dummy = dummy_stage0_fifo.deq()

          a_reg_1(0) = a_reg_0(1)
          b_reg_1(0) = b_reg_0(1)
          result_1(0) = result_0(1) 

          if (b_reg_1(0).bit(0) == 1) {
            result_1(1) = result_1(0) ^ a_reg_1(0)
          }

          if (a_reg_1(0).bit(7) == 1) {
            a_reg_1(1) = (a_reg_1(0) << 1) ^ irred
          } else {
            a_reg_1(1) = a_reg_1(0) << 1
          }

          b_reg_1(1) = b_reg_1(0) >> 1

          // OUTPUT a_reg_1(1) b_reg_1(1) result_1(1)
          dummy_stage1_fifo.enq(result_1(1))
        }

        Pipe {
          val dummy = dummy_stage1_fifo.deq()

          a_reg_2(0) = a_reg_1(1)
          b_reg_2(0) = b_reg_1(1)
          result_2(0) = result_1(1) 

          if (b_reg_2(0).bit(0) == 1) {
            result_2(1) = result_2(0) ^ a_reg_2(0)
          }

          if (a_reg_2(0).bit(7) == 1) {
            a_reg_2(1) = (a_reg_2(0) << 1) ^ irred
          } else {
            a_reg_2(1) = a_reg_2(0) << 1
          }

          b_reg_2(1) = b_reg_2(0) >> 1

          // OUTPUT a_reg_2(1) b_reg_2(1) result_2(1)
          dummy_stage2_fifo.enq(result_2(1))
        }

        Pipe {
          val dummy = dummy_stage2_fifo.deq()

          a_reg_3(0) = a_reg_2(1)
          b_reg_3(0) = b_reg_2(1)
          result_3(0) = result_2(1) 

          if (b_reg_3(0).bit(0) == 1) {
            result_3(1) = result_3(0) ^ a_reg_3(0)
          }

          if (a_reg_3(0).bit(7) == 1) {
            a_reg_3(1) = (a_reg_3(0) << 1) ^ irred
          } else {
            a_reg_3(1) = a_reg_3(0) << 1
          }

          b_reg_3(1) = b_reg_3(0) >> 1

          // OUTPUT a_reg_3(1) b_reg_3(1) result_3(1)
          dummy_stage3_fifo.enq(result_3(1))
        }

        Pipe {
          val dummy = dummy_stage3_fifo.deq()

          a_reg_4(0) = a_reg_3(1)
          b_reg_4(0) = b_reg_3(1)
          result_4(0) = result_3(1) 

          if (b_reg_4(0).bit(0) == 1) {
            result_4(1) = result_4(0) ^ a_reg_4(0)
          }

          if (a_reg_4(0).bit(7) == 1) {
            a_reg_4(1) = (a_reg_4(0) << 1) ^ irred
          } else {
            a_reg_4(1) = a_reg_4(0) << 1
          }

          b_reg_4(1) = b_reg_4(0) >> 1

          // OUTPUT a_reg_4(1) b_reg_4(1) result_4(1)
          dummy_stage4_fifo.enq(result_4(1))
        }

        Pipe {
          val dummy = dummy_stage4_fifo.deq()

          a_reg_5(0) = a_reg_4(1)
          b_reg_5(0) = b_reg_4(1)
          result_5(0) = result_4(1) 

          if (b_reg_5(0).bit(0) == 1) {
            result_5(1) = result_5(0) ^ a_reg_5(0)
          }

          if (a_reg_5(0).bit(7) == 1) {
            a_reg_5(1) = (a_reg_5(0) << 1) ^ irred
          } else {
            a_reg_5(1) = a_reg_5(0) << 1
          }

          b_reg_5(1) = b_reg_5(0) >> 1

          // OUTPUT a_reg_5(1) b_reg_5(1) result_5(1)
          dummy_stage5_fifo.enq(result_5(1))
        }

        Pipe {
          val dummy = dummy_stage5_fifo.deq()

          a_reg_6(0) = a_reg_5(1)
          b_reg_6(0) = b_reg_5(1)
          result_6(0) = result_5(1) 

          if (b_reg_6(0).bit(0) == 1) {
            result_6(1) = result_6(0) ^ a_reg_6(0)
          }

          if (a_reg_6(0).bit(7) == 1) {
            a_reg_6(1) = (a_reg_6(0) << 1) ^ irred
          } else {
            a_reg_6(1) = a_reg_6(0) << 1
          }

          b_reg_6(1) = b_reg_6(0) >> 1

          // OUTPUT a_reg_6(1) b_reg_6(1) result_6(1)
          dummy_stage6_fifo.enq(result_6(1))
        }

        Pipe {
          val dummy = dummy_stage6_fifo.deq()

          a_reg_7(0) = a_reg_6(1)
          b_reg_7(0) = b_reg_6(1)
          result_7(0) = result_6(1) 

          if (b_reg_7(0).bit(0) == 1) {
            result_7(1) = result_7(0) ^ a_reg_7(0)
          }

          if (a_reg_7(0).bit(7) == 1) {
            a_reg_7(1) = (a_reg_7(0) << 1) ^ irred
          } else {
            a_reg_7(1) = a_reg_7(0) << 1
          }

          b_reg_7(1) = b_reg_7(0) >> 1

          // OUTPUT a_reg_7(1) b_reg_7(1) result_7(1)
          dummy_stage7_fifo.enq(result_7(1))
        }

        Pipe {
          val dummy = dummy_stage7_fifo.deq()

          val packet = packet_use2.deq()
          val newPacket = AxiStream512((packet.tdata.as[U512]) | (result_7(1).as[U512] << 504) , packet.tstrb, packet.tkeep, packet.tlast, packet.tid, 1, 0)
          outbus := newPacket
        }
         
      }
    }
    assert(1 == 1) // Assert keeps spatial happy
  }
}
