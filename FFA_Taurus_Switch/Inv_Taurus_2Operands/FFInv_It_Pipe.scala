package spatial.tests.feature.transfers

import argon.static.Sym
import spatial.dsl._

@spatial class FFInv_It_Pipe extends SpatialTest {
  import spatial.lang.{AxiStream512, AxiStream512Bus}
  // @struct case class AxiStream512(tdata: U512, tstrb: U64, tkeep: U64, tlast: Bit, tid: U8, tdest: U8, tuser: U64)
  
  val N = 8
  val iterations = 2*N
  
  def main(args: Array[String]): Unit = {
    // In/out buses here have type AxiStream512, meaning you can access all the axis fields in the Spatial source code (tdata, tstrb, tkeep, tlast, tid, tdest, tuser)
    //  If you only care about the tdata field, you should use type U512 instead of AxiStream512
    val inbus = StreamIn[AxiStream512](AxiStream512Bus(tid = 0, tdest = 0))
    val outbus = StreamOut[AxiStream512](AxiStream512Bus(tid = 0, tdest = 1))

    Accel {
      val packet_use1 = FIFO[AxiStream512](2)
      val packet_use2 = FIFO[AxiStream512](10)
      val dummy_input_fifo = FIFO[U8](2)
      val dummy_stage0_fifo = FIFO[U9](2)
      val dummy_stage1_fifo = FIFO[U9](2)
      val dummy_stage2_fifo = FIFO[U9](2)
      val dummy_stage3_fifo = FIFO[U9](2)
      val dummy_stage4_fifo = FIFO[U9](2)
      val dummy_stage5_fifo = FIFO[U9](2)
      val dummy_stage6_fifo = FIFO[U9](2)
      val dummy_stage7_fifo = FIFO[U9](2)
      val dummy_stage8_fifo = FIFO[U9](2)
      val dummy_stage9_fifo = FIFO[U9](2)
      val dummy_stage10_fifo = FIFO[U9](2)
      val dummy_stage11_fifo = FIFO[U9](2)
      val dummy_stage12_fifo = FIFO[U9](2)
      val dummy_stage13_fifo = FIFO[U9](2)
      val dummy_stage14_fifo = FIFO[U9](2)
      val dummy_stage15_fifo = FIFO[U9](2)
      
      val irred = 0x11b.to[U9] // Irreducible polynomial of GF(2^8)

      val result_0 = SRAM[U9](2)
      val a_reg_0 = SRAM[U9](2)
      val s_reg_0 = SRAM[U9](4)
      val v_reg_0 = SRAM[U9](3)
      val delta_reg_0 = SRAM[Int](2)

      val result_1 = SRAM[U9](2)
      val a_reg_1 = SRAM[U9](2)
      val s_reg_1 = SRAM[U9](4)
      val v_reg_1 = SRAM[U9](3)
      val delta_reg_1 = SRAM[Int](2)

      val result_2 = SRAM[U9](2)
      val a_reg_2 = SRAM[U9](2)
      val s_reg_2 = SRAM[U9](4)
      val v_reg_2 = SRAM[U9](3)
      val delta_reg_2 = SRAM[Int](2)

      val result_3 = SRAM[U9](2)
      val a_reg_3 = SRAM[U9](2)
      val s_reg_3 = SRAM[U9](4)
      val v_reg_3 = SRAM[U9](3)
      val delta_reg_3 = SRAM[Int](2)

      val result_4 = SRAM[U9](2)
      val a_reg_4 = SRAM[U9](2)
      val s_reg_4 = SRAM[U9](4)
      val v_reg_4 = SRAM[U9](3)
      val delta_reg_4 = SRAM[Int](2)

      val result_5 = SRAM[U9](2)
      val a_reg_5 = SRAM[U9](2)
      val s_reg_5 = SRAM[U9](4)
      val v_reg_5 = SRAM[U9](3)
      val delta_reg_5 = SRAM[Int](2)

      val result_6 = SRAM[U9](2)
      val a_reg_6 = SRAM[U9](2)
      val s_reg_6 = SRAM[U9](4)
      val v_reg_6 = SRAM[U9](3)
      val delta_reg_6 = SRAM[Int](2)

      val result_7 = SRAM[U9](2)
      val a_reg_7 = SRAM[U9](2)
      val s_reg_7 = SRAM[U9](4)
      val v_reg_7 = SRAM[U9](3)
      val delta_reg_7 = SRAM[Int](2)

      val result_8 = SRAM[U9](2)
      val a_reg_8 = SRAM[U9](2)
      val s_reg_8 = SRAM[U9](4)
      val v_reg_8 = SRAM[U9](3)
      val delta_reg_8 = SRAM[Int](2)

      val result_9 = SRAM[U9](2)
      val a_reg_9 = SRAM[U9](2)
      val s_reg_9 = SRAM[U9](4)
      val v_reg_9 = SRAM[U9](3)
      val delta_reg_9 = SRAM[Int](2)

      val result_10 = SRAM[U9](2)
      val a_reg_10 = SRAM[U9](2)
      val s_reg_10 = SRAM[U9](4)
      val v_reg_10 = SRAM[U9](3)
      val delta_reg_10 = SRAM[Int](2)

      val result_11 = SRAM[U9](2)
      val a_reg_11 = SRAM[U9](2)
      val s_reg_11 = SRAM[U9](4)
      val v_reg_11 = SRAM[U9](3)
      val delta_reg_11 = SRAM[Int](2)

      val result_12 = SRAM[U9](2)
      val a_reg_12 = SRAM[U9](2)
      val s_reg_12 = SRAM[U9](4)
      val v_reg_12 = SRAM[U9](3)
      val delta_reg_12 = SRAM[Int](2)

      val result_13 = SRAM[U9](2)
      val a_reg_13 = SRAM[U9](2)
      val s_reg_13 = SRAM[U9](4)
      val v_reg_13 = SRAM[U9](3)
      val delta_reg_13 = SRAM[Int](2)

      val result_14 = SRAM[U9](2)
      val a_reg_14 = SRAM[U9](2)
      val s_reg_14 = SRAM[U9](4)
      val v_reg_14 = SRAM[U9](3)
      val delta_reg_14 = SRAM[Int](2)

      val result_15 = SRAM[U9](2)
      val a_reg_15 = SRAM[U9](2)
      val s_reg_15 = SRAM[U9](4)
      val v_reg_15 = SRAM[U9](3)
      val delta_reg_15 = SRAM[Int](2)

      Stream.Foreach(*) { stream_idx =>

        val input = SRAM[U8](1) // Lets assume 1 inversion for now
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
          }
          dummy_input_fifo.enq(input(0))

        }
        
        // PASS 1

        Pipe {
          val dummy = dummy_input_fifo.deq()

          a_reg_0(0) = input(0).as[U9]
          s_reg_0(0) = irred
          v_reg_0(0) = 0
          delta_reg_0(0) = 0
          result_0(0) = 1 

          if(a_reg_0(0).bit(0) == 0) {
            a_reg_0(1) = a_reg_0(0) << 1
            result_0(1) = result_0(0) << 1
            delta_reg_0(1) = delta_reg_0(0) + 1
            s_reg_0(3) = s_reg_0(0)
            v_reg_0(2) = v_reg_0(0)

          } else {
            
            if(s_reg_0(0).bit(N) == 1) {
              s_reg_0(1) = (s_reg_0(0) ^ a_reg_0(0)).as[U9]
              v_reg_0(1) = (v_reg_0(0) ^ result_0(0)).as[U9] 
            } else {
              v_reg_0(1) = v_reg_0(0)
              s_reg_0(1) = s_reg_0(0)
            }

            s_reg_0(2) = s_reg_0(1) << 1

            if(delta_reg_0(0) == 0) {
              val tmp1 = a_reg_0(0)
              a_reg_0(1) = s_reg_0(2)
              s_reg_0(3) = tmp1

              val tmp2 = result_0(0)
              result_0(1) = v_reg_0(1) << 1
              v_reg_0(2) = tmp2

              delta_reg_0(1) = 1
            
            } else {
              result_0(1) = result_0(0) >> 1
              delta_reg_0(1) = delta_reg_0(0) - 1
              a_reg_0(1) = a_reg_0(0)
              s_reg_0(3) = s_reg_0(2)
              v_reg_0(2) = v_reg_0(1)
            }
          }
          // OUTPUT a_reg_0(1) result_0(1) s_reg_0(3) v_reg_0(2) delta_reg_0(1)
          dummy_stage0_fifo.enq(v_reg_0(2))
        }

        // PASS 2

        Pipe {
          val dummy = dummy_stage0_fifo.deq()

          a_reg_1(0) = a_reg_0(1)
          s_reg_1(0) = s_reg_0(3)
          v_reg_1(0) = v_reg_0(2)
          delta_reg_1(0) = delta_reg_0(1)
          result_1(0) = result_0(1)

          if(a_reg_1(0).bit(0) == 0) {
            a_reg_1(1) = a_reg_1(0) << 1
            result_1(1) = result_1(0) << 1
            delta_reg_1(1) = delta_reg_1(0) + 1
            s_reg_1(3) = s_reg_1(0)
            v_reg_1(2) = v_reg_1(0)

          } else {
            
            if(s_reg_1(0).bit(N) == 1) {
              s_reg_1(1) = (s_reg_1(0) ^ a_reg_1(0)).as[U9]
              v_reg_1(1) = (v_reg_1(0) ^ result_1(0)).as[U9] 
            } else {
              v_reg_1(1) = v_reg_1(0)
              s_reg_1(1) = s_reg_1(0)
            }

            s_reg_1(2) = s_reg_1(1) << 1

            if(delta_reg_1(0) == 0) {
              val tmp1 = a_reg_1(0)
              a_reg_1(1) = s_reg_1(2)
              s_reg_1(3) = tmp1

              val tmp2 = result_1(0)
              result_1(1) = v_reg_1(1) << 1
              v_reg_1(2) = tmp2

              delta_reg_1(1) = 1
            
            } else {
              result_1(1) = result_1(0) >> 1
              delta_reg_1(1) = delta_reg_1(0) - 1
              a_reg_1(1) = a_reg_1(0)
              s_reg_1(3) = s_reg_1(2)
              v_reg_1(2) = v_reg_1(1)
            }
          }
          // OUTPUT a_reg_1(1) result_1(1) s_reg_1(3) v_reg_1(2) delta_reg_1(1)
          dummy_stage1_fifo.enq(v_reg_1(2))
        }

        // PASS 3

        Pipe {
          val dummy = dummy_stage1_fifo.deq()

          a_reg_2(0) = a_reg_1(1)
          s_reg_2(0) = s_reg_1(3)
          v_reg_2(0) = v_reg_1(2)
          delta_reg_2(0) = delta_reg_1(1)
          result_2(0) = result_1(1)

          if(a_reg_2(0).bit(0) == 0) {
            a_reg_2(1) = a_reg_2(0) << 1
            result_2(1) = result_2(0) << 1
            delta_reg_2(1) = delta_reg_2(0) + 1
            s_reg_2(3) = s_reg_2(0)
            v_reg_2(2) = v_reg_2(0)

          } else {
            
            if(s_reg_2(0).bit(N) == 1) {
              s_reg_2(1) = (s_reg_2(0) ^ a_reg_2(0)).as[U9]
              v_reg_2(1) = (v_reg_2(0) ^ result_2(0)).as[U9] 
            } else {
              v_reg_2(1) = v_reg_2(0)
              s_reg_2(1) = s_reg_2(0)
            }

            s_reg_2(2) = s_reg_2(1) << 1

            if(delta_reg_2(0) == 0) {
              val tmp1 = a_reg_2(0)
              a_reg_2(1) = s_reg_2(2)
              s_reg_2(3) = tmp1

              val tmp2 = result_2(0)
              result_2(1) = v_reg_2(1) << 1
              v_reg_2(2) = tmp2

              delta_reg_2(1) = 1
            
            } else {
              result_2(1) = result_2(0) >> 1
              delta_reg_2(1) = delta_reg_2(0) - 1
              a_reg_2(1) = a_reg_2(0)
              s_reg_2(3) = s_reg_2(2)
              v_reg_2(2) = v_reg_2(1)
            }
          }
          // OUTPUT a_reg_2(1) result_2(1) s_reg_2(3) v_reg_2(2) delta_reg_2(1)
          dummy_stage2_fifo.enq(v_reg_2(2))
        }

        // PASS 4

        Pipe {
          val dummy = dummy_stage2_fifo.deq()

          a_reg_3(0) = a_reg_2(1)
          s_reg_3(0) = s_reg_2(3)
          v_reg_3(0) = v_reg_2(2)
          delta_reg_3(0) = delta_reg_2(1)
          result_3(0) = result_2(1)

          if(a_reg_3(0).bit(0) == 0) {
            a_reg_3(1) = a_reg_3(0) << 1
            result_3(1) = result_3(0) << 1
            delta_reg_3(1) = delta_reg_3(0) + 1
            s_reg_3(3) = s_reg_3(0)
            v_reg_3(2) = v_reg_3(0)

          } else {
            
            if(s_reg_3(0).bit(N) == 1) {
              s_reg_3(1) = (s_reg_3(0) ^ a_reg_3(0)).as[U9]
              v_reg_3(1) = (v_reg_3(0) ^ result_3(0)).as[U9] 
            } else {
              v_reg_3(1) = v_reg_3(0)
              s_reg_3(1) = s_reg_3(0)
            }

            s_reg_3(2) = s_reg_3(1) << 1

            if(delta_reg_3(0) == 0) {
              val tmp1 = a_reg_3(0)
              a_reg_3(1) = s_reg_3(2)
              s_reg_3(3) = tmp1

              val tmp2 = result_3(0)
              result_3(1) = v_reg_3(1) << 1
              v_reg_3(2) = tmp2

              delta_reg_3(1) = 1
            
            } else {
              result_3(1) = result_3(0) >> 1
              delta_reg_3(1) = delta_reg_3(0) - 1
              a_reg_3(1) = a_reg_3(0)
              s_reg_3(3) = s_reg_3(2)
              v_reg_3(2) = v_reg_3(1)
            }
          }
          // OUTPUT a_reg_3(1) result_3(1) s_reg_3(3) v_reg_3(2) delta_reg_3(1)
          dummy_stage3_fifo.enq(v_reg_3(2))
        }

        // PASS 5

        Pipe {
          val dummy = dummy_stage3_fifo.deq()

          a_reg_4(0) = a_reg_3(1)
          s_reg_4(0) = s_reg_3(3)
          v_reg_4(0) = v_reg_3(2)
          delta_reg_4(0) = delta_reg_3(1)
          result_4(0) = result_3(1)

          if(a_reg_4(0).bit(0) == 0) {
            a_reg_4(1) = a_reg_4(0) << 1
            result_4(1) = result_4(0) << 1
            delta_reg_4(1) = delta_reg_4(0) + 1
            s_reg_4(3) = s_reg_4(0)
            v_reg_4(2) = v_reg_4(0)

          } else {
            
            if(s_reg_4(0).bit(N) == 1) {
              s_reg_4(1) = (s_reg_4(0) ^ a_reg_4(0)).as[U9]
              v_reg_4(1) = (v_reg_4(0) ^ result_4(0)).as[U9] 
            } else {
              v_reg_4(1) = v_reg_4(0)
              s_reg_4(1) = s_reg_4(0)
            }

            s_reg_4(2) = s_reg_4(1) << 1

            if(delta_reg_4(0) == 0) {
              val tmp1 = a_reg_4(0)
              a_reg_4(1) = s_reg_4(2)
              s_reg_4(3) = tmp1

              val tmp2 = result_4(0)
              result_4(1) = v_reg_4(1) << 1
              v_reg_4(2) = tmp2

              delta_reg_4(1) = 1
            
            } else {
              result_4(1) = result_4(0) >> 1
              delta_reg_4(1) = delta_reg_4(0) - 1
              a_reg_4(1) = a_reg_4(0)
              s_reg_4(3) = s_reg_4(2)
              v_reg_4(2) = v_reg_4(1)
            }
          }
          // OUTPUT a_reg_4(1) result_4(1) s_reg_4(3) v_reg_4(2) delta_reg_4(1)
          dummy_stage4_fifo.enq(v_reg_4(2))
        }

        // PASS 6

        Pipe {
          val dummy = dummy_stage4_fifo.deq()

          a_reg_5(0) = a_reg_4(1)
          s_reg_5(0) = s_reg_4(3)
          v_reg_5(0) = v_reg_4(2)
          delta_reg_5(0) = delta_reg_4(1)
          result_5(0) = result_4(1)

          if(a_reg_5(0).bit(0) == 0) {
            a_reg_5(1) = a_reg_5(0) << 1
            result_5(1) = result_5(0) << 1
            delta_reg_5(1) = delta_reg_5(0) + 1
            s_reg_5(3) = s_reg_5(0)
            v_reg_5(2) = v_reg_5(0)

          } else {
            
            if(s_reg_5(0).bit(N) == 1) {
              s_reg_5(1) = (s_reg_5(0) ^ a_reg_5(0)).as[U9]
              v_reg_5(1) = (v_reg_5(0) ^ result_5(0)).as[U9] 
            } else {
              v_reg_5(1) = v_reg_5(0)
              s_reg_5(1) = s_reg_5(0)
            }

            s_reg_5(2) = s_reg_5(1) << 1

            if(delta_reg_5(0) == 0) {
              val tmp1 = a_reg_5(0)
              a_reg_5(1) = s_reg_5(2)
              s_reg_5(3) = tmp1

              val tmp2 = result_5(0)
              result_5(1) = v_reg_5(1) << 1
              v_reg_5(2) = tmp2

              delta_reg_5(1) = 1
            
            } else {
              result_5(1) = result_5(0) >> 1
              delta_reg_5(1) = delta_reg_5(0) - 1
              a_reg_5(1) = a_reg_5(0)
              s_reg_5(3) = s_reg_5(2)
              v_reg_5(2) = v_reg_5(1)
            }
          }
          // OUTPUT a_reg_5(1) result_5(1) s_reg_5(3) v_reg_5(2) delta_reg_5(1)
          dummy_stage5_fifo.enq(v_reg_5(2))
        }

        // PASS 7

        Pipe {
          val dummy = dummy_stage5_fifo.deq()

          a_reg_6(0) = a_reg_5(1)
          s_reg_6(0) = s_reg_5(3)
          v_reg_6(0) = v_reg_5(2)
          delta_reg_6(0) = delta_reg_5(1)
          result_6(0) = result_5(1)

          if(a_reg_6(0).bit(0) == 0) {
            a_reg_6(1) = a_reg_6(0) << 1
            result_6(1) = result_6(0) << 1
            delta_reg_6(1) = delta_reg_6(0) + 1
            s_reg_6(3) = s_reg_6(0)
            v_reg_6(2) = v_reg_6(0)

          } else {
            
            if(s_reg_6(0).bit(N) == 1) {
              s_reg_6(1) = (s_reg_6(0) ^ a_reg_6(0)).as[U9]
              v_reg_6(1) = (v_reg_6(0) ^ result_6(0)).as[U9] 
            } else {
              v_reg_6(1) = v_reg_6(0)
              s_reg_6(1) = s_reg_6(0)
            }

            s_reg_6(2) = s_reg_6(1) << 1

            if(delta_reg_6(0) == 0) {
              val tmp1 = a_reg_6(0)
              a_reg_6(1) = s_reg_6(2)
              s_reg_6(3) = tmp1

              val tmp2 = result_6(0)
              result_6(1) = v_reg_6(1) << 1
              v_reg_6(2) = tmp2

              delta_reg_6(1) = 1
            
            } else {
              result_6(1) = result_6(0) >> 1
              delta_reg_6(1) = delta_reg_6(0) - 1
              a_reg_6(1) = a_reg_6(0)
              s_reg_6(3) = s_reg_6(2)
              v_reg_6(2) = v_reg_6(1)
            }
          }
          // OUTPUT a_reg_6(1) result_6(1) s_reg_6(3) v_reg_6(2) delta_reg_6(1)
          dummy_stage6_fifo.enq(v_reg_6(2))
        }

        // PASS 8

        Pipe {
          val dummy = dummy_stage6_fifo.deq()

          a_reg_7(0) = a_reg_6(1)
          s_reg_7(0) = s_reg_6(3)
          v_reg_7(0) = v_reg_6(2)
          delta_reg_7(0) = delta_reg_6(1)
          result_7(0) = result_6(1)

          if(a_reg_7(0).bit(0) == 0) {
            a_reg_7(1) = a_reg_7(0) << 1
            result_7(1) = result_7(0) << 1
            delta_reg_7(1) = delta_reg_7(0) + 1
            s_reg_7(3) = s_reg_7(0)
            v_reg_7(2) = v_reg_7(0)

          } else {
            
            if(s_reg_7(0).bit(N) == 1) {
              s_reg_7(1) = (s_reg_7(0) ^ a_reg_7(0)).as[U9]
              v_reg_7(1) = (v_reg_7(0) ^ result_7(0)).as[U9] 
            } else {
              v_reg_7(1) = v_reg_7(0)
              s_reg_7(1) = s_reg_7(0)
            }

            s_reg_7(2) = s_reg_7(1) << 1

            if(delta_reg_7(0) == 0) {
              val tmp1 = a_reg_7(0)
              a_reg_7(1) = s_reg_7(2)
              s_reg_7(3) = tmp1

              val tmp2 = result_7(0)
              result_7(1) = v_reg_7(1) << 1
              v_reg_7(2) = tmp2

              delta_reg_7(1) = 1
            
            } else {
              result_7(1) = result_7(0) >> 1
              delta_reg_7(1) = delta_reg_7(0) - 1
              a_reg_7(1) = a_reg_7(0)
              s_reg_7(3) = s_reg_7(2)
              v_reg_7(2) = v_reg_7(1)
            }
          }
          // OUTPUT a_reg_7(1) result_7(1) s_reg_7(3) v_reg_7(2) delta_reg_7(1)
          dummy_stage7_fifo.enq(v_reg_7(2))
        }

        // PASS 9

        Pipe {
          val dummy = dummy_stage7_fifo.deq()

          a_reg_8(0) = a_reg_7(1)
          s_reg_8(0) = s_reg_7(3)
          v_reg_8(0) = v_reg_7(2)
          delta_reg_8(0) = delta_reg_7(1)
          result_8(0) = result_7(1)

          if(a_reg_8(0).bit(0) == 0) {
            a_reg_8(1) = a_reg_8(0) << 1
            result_8(1) = result_8(0) << 1
            delta_reg_8(1) = delta_reg_8(0) + 1
            s_reg_8(3) = s_reg_8(0)
            v_reg_8(2) = v_reg_8(0)

          } else {
            
            if(s_reg_8(0).bit(N) == 1) {
              s_reg_8(1) = (s_reg_8(0) ^ a_reg_8(0)).as[U9]
              v_reg_8(1) = (v_reg_8(0) ^ result_8(0)).as[U9] 
            } else {
              v_reg_8(1) = v_reg_8(0)
              s_reg_8(1) = s_reg_8(0)
            }

            s_reg_8(2) = s_reg_8(1) << 1

            if(delta_reg_8(0) == 0) {
              val tmp1 = a_reg_8(0)
              a_reg_8(1) = s_reg_8(2)
              s_reg_8(3) = tmp1

              val tmp2 = result_8(0)
              result_8(1) = v_reg_8(1) << 1
              v_reg_8(2) = tmp2

              delta_reg_8(1) = 1
            
            } else {
              result_8(1) = result_8(0) >> 1
              delta_reg_8(1) = delta_reg_8(0) - 1
              a_reg_8(1) = a_reg_8(0)
              s_reg_8(3) = s_reg_8(2)
              v_reg_8(2) = v_reg_8(1)
            }
          }
          // OUTPUT a_reg_8(1) result_8(1) s_reg_8(3) v_reg_8(2) delta_reg_8(1)
          dummy_stage8_fifo.enq(v_reg_8(2))
        }

        // PASS 10

        Pipe {
          val dummy = dummy_stage8_fifo.deq()

          a_reg_9(0) = a_reg_8(1)
          s_reg_9(0) = s_reg_8(3)
          v_reg_9(0) = v_reg_8(2)
          delta_reg_9(0) = delta_reg_8(1)
          result_9(0) = result_8(1)

          if(a_reg_9(0).bit(0) == 0) {
            a_reg_9(1) = a_reg_9(0) << 1
            result_9(1) = result_9(0) << 1
            delta_reg_9(1) = delta_reg_9(0) + 1
            s_reg_9(3) = s_reg_9(0)
            v_reg_9(2) = v_reg_9(0)

          } else {
            
            if(s_reg_9(0).bit(N) == 1) {
              s_reg_9(1) = (s_reg_9(0) ^ a_reg_9(0)).as[U9]
              v_reg_9(1) = (v_reg_9(0) ^ result_9(0)).as[U9] 
            } else {
              v_reg_9(1) = v_reg_9(0)
              s_reg_9(1) = s_reg_9(0)
            }

            s_reg_9(2) = s_reg_9(1) << 1

            if(delta_reg_9(0) == 0) {
              val tmp1 = a_reg_9(0)
              a_reg_9(1) = s_reg_9(2)
              s_reg_9(3) = tmp1

              val tmp2 = result_9(0)
              result_9(1) = v_reg_9(1) << 1
              v_reg_9(2) = tmp2

              delta_reg_9(1) = 1
            
            } else {
              result_9(1) = result_9(0) >> 1
              delta_reg_9(1) = delta_reg_9(0) - 1
              a_reg_9(1) = a_reg_9(0)
              s_reg_9(3) = s_reg_9(2)
              v_reg_9(2) = v_reg_9(1)
            }
          }
          // OUTPUT a_reg_9(1) result_9(1) s_reg_9(3) v_reg_9(2) delta_reg_9(1)
          dummy_stage9_fifo.enq(v_reg_9(2))
        }

        // PASS 11

        Pipe {
          val dummy = dummy_stage9_fifo.deq()

          a_reg_10(0) = a_reg_9(1)
          s_reg_10(0) = s_reg_9(3)
          v_reg_10(0) = v_reg_9(2)
          delta_reg_10(0) = delta_reg_9(1)
          result_10(0) = result_9(1)

          if(a_reg_10(0).bit(0) == 0) {
            a_reg_10(1) = a_reg_10(0) << 1
            result_10(1) = result_10(0) << 1
            delta_reg_10(1) = delta_reg_10(0) + 1
            s_reg_10(3) = s_reg_10(0)
            v_reg_10(2) = v_reg_10(0)

          } else {
            
            if(s_reg_10(0).bit(N) == 1) {
              s_reg_10(1) = (s_reg_10(0) ^ a_reg_10(0)).as[U9]
              v_reg_10(1) = (v_reg_10(0) ^ result_10(0)).as[U9] 
            } else {
              v_reg_10(1) = v_reg_10(0)
              s_reg_10(1) = s_reg_10(0)
            }

            s_reg_10(2) = s_reg_10(1) << 1

            if(delta_reg_10(0) == 0) {
              val tmp1 = a_reg_10(0)
              a_reg_10(1) = s_reg_10(2)
              s_reg_10(3) = tmp1

              val tmp2 = result_10(0)
              result_10(1) = v_reg_10(1) << 1
              v_reg_10(2) = tmp2

              delta_reg_10(1) = 1
            
            } else {
              result_10(1) = result_10(0) >> 1
              delta_reg_10(1) = delta_reg_10(0) - 1
              a_reg_10(1) = a_reg_10(0)
              s_reg_10(3) = s_reg_10(2)
              v_reg_10(2) = v_reg_10(1)
            }
          }
          // OUTPUT a_reg_10(1) result_10(1) s_reg_10(3) v_reg_10(2) delta_reg_10(1)
          dummy_stage10_fifo.enq(v_reg_10(2))
        }

        // PASS 12

        Pipe {
          val dummy = dummy_stage10_fifo.deq()

          a_reg_11(0) = a_reg_10(1)
          s_reg_11(0) = s_reg_10(3)
          v_reg_11(0) = v_reg_10(2)
          delta_reg_11(0) = delta_reg_10(1)
          result_11(0) = result_10(1)

          if(a_reg_11(0).bit(0) == 0) {
            a_reg_11(1) = a_reg_11(0) << 1
            result_11(1) = result_11(0) << 1
            delta_reg_11(1) = delta_reg_11(0) + 1
            s_reg_11(3) = s_reg_11(0)
            v_reg_11(2) = v_reg_11(0)

          } else {
            
            if(s_reg_11(0).bit(N) == 1) {
              s_reg_11(1) = (s_reg_11(0) ^ a_reg_11(0)).as[U9]
              v_reg_11(1) = (v_reg_11(0) ^ result_11(0)).as[U9] 
            } else {
              v_reg_11(1) = v_reg_11(0)
              s_reg_11(1) = s_reg_11(0)
            }

            s_reg_11(2) = s_reg_11(1) << 1

            if(delta_reg_11(0) == 0) {
              val tmp1 = a_reg_11(0)
              a_reg_11(1) = s_reg_11(2)
              s_reg_11(3) = tmp1

              val tmp2 = result_11(0)
              result_11(1) = v_reg_11(1) << 1
              v_reg_11(2) = tmp2

              delta_reg_11(1) = 1
            
            } else {
              result_11(1) = result_11(0) >> 1
              delta_reg_11(1) = delta_reg_11(0) - 1
              a_reg_11(1) = a_reg_11(0)
              s_reg_11(3) = s_reg_11(2)
              v_reg_11(2) = v_reg_11(1)
            }
          }
          // OUTPUT a_reg_11(1) result_11(1) s_reg_11(3) v_reg_11(2) delta_reg_11(1)
          dummy_stage11_fifo.enq(v_reg_11(2))
        }

        // PASS 13

        Pipe {
          val dummy = dummy_stage11_fifo.deq()

          a_reg_12(0) = a_reg_11(1)
          s_reg_12(0) = s_reg_11(3)
          v_reg_12(0) = v_reg_11(2)
          delta_reg_12(0) = delta_reg_11(1)
          result_12(0) = result_11(1)

          if(a_reg_12(0).bit(0) == 0) {
            a_reg_12(1) = a_reg_12(0) << 1
            result_12(1) = result_12(0) << 1
            delta_reg_12(1) = delta_reg_12(0) + 1
            s_reg_12(3) = s_reg_12(0)
            v_reg_12(2) = v_reg_12(0)

          } else {
            
            if(s_reg_12(0).bit(N) == 1) {
              s_reg_12(1) = (s_reg_12(0) ^ a_reg_12(0)).as[U9]
              v_reg_12(1) = (v_reg_12(0) ^ result_12(0)).as[U9] 
            } else {
              v_reg_12(1) = v_reg_12(0)
              s_reg_12(1) = s_reg_12(0)
            }

            s_reg_12(2) = s_reg_12(1) << 1

            if(delta_reg_12(0) == 0) {
              val tmp1 = a_reg_12(0)
              a_reg_12(1) = s_reg_12(2)
              s_reg_12(3) = tmp1

              val tmp2 = result_12(0)
              result_12(1) = v_reg_12(1) << 1
              v_reg_12(2) = tmp2

              delta_reg_12(1) = 1
            
            } else {
              result_12(1) = result_12(0) >> 1
              delta_reg_12(1) = delta_reg_12(0) - 1
              a_reg_12(1) = a_reg_12(0)
              s_reg_12(3) = s_reg_12(2)
              v_reg_12(2) = v_reg_12(1)
            }
          }
          // OUTPUT a_reg_12(1) result_12(1) s_reg_12(3) v_reg_12(2) delta_reg_12(1)
          dummy_stage12_fifo.enq(v_reg_12(2))
        }

        // PASS 14

        Pipe {
          val dummy = dummy_stage12_fifo.deq()

          a_reg_13(0) = a_reg_12(1)
          s_reg_13(0) = s_reg_12(3)
          v_reg_13(0) = v_reg_12(2)
          delta_reg_13(0) = delta_reg_12(1)
          result_13(0) = result_12(1)

          if(a_reg_13(0).bit(0) == 0) {
            a_reg_13(1) = a_reg_13(0) << 1
            result_13(1) = result_13(0) << 1
            delta_reg_13(1) = delta_reg_13(0) + 1
            s_reg_13(3) = s_reg_13(0)
            v_reg_13(2) = v_reg_13(0)

          } else {
            
            if(s_reg_13(0).bit(N) == 1) {
              s_reg_13(1) = (s_reg_13(0) ^ a_reg_13(0)).as[U9]
              v_reg_13(1) = (v_reg_13(0) ^ result_13(0)).as[U9] 
            } else {
              v_reg_13(1) = v_reg_13(0)
              s_reg_13(1) = s_reg_13(0)
            }

            s_reg_13(2) = s_reg_13(1) << 1

            if(delta_reg_13(0) == 0) {
              val tmp1 = a_reg_13(0)
              a_reg_13(1) = s_reg_13(2)
              s_reg_13(3) = tmp1

              val tmp2 = result_13(0)
              result_13(1) = v_reg_13(1) << 1
              v_reg_13(2) = tmp2

              delta_reg_13(1) = 1
            
            } else {
              result_13(1) = result_13(0) >> 1
              delta_reg_13(1) = delta_reg_13(0) - 1
              a_reg_13(1) = a_reg_13(0)
              s_reg_13(3) = s_reg_13(2)
              v_reg_13(2) = v_reg_13(1)
            }
          }
          // OUTPUT a_reg_13(1) result_13(1) s_reg_13(3) v_reg_13(2) delta_reg_13(1)
          dummy_stage13_fifo.enq(v_reg_13(2))
        }

        // PASS 15

        Pipe {
          val dummy = dummy_stage13_fifo.deq()

          a_reg_14(0) = a_reg_13(1)
          s_reg_14(0) = s_reg_13(3)
          v_reg_14(0) = v_reg_13(2)
          delta_reg_14(0) = delta_reg_13(1)
          result_14(0) = result_13(1)

          if(a_reg_14(0).bit(0) == 0) {
            a_reg_14(1) = a_reg_14(0) << 1
            result_14(1) = result_14(0) << 1
            delta_reg_14(1) = delta_reg_14(0) + 1
            s_reg_14(3) = s_reg_14(0)
            v_reg_14(2) = v_reg_14(0)

          } else {
            
            if(s_reg_14(0).bit(N) == 1) {
              s_reg_14(1) = (s_reg_14(0) ^ a_reg_14(0)).as[U9]
              v_reg_14(1) = (v_reg_14(0) ^ result_14(0)).as[U9] 
            } else {
              v_reg_14(1) = v_reg_14(0)
              s_reg_14(1) = s_reg_14(0)
            }

            s_reg_14(2) = s_reg_14(1) << 1

            if(delta_reg_14(0) == 0) {
              val tmp1 = a_reg_14(0)
              a_reg_14(1) = s_reg_14(2)
              s_reg_14(3) = tmp1

              val tmp2 = result_14(0)
              result_14(1) = v_reg_14(1) << 1
              v_reg_14(2) = tmp2

              delta_reg_14(1) = 1
            
            } else {
              result_14(1) = result_14(0) >> 1
              delta_reg_14(1) = delta_reg_14(0) - 1
              a_reg_14(1) = a_reg_14(0)
              s_reg_14(3) = s_reg_14(2)
              v_reg_14(2) = v_reg_14(1)
            }
          }
          // OUTPUT a_reg_14(1) result_14(1) s_reg_14(3) v_reg_14(2) delta_reg_14(1)
          dummy_stage14_fifo.enq(v_reg_14(2))
        }

        // PASS 16

        Pipe {
          val dummy = dummy_stage14_fifo.deq()

          a_reg_15(0) = a_reg_14(1)
          s_reg_15(0) = s_reg_14(3)
          v_reg_15(0) = v_reg_14(2)
          delta_reg_15(0) = delta_reg_14(1)
          result_15(0) = result_14(1)

          if(a_reg_15(0).bit(0) == 0) {
            a_reg_15(1) = a_reg_15(0) << 1
            result_15(1) = result_15(0) << 1
            delta_reg_15(1) = delta_reg_15(0) + 1
            s_reg_15(3) = s_reg_15(0)
            v_reg_15(2) = v_reg_15(0)

          } else {
            
            if(s_reg_15(0).bit(N) == 1) {
              s_reg_15(1) = (s_reg_15(0) ^ a_reg_15(0)).as[U9]
              v_reg_15(1) = (v_reg_15(0) ^ result_15(0)).as[U9] 
            } else {
              v_reg_15(1) = v_reg_15(0)
              s_reg_15(1) = s_reg_15(0)
            }

            s_reg_15(2) = s_reg_15(1) << 1

            if(delta_reg_15(0) == 0) {
              val tmp1 = a_reg_15(0)
              a_reg_15(1) = s_reg_15(2)
              s_reg_15(3) = tmp1

              val tmp2 = result_15(0)
              result_15(1) = v_reg_15(1) << 1
              v_reg_15(2) = tmp2

              delta_reg_15(1) = 1
            
            } else {
              result_15(1) = result_15(0) >> 1
              delta_reg_15(1) = delta_reg_15(0) - 1
              a_reg_15(1) = a_reg_15(0)
              s_reg_15(3) = s_reg_15(2)
              v_reg_15(2) = v_reg_15(1)
            }
          }
          // OUTPUT a_reg_15(1) result_15(1) s_reg_15(3) v_reg_15(2) delta_reg_15(1)
          dummy_stage15_fifo.enq(v_reg_15(2))
        }


        Pipe {
          val dummy = dummy_stage15_fifo.deq()

          val packet = packet_use2.deq()
          val newPacket = AxiStream512((packet.tdata.as[U512]) | (result_15(1).as[U512] << 504) , packet.tstrb, packet.tkeep, packet.tlast, packet.tid, 1, 0)
          outbus := newPacket
        }
         
      }
    }
    assert(1 == 1) // Assert keeps spatial happy
  }
}
