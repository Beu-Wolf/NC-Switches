package spatial.tests.feature.transfers

import argon.static.Sym
import spatial.dsl._

@spatial class FFDiv_It_Pipe extends SpatialTest {
  import spatial.lang.{AxiStream512, AxiStream512Bus}
  // @struct case class AxiStream512(tdata: U512, tstrb: U64, tkeep: U64, tlast: Bit, tid: U8, tdest: U8, tuser: U64)
  
  val N = 8
  val iterations = 2*N-1
  
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
      val dummy_stage8_fifo = FIFO[U8](2)
      val dummy_stage9_fifo = FIFO[U8](2)
      val dummy_stage10_fifo = FIFO[U8](2)
      val dummy_stage11_fifo = FIFO[U8](2)
      val dummy_stage12_fifo = FIFO[U8](2)
      val dummy_stage13_fifo = FIFO[U8](2)
      val dummy_stage14_fifo = FIFO[U8](2)
      
      val irred = 0x11b.to[U9] // Irreducible polynomial of GF(2^8)

      val result_0 = SRAM[U8](2)
      val a_reg_0 = SRAM[U8](6)
      val b_reg_0 = SRAM[U9](2)
      val s_reg_0 = SRAM[U9](2)
      val delta_reg_0 = SRAM[Int](2)

      val result_1 = SRAM[U8](2)
      val a_reg_1 = SRAM[U8](6)
      val b_reg_1 = SRAM[U9](2)
      val s_reg_1 = SRAM[U9](2)
      val delta_reg_1 = SRAM[Int](2)

      val result_2 = SRAM[U8](2)
      val a_reg_2 = SRAM[U8](6)
      val b_reg_2 = SRAM[U9](2)
      val s_reg_2 = SRAM[U9](2)
      val delta_reg_2 = SRAM[Int](2)

      val result_3 = SRAM[U8](2)
      val a_reg_3 = SRAM[U8](6)
      val b_reg_3 = SRAM[U9](2)
      val s_reg_3 = SRAM[U9](2)
      val delta_reg_3 = SRAM[Int](2)

      val result_4 = SRAM[U8](2)
      val a_reg_4 = SRAM[U8](6)
      val b_reg_4 = SRAM[U9](2)
      val s_reg_4 = SRAM[U9](2)
      val delta_reg_4 = SRAM[Int](2)

      val result_5 = SRAM[U8](2)
      val a_reg_5 = SRAM[U8](6)
      val b_reg_5 = SRAM[U9](2)
      val s_reg_5 = SRAM[U9](2)
      val delta_reg_5 = SRAM[Int](2)

      val result_6 = SRAM[U8](2)
      val a_reg_6 = SRAM[U8](6)
      val b_reg_6 = SRAM[U9](2)
      val s_reg_6 = SRAM[U9](2)
      val delta_reg_6 = SRAM[Int](2)

      val result_7 = SRAM[U8](2)
      val a_reg_7 = SRAM[U8](6)
      val b_reg_7 = SRAM[U9](2)
      val s_reg_7 = SRAM[U9](2)
      val delta_reg_7 = SRAM[Int](2)

      val result_8 = SRAM[U8](2)
      val a_reg_8 = SRAM[U8](6)
      val b_reg_8 = SRAM[U9](2)
      val s_reg_8 = SRAM[U9](2)
      val delta_reg_8 = SRAM[Int](2)

      val result_9 = SRAM[U8](2)
      val a_reg_9 = SRAM[U8](6)
      val b_reg_9 = SRAM[U9](2)
      val s_reg_9 = SRAM[U9](2)
      val delta_reg_9 = SRAM[Int](2)

      val result_10 = SRAM[U8](2)
      val a_reg_10 = SRAM[U8](6)
      val b_reg_10 = SRAM[U9](2)
      val s_reg_10 = SRAM[U9](2)
      val delta_reg_10 = SRAM[Int](2)

      val result_11 = SRAM[U8](2)
      val a_reg_11 = SRAM[U8](6)
      val b_reg_11 = SRAM[U9](2)
      val s_reg_11 = SRAM[U9](2)
      val delta_reg_11 = SRAM[Int](2)

      val result_12 = SRAM[U8](2)
      val a_reg_12 = SRAM[U8](6)
      val b_reg_12 = SRAM[U9](2)
      val s_reg_12 = SRAM[U9](2)
      val delta_reg_12 = SRAM[Int](2)

      val result_13 = SRAM[U8](2)
      val a_reg_13 = SRAM[U8](6)
      val b_reg_13 = SRAM[U9](2)
      val s_reg_13 = SRAM[U9](2)
      val delta_reg_13 = SRAM[Int](2)

      val result_14 = SRAM[U8](2)
      val a_reg_14 = SRAM[U8](6)
      val b_reg_14 = SRAM[U9](2)
      val s_reg_14 = SRAM[U9](2)
      val delta_reg_14 = SRAM[Int](2)



      Stream.Foreach(*) { stream_idx =>

        val input = SRAM[U8](2) // Lets assume 1 multiplication for now
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
        
        // PASS 1

        Pipe {
          val dummy = dummy_input_fifo.deq()

          a_reg_0(0) = input(0)
          b_reg_0(0) = input(1).as[U9]
          s_reg_0(0) = irred
          delta_reg_0(0) = -1
          result_0(0) = 0 


          if(b_reg_0(0).bit(0) == 1) {
            if (delta_reg_0(0) < 0) {
              val tmp1 = b_reg_0(0)
              b_reg_0(1) = ((b_reg_0(0) ^ s_reg_0(0)) >> 1).as[U9]
              s_reg_0(1) = tmp1

              val tmp2 = a_reg_0(0)
              a_reg_0(1) = (a_reg_0(0) ^ result_0(0)).as[U8]
              result_0(1) = tmp2

              delta_reg_0(1) = (- delta_reg_0(0)) - 1

            } else {
              b_reg_0(1) = ((b_reg_0(0) ^ s_reg_0(0)) >> 1).as[U9]
              a_reg_0(1) = (a_reg_0(0) ^ result_0(0)).as[U8]
              s_reg_0(1) = s_reg_0(0)
              result_0(1) = result_0(0)
              delta_reg_0(1) = delta_reg_0(0) - 1

            }

          } else {
            b_reg_0(1) = b_reg_0(0) >> 1
          }

          val if_swap = a_reg_0(1).bit(0)

          // Rotate right
          a_reg_0(2) = ((a_reg_0(1) >> 1) | (a_reg_0(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_0(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_0(3) = ((a_reg_0(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_0(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_0(4) = ((a_reg_0(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_0(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_0(5) = ((a_reg_0(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_0(1) b_reg_0(1) result_0(1) s_reg_0(1) delta_reg_0(1)
          dummy_stage0_fifo.enq(a_reg_0(5))
        }


        // PASS 2

        Pipe {
          val dummy = dummy_stage0_fifo.deq()

          a_reg_1(0) = a_reg_0(5)
          b_reg_1(0) = b_reg_0(1)
          s_reg_1(0) = s_reg_0(1)
          delta_reg_1(0) = delta_reg_0(1)
          result_1(0) = result_0(1)


          if(b_reg_1(0).bit(0) == 1) {
            if (delta_reg_1(0) < 0) {
              val tmp1 = b_reg_1(0)
              b_reg_1(1) = ((b_reg_1(0) ^ s_reg_1(0)) >> 1).as[U9]
              s_reg_1(1) = tmp1

              val tmp2 = a_reg_1(0)
              a_reg_1(1) = (a_reg_1(0) ^ result_1(0)).as[U8]
              result_1(1) = tmp2

              delta_reg_1(1) = (- delta_reg_1(0)) - 1

            } else {
              b_reg_1(1) = ((b_reg_1(0) ^ s_reg_1(0)) >> 1).as[U9]
              a_reg_1(1) = (a_reg_1(0) ^ result_1(0)).as[U8]
              s_reg_1(1) = s_reg_1(0)
              result_1(1) = result_1(0)
              delta_reg_1(1) = delta_reg_1(0) - 1

            }

          } else {
            b_reg_1(1) = b_reg_1(0) >> 1
          }

          val if_swap = a_reg_1(1).bit(0)

          // Rotate right
          a_reg_1(2) = ((a_reg_1(1) >> 1) | (a_reg_1(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_1(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_1(3) = ((a_reg_1(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_1(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_1(4) = ((a_reg_1(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_1(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_1(5) = ((a_reg_1(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_1(1) b_reg_1(1) result_1(1) s_reg_1(1) delta_reg_1(1)
          dummy_stage1_fifo.enq(a_reg_1(5))
        }

        // PASS 3

        Pipe {
          val dummy = dummy_stage1_fifo.deq()

          a_reg_2(0) = a_reg_1(5)
          b_reg_2(0) = b_reg_1(1)
          s_reg_2(0) = s_reg_1(1)
          delta_reg_2(0) = delta_reg_1(1)
          result_2(0) = result_1(1)


          if(b_reg_2(0).bit(0) == 1) {
            if (delta_reg_2(0) < 0) {
              val tmp1 = b_reg_2(0)
              b_reg_2(1) = ((b_reg_2(0) ^ s_reg_2(0)) >> 1).as[U9]
              s_reg_2(1) = tmp1

              val tmp2 = a_reg_2(0)
              a_reg_2(1) = (a_reg_2(0) ^ result_2(0)).as[U8]
              result_2(1) = tmp2

              delta_reg_2(1) = (- delta_reg_2(0)) - 1

            } else {
              b_reg_2(1) = ((b_reg_2(0) ^ s_reg_2(0)) >> 1).as[U9]
              a_reg_2(1) = (a_reg_2(0) ^ result_2(0)).as[U8]
              s_reg_2(1) = s_reg_2(0)
              result_2(1) = result_2(0)
              delta_reg_2(1) = delta_reg_2(0) - 1

            }

          } else {
            b_reg_2(1) = b_reg_2(0) >> 1
          }

          val if_swap = a_reg_2(1).bit(0)

          // Rotate right
          a_reg_2(2) = ((a_reg_2(1) >> 1) | (a_reg_2(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_2(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_2(3) = ((a_reg_2(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_2(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_2(4) = ((a_reg_2(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_2(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_2(5) = ((a_reg_2(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_2(1) b_reg_2(1) result_2(1) s_reg_2(1) delta_reg_2(1)
          dummy_stage2_fifo.enq(a_reg_2(5))
        }

        // Pass 4

        Pipe {
          val dummy = dummy_stage2_fifo.deq()

          a_reg_3(0) = a_reg_2(5)
          b_reg_3(0) = b_reg_2(1)
          s_reg_3(0) = s_reg_2(1)
          delta_reg_3(0) = delta_reg_2(1)
          result_3(0) = result_2(1)


          if(b_reg_3(0).bit(0) == 1) {
            if (delta_reg_3(0) < 0) {
              val tmp1 = b_reg_3(0)
              b_reg_3(1) = ((b_reg_3(0) ^ s_reg_3(0)) >> 1).as[U9]
              s_reg_3(1) = tmp1

              val tmp2 = a_reg_3(0)
              a_reg_3(1) = (a_reg_3(0) ^ result_3(0)).as[U8]
              result_3(1) = tmp2

              delta_reg_3(1) = (- delta_reg_3(0)) - 1

            } else {
              b_reg_3(1) = ((b_reg_3(0) ^ s_reg_3(0)) >> 1).as[U9]
              a_reg_3(1) = (a_reg_3(0) ^ result_3(0)).as[U8]
              s_reg_3(1) = s_reg_3(0)
              result_3(1) = result_3(0)
              delta_reg_3(1) = delta_reg_3(0) - 1

            }

          } else {
            b_reg_3(1) = b_reg_3(0) >> 1
          }

          val if_swap = a_reg_3(1).bit(0)

          // Rotate right
          a_reg_3(2) = ((a_reg_3(1) >> 1) | (a_reg_3(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_3(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_3(3) = ((a_reg_3(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_3(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_3(4) = ((a_reg_3(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_3(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_3(5) = ((a_reg_3(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_3(1) b_reg_3(1) result_3(1) s_reg_3(1) delta_reg_3(1)
          dummy_stage3_fifo.enq(a_reg_3(5))
        }

        // Pass 5

        Pipe {
          val dummy = dummy_stage3_fifo.deq()

          a_reg_4(0) = a_reg_3(5)
          b_reg_4(0) = b_reg_3(1)
          s_reg_4(0) = s_reg_3(1)
          delta_reg_4(0) = delta_reg_3(1)
          result_4(0) = result_3(1)


          if(b_reg_4(0).bit(0) == 1) {
            if (delta_reg_4(0) < 0) {
              val tmp1 = b_reg_4(0)
              b_reg_4(1) = ((b_reg_4(0) ^ s_reg_4(0)) >> 1).as[U9]
              s_reg_4(1) = tmp1

              val tmp2 = a_reg_4(0)
              a_reg_4(1) = (a_reg_4(0) ^ result_4(0)).as[U8]
              result_4(1) = tmp2

              delta_reg_4(1) = (- delta_reg_4(0)) - 1

            } else {
              b_reg_4(1) = ((b_reg_4(0) ^ s_reg_4(0)) >> 1).as[U9]
              a_reg_4(1) = (a_reg_4(0) ^ result_4(0)).as[U8]
              s_reg_4(1) = s_reg_4(0)
              result_4(1) = result_4(0)
              delta_reg_4(1) = delta_reg_4(0) - 1

            }

          } else {
            b_reg_4(1) = b_reg_4(0) >> 1
          }

          val if_swap = a_reg_4(1).bit(0)

          // Rotate right
          a_reg_4(2) = ((a_reg_4(1) >> 1) | (a_reg_4(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_4(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_4(3) = ((a_reg_4(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_4(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_4(4) = ((a_reg_4(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_4(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_4(5) = ((a_reg_4(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_4(1) b_reg_4(1) result_4(1) s_reg_4(1) delta_reg_4(1)
          dummy_stage4_fifo.enq(a_reg_4(5))
        }

        // Pass 6

        Pipe {
          val dummy = dummy_stage4_fifo.deq()

          a_reg_5(0) = a_reg_4(5)
          b_reg_5(0) = b_reg_4(1)
          s_reg_5(0) = s_reg_4(1)
          delta_reg_5(0) = delta_reg_4(1)
          result_5(0) = result_4(1)


          if(b_reg_5(0).bit(0) == 1) {
            if (delta_reg_5(0) < 0) {
              val tmp1 = b_reg_5(0)
              b_reg_5(1) = ((b_reg_5(0) ^ s_reg_5(0)) >> 1).as[U9]
              s_reg_5(1) = tmp1

              val tmp2 = a_reg_5(0)
              a_reg_5(1) = (a_reg_5(0) ^ result_5(0)).as[U8]
              result_5(1) = tmp2

              delta_reg_5(1) = (- delta_reg_5(0)) - 1

            } else {
              b_reg_5(1) = ((b_reg_5(0) ^ s_reg_5(0)) >> 1).as[U9]
              a_reg_5(1) = (a_reg_5(0) ^ result_5(0)).as[U8]
              s_reg_5(1) = s_reg_5(0)
              result_5(1) = result_5(0)
              delta_reg_5(1) = delta_reg_5(0) - 1

            }

          } else {
            b_reg_5(1) = b_reg_5(0) >> 1
          }

          val if_swap = a_reg_5(1).bit(0)

          // Rotate right
          a_reg_5(2) = ((a_reg_5(1) >> 1) | (a_reg_5(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_5(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_5(3) = ((a_reg_5(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_5(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_5(4) = ((a_reg_5(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_5(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_5(5) = ((a_reg_5(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_5(1) b_reg_5(1) result_5(1) s_reg_5(1) delta_reg_5(1)
          dummy_stage5_fifo.enq(a_reg_5(5))
        }

        // Pass 7

        Pipe {
          val dummy = dummy_stage5_fifo.deq()

          a_reg_6(0) = a_reg_5(5)
          b_reg_6(0) = b_reg_5(1)
          s_reg_6(0) = s_reg_5(1)
          delta_reg_6(0) = delta_reg_5(1)
          result_6(0) = result_5(1)


          if(b_reg_6(0).bit(0) == 1) {
            if (delta_reg_6(0) < 0) {
              val tmp1 = b_reg_6(0)
              b_reg_6(1) = ((b_reg_6(0) ^ s_reg_6(0)) >> 1).as[U9]
              s_reg_6(1) = tmp1

              val tmp2 = a_reg_6(0)
              a_reg_6(1) = (a_reg_6(0) ^ result_6(0)).as[U8]
              result_6(1) = tmp2

              delta_reg_6(1) = (- delta_reg_6(0)) - 1

            } else {
              b_reg_6(1) = ((b_reg_6(0) ^ s_reg_6(0)) >> 1).as[U9]
              a_reg_6(1) = (a_reg_6(0) ^ result_6(0)).as[U8]
              s_reg_6(1) = s_reg_6(0)
              result_6(1) = result_6(0)
              delta_reg_6(1) = delta_reg_6(0) - 1

            }

          } else {
            b_reg_6(1) = b_reg_6(0) >> 1
          }

          val if_swap = a_reg_6(1).bit(0)

          // Rotate right
          a_reg_6(2) = ((a_reg_6(1) >> 1) | (a_reg_6(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_6(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_6(3) = ((a_reg_6(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_6(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_6(4) = ((a_reg_6(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_6(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_6(5) = ((a_reg_6(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_6(1) b_reg_6(1) result_6(1) s_reg_6(1) delta_reg_6(1)
          dummy_stage6_fifo.enq(a_reg_6(5))
        }

        // Pass 8

        Pipe {
          val dummy = dummy_stage6_fifo.deq()

          a_reg_7(0) = a_reg_6(5)
          b_reg_7(0) = b_reg_6(1)
          s_reg_7(0) = s_reg_6(1)
          delta_reg_7(0) = delta_reg_6(1)
          result_7(0) = result_6(1)


          if(b_reg_7(0).bit(0) == 1) {
            if (delta_reg_7(0) < 0) {
              val tmp1 = b_reg_7(0)
              b_reg_7(1) = ((b_reg_7(0) ^ s_reg_7(0)) >> 1).as[U9]
              s_reg_7(1) = tmp1

              val tmp2 = a_reg_7(0)
              a_reg_7(1) = (a_reg_7(0) ^ result_7(0)).as[U8]
              result_7(1) = tmp2

              delta_reg_7(1) = (- delta_reg_7(0)) - 1

            } else {
              b_reg_7(1) = ((b_reg_7(0) ^ s_reg_7(0)) >> 1).as[U9]
              a_reg_7(1) = (a_reg_7(0) ^ result_7(0)).as[U8]
              s_reg_7(1) = s_reg_7(0)
              result_7(1) = result_7(0)
              delta_reg_7(1) = delta_reg_7(0) - 1

            }

          } else {
            b_reg_7(1) = b_reg_7(0) >> 1
          }

          val if_swap = a_reg_7(1).bit(0)

          // Rotate right
          a_reg_7(2) = ((a_reg_7(1) >> 1) | (a_reg_7(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_7(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_7(3) = ((a_reg_7(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_7(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_7(4) = ((a_reg_7(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_7(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_7(5) = ((a_reg_7(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_7(1) b_reg_7(1) result_7(1) s_reg_7(1) delta_reg_7(1)
          dummy_stage7_fifo.enq(a_reg_7(5))
        }

        // Pass 9

        Pipe {
          val dummy = dummy_stage7_fifo.deq()

          a_reg_8(0) = a_reg_7(5)
          b_reg_8(0) = b_reg_7(1)
          s_reg_8(0) = s_reg_7(1)
          delta_reg_8(0) = delta_reg_7(1)
          result_8(0) = result_7(1)


          if(b_reg_8(0).bit(0) == 1) {
            if (delta_reg_8(0) < 0) {
              val tmp1 = b_reg_8(0)
              b_reg_8(1) = ((b_reg_8(0) ^ s_reg_8(0)) >> 1).as[U9]
              s_reg_8(1) = tmp1

              val tmp2 = a_reg_8(0)
              a_reg_8(1) = (a_reg_8(0) ^ result_8(0)).as[U8]
              result_8(1) = tmp2

              delta_reg_8(1) = (- delta_reg_8(0)) - 1

            } else {
              b_reg_8(1) = ((b_reg_8(0) ^ s_reg_8(0)) >> 1).as[U9]
              a_reg_8(1) = (a_reg_8(0) ^ result_8(0)).as[U8]
              s_reg_8(1) = s_reg_8(0)
              result_8(1) = result_8(0)
              delta_reg_8(1) = delta_reg_8(0) - 1

            }

          } else {
            b_reg_8(1) = b_reg_8(0) >> 1
          }

          val if_swap = a_reg_8(1).bit(0)

          // Rotate right
          a_reg_8(2) = ((a_reg_8(1) >> 1) | (a_reg_8(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_8(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_8(3) = ((a_reg_8(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_8(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_8(4) = ((a_reg_8(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_8(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_8(5) = ((a_reg_8(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_8(1) b_reg_8(1) result_8(1) s_reg_8(1) delta_reg_8(1)
          dummy_stage8_fifo.enq(a_reg_8(5))
        }

        // Pass 10

        Pipe {
          val dummy = dummy_stage8_fifo.deq()

          a_reg_9(0) = a_reg_8(5)
          b_reg_9(0) = b_reg_8(1)
          s_reg_9(0) = s_reg_8(1)
          delta_reg_9(0) = delta_reg_8(1)
          result_9(0) = result_8(1)


          if(b_reg_9(0).bit(0) == 1) {
            if (delta_reg_9(0) < 0) {
              val tmp1 = b_reg_9(0)
              b_reg_9(1) = ((b_reg_9(0) ^ s_reg_9(0)) >> 1).as[U9]
              s_reg_9(1) = tmp1

              val tmp2 = a_reg_9(0)
              a_reg_9(1) = (a_reg_9(0) ^ result_9(0)).as[U8]
              result_9(1) = tmp2

              delta_reg_9(1) = (- delta_reg_9(0)) - 1

            } else {
              b_reg_9(1) = ((b_reg_9(0) ^ s_reg_9(0)) >> 1).as[U9]
              a_reg_9(1) = (a_reg_9(0) ^ result_9(0)).as[U8]
              s_reg_9(1) = s_reg_9(0)
              result_9(1) = result_9(0)
              delta_reg_9(1) = delta_reg_9(0) - 1

            }

          } else {
            b_reg_9(1) = b_reg_9(0) >> 1
          }

          val if_swap = a_reg_9(1).bit(0)

          // Rotate right
          a_reg_9(2) = ((a_reg_9(1) >> 1) | (a_reg_9(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_9(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_9(3) = ((a_reg_9(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_9(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_9(4) = ((a_reg_9(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_9(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_9(5) = ((a_reg_9(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_9(1) b_reg_9(1) result_9(1) s_reg_9(1) delta_reg_9(1)
          dummy_stage9_fifo.enq(a_reg_9(5))
        }

        // Pass 11

        Pipe {
          val dummy = dummy_stage9_fifo.deq()

          a_reg_10(0) = a_reg_9(5)
          b_reg_10(0) = b_reg_9(1)
          s_reg_10(0) = s_reg_9(1)
          delta_reg_10(0) = delta_reg_9(1)
          result_10(0) = result_9(1)


          if(b_reg_10(0).bit(0) == 1) {
            if (delta_reg_10(0) < 0) {
              val tmp1 = b_reg_10(0)
              b_reg_10(1) = ((b_reg_10(0) ^ s_reg_10(0)) >> 1).as[U9]
              s_reg_10(1) = tmp1

              val tmp2 = a_reg_10(0)
              a_reg_10(1) = (a_reg_10(0) ^ result_10(0)).as[U8]
              result_10(1) = tmp2

              delta_reg_10(1) = (- delta_reg_10(0)) - 1

            } else {
              b_reg_10(1) = ((b_reg_10(0) ^ s_reg_10(0)) >> 1).as[U9]
              a_reg_10(1) = (a_reg_10(0) ^ result_10(0)).as[U8]
              s_reg_10(1) = s_reg_10(0)
              result_10(1) = result_10(0)
              delta_reg_10(1) = delta_reg_10(0) - 1

            }

          } else {
            b_reg_10(1) = b_reg_10(0) >> 1
          }

          val if_swap = a_reg_10(1).bit(0)

          // Rotate right
          a_reg_10(2) = ((a_reg_10(1) >> 1) | (a_reg_10(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_10(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_10(3) = ((a_reg_10(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_10(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_10(4) = ((a_reg_10(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_10(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_10(5) = ((a_reg_10(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_10(1) b_reg_10(1) result_10(1) s_reg_10(1) delta_reg_10(1)
          dummy_stage10_fifo.enq(a_reg_10(5))
        }

        // Pass 12

        Pipe {
          val dummy = dummy_stage10_fifo.deq()

          a_reg_11(0) = a_reg_10(5)
          b_reg_11(0) = b_reg_10(1)
          s_reg_11(0) = s_reg_10(1)
          delta_reg_11(0) = delta_reg_10(1)
          result_11(0) = result_10(1)


          if(b_reg_11(0).bit(0) == 1) {
            if (delta_reg_11(0) < 0) {
              val tmp1 = b_reg_11(0)
              b_reg_11(1) = ((b_reg_11(0) ^ s_reg_11(0)) >> 1).as[U9]
              s_reg_11(1) = tmp1

              val tmp2 = a_reg_11(0)
              a_reg_11(1) = (a_reg_11(0) ^ result_11(0)).as[U8]
              result_11(1) = tmp2

              delta_reg_11(1) = (- delta_reg_11(0)) - 1

            } else {
              b_reg_11(1) = ((b_reg_11(0) ^ s_reg_11(0)) >> 1).as[U9]
              a_reg_11(1) = (a_reg_11(0) ^ result_11(0)).as[U8]
              s_reg_11(1) = s_reg_11(0)
              result_11(1) = result_11(0)
              delta_reg_11(1) = delta_reg_11(0) - 1

            }

          } else {
            b_reg_11(1) = b_reg_11(0) >> 1
          }

          val if_swap = a_reg_11(1).bit(0)

          // Rotate right
          a_reg_11(2) = ((a_reg_11(1) >> 1) | (a_reg_11(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_11(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_11(3) = ((a_reg_11(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_11(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_11(4) = ((a_reg_11(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_11(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_11(5) = ((a_reg_11(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_11(1) b_reg_11(1) result_11(1) s_reg_11(1) delta_reg_11(1)
          dummy_stage11_fifo.enq(a_reg_11(5))
        }

        // Pass 13

        Pipe {
          val dummy = dummy_stage11_fifo.deq()

          a_reg_12(0) = a_reg_11(5)
          b_reg_12(0) = b_reg_11(1)
          s_reg_12(0) = s_reg_11(1)
          delta_reg_12(0) = delta_reg_11(1)
          result_12(0) = result_11(1)


          if(b_reg_12(0).bit(0) == 1) {
            if (delta_reg_12(0) < 0) {
              val tmp1 = b_reg_12(0)
              b_reg_12(1) = ((b_reg_12(0) ^ s_reg_12(0)) >> 1).as[U9]
              s_reg_12(1) = tmp1

              val tmp2 = a_reg_12(0)
              a_reg_12(1) = (a_reg_12(0) ^ result_12(0)).as[U8]
              result_12(1) = tmp2

              delta_reg_12(1) = (- delta_reg_12(0)) - 1

            } else {
              b_reg_12(1) = ((b_reg_12(0) ^ s_reg_12(0)) >> 1).as[U9]
              a_reg_12(1) = (a_reg_12(0) ^ result_12(0)).as[U8]
              s_reg_12(1) = s_reg_12(0)
              result_12(1) = result_12(0)
              delta_reg_12(1) = delta_reg_12(0) - 1

            }

          } else {
            b_reg_12(1) = b_reg_12(0) >> 1
          }

          val if_swap = a_reg_12(1).bit(0)

          // Rotate right
          a_reg_12(2) = ((a_reg_12(1) >> 1) | (a_reg_12(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_12(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_12(3) = ((a_reg_12(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_12(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_12(4) = ((a_reg_12(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_12(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_12(5) = ((a_reg_12(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_12(1) b_reg_12(1) result_12(1) s_reg_12(1) delta_reg_12(1)
          dummy_stage12_fifo.enq(a_reg_12(5))
        }

        // Pass 14

        Pipe {
          val dummy = dummy_stage12_fifo.deq()

          a_reg_13(0) = a_reg_12(5)
          b_reg_13(0) = b_reg_12(1)
          s_reg_13(0) = s_reg_12(1)
          delta_reg_13(0) = delta_reg_12(1)
          result_13(0) = result_12(1)


          if(b_reg_13(0).bit(0) == 1) {
            if (delta_reg_13(0) < 0) {
              val tmp1 = b_reg_13(0)
              b_reg_13(1) = ((b_reg_13(0) ^ s_reg_13(0)) >> 1).as[U9]
              s_reg_13(1) = tmp1

              val tmp2 = a_reg_13(0)
              a_reg_13(1) = (a_reg_13(0) ^ result_13(0)).as[U8]
              result_13(1) = tmp2

              delta_reg_13(1) = (- delta_reg_13(0)) - 1

            } else {
              b_reg_13(1) = ((b_reg_13(0) ^ s_reg_13(0)) >> 1).as[U9]
              a_reg_13(1) = (a_reg_13(0) ^ result_13(0)).as[U8]
              s_reg_13(1) = s_reg_13(0)
              result_13(1) = result_13(0)
              delta_reg_13(1) = delta_reg_13(0) - 1

            }

          } else {
            b_reg_13(1) = b_reg_13(0) >> 1
          }

          val if_swap = a_reg_13(1).bit(0)

          // Rotate right
          a_reg_13(2) = ((a_reg_13(1) >> 1) | (a_reg_13(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_13(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_13(3) = ((a_reg_13(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_13(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_13(4) = ((a_reg_13(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_13(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_13(5) = ((a_reg_13(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_13(1) b_reg_13(1) result_13(1) s_reg_13(1) delta_reg_13(1)
          dummy_stage13_fifo.enq(a_reg_13(5))
        }

        // Pass 15

        Pipe {
          val dummy = dummy_stage13_fifo.deq()

          a_reg_14(0) = a_reg_13(5)
          b_reg_14(0) = b_reg_13(1)
          s_reg_14(0) = s_reg_13(1)
          delta_reg_14(0) = delta_reg_13(1)
          result_14(0) = result_13(1)


          if(b_reg_14(0).bit(0) == 1) {
            if (delta_reg_14(0) < 0) {
              val tmp1 = b_reg_14(0)
              b_reg_14(1) = ((b_reg_14(0) ^ s_reg_14(0)) >> 1).as[U9]
              s_reg_14(1) = tmp1

              val tmp2 = a_reg_14(0)
              a_reg_14(1) = (a_reg_14(0) ^ result_14(0)).as[U8]
              result_14(1) = tmp2

              delta_reg_14(1) = (- delta_reg_14(0)) - 1

            } else {
              b_reg_14(1) = ((b_reg_14(0) ^ s_reg_14(0)) >> 1).as[U9]
              a_reg_14(1) = (a_reg_14(0) ^ result_14(0)).as[U8]
              s_reg_14(1) = s_reg_14(0)
              result_14(1) = result_14(0)
              delta_reg_14(1) = delta_reg_14(0) - 1

            }

          } else {
            b_reg_14(1) = b_reg_14(0) >> 1
          }

          val if_swap = a_reg_14(1).bit(0)

          // Rotate right
          a_reg_14(2) = ((a_reg_14(1) >> 1) | (a_reg_14(1) << N - 1)).as[U8]

          // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3

          if(if_swap == 1) {
            val bit0 = a_reg_14(2).bit(0).as[U8] ^ 1
            val mask0 = 1.to[U8]
            a_reg_14(3) = ((a_reg_14(2) & ~mask0) | (bit0)).as[U8]

            val bit2 = a_reg_14(3).bit(2).as[U8] ^ 1
            val mask2 = (1 << 2).to[U8]
            a_reg_14(4) = ((a_reg_14(3) & ~mask2.as[U8]) | (bit2 << 2).as[U8]).as[U8]
            
            val bit3 = a_reg_14(4).bit(3).as[U8] ^ 1
            val mask3 = (1 << 3).to[U8]
            a_reg_14(5) = ((a_reg_14(4) & ~mask3.as[U8]) | (bit3 << 3).as[U8]).as[U8]

          }

          // OUTPUT a_reg_14(1) b_reg_14(1) result_14(1) s_reg_14(1) delta_reg_14(1)
          dummy_stage14_fifo.enq(result_14(1))
        }

        Pipe {
          val dummy = dummy_stage14_fifo.deq()

          val packet = packet_use2.deq()
          val newPacket = AxiStream512((packet.tdata.as[U512]) | (result_14(1).as[U512] << 504) , packet.tstrb, packet.tkeep, packet.tlast, packet.tid, 1, 0)
          outbus := newPacket
        }
         
      }
    }
    assert(1 == 1) // Assert keeps spatial happy
  }
}
