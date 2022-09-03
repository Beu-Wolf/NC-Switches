import spatial.dsl._
import spatial.lib.ML._
import utils.io.files._
import spatial.lang.{FileBus,FileEOFBus}
import spatial.metadata.bounds._

@spatial class FFInv_It_Pipe_v2 extends SpatialTest {
  // @struct case class AxiStream512(tdata: U512, tstrb: U64, tkeep: U64, tlast: Bit, tid: U8, tdest: U8, tuser: U64)
  
  val N = 100
  val FFSize = 8
  type T = U16
  
  def main(args: Array[String]): Unit = {
    val infile_a = buildPath(IR.config.genDir,"tungsten", "in_a.csv")
		val outfile = buildPath(IR.config.genDir,"tungsten", "out.csv")
		createDirectories(dirName(infile_a))
		val inputs = List.tabulate(N) {i => i}
		writeCSVNow(inputs, infile_a)

		val stream_a_in  = StreamIn[T](FileBus[T](infile_a))
		val stream_out  = StreamOut[Tup2[I32,Bit]](FileEOFBus[Tup2[I32,Bit]](outfile))

    Accel {
      val irred = 0x11b.to[T] // Irreducible polynomial of GF(2^8)
      val mask_msb = 0x1.to[T] << FFSize

      val result_0 = FIFO[T](4)
      val a_reg_0 = FIFO[T](4)
      val s_reg_0 = FIFO[T](4)
      val v_reg_0 = FIFO[T](4)
      val delta_reg_0 = FIFO[Int](4)

      val result_1 = FIFO[T](4)
      val a_reg_1 = FIFO[T](4)
      val s_reg_1 = FIFO[T](4)
      val v_reg_1 = FIFO[T](4)
      val delta_reg_1 = FIFO[Int](4)

      val result_2 = FIFO[T](4)
      val a_reg_2 = FIFO[T](4)
      val s_reg_2 = FIFO[T](4)
      val v_reg_2 = FIFO[T](4)
      val delta_reg_2 = FIFO[Int](4)

      val result_3 = FIFO[T](4)
      val a_reg_3 = FIFO[T](4)
      val s_reg_3 = FIFO[T](4)
      val v_reg_3 = FIFO[T](4)
      val delta_reg_3 = FIFO[Int](4)

      val result_4 = FIFO[T](4)
      val a_reg_4 = FIFO[T](4)
      val s_reg_4 = FIFO[T](4)
      val v_reg_4 = FIFO[T](4)
      val delta_reg_4 = FIFO[Int](4)

      val result_5 = FIFO[T](4)
      val a_reg_5 = FIFO[T](4)
      val s_reg_5 = FIFO[T](4)
      val v_reg_5 = FIFO[T](4)
      val delta_reg_5 = FIFO[Int](4)

      val result_6 = FIFO[T](4)
      val a_reg_6 = FIFO[T](4)
      val s_reg_6 = FIFO[T](4)
      val v_reg_6 = FIFO[T](4)
      val delta_reg_6 = FIFO[Int](4)

      val result_7 = FIFO[T](4)
      val a_reg_7 = FIFO[T](4)
      val s_reg_7 = FIFO[T](4)
      val v_reg_7 = FIFO[T](4)
      val delta_reg_7 = FIFO[Int](4)

      val result_8 = FIFO[T](4)
      val a_reg_8 = FIFO[T](4)
      val s_reg_8 = FIFO[T](4)
      val v_reg_8 = FIFO[T](4)
      val delta_reg_8 = FIFO[Int](4)

      val result_9 = FIFO[T](4)
      val a_reg_9 = FIFO[T](4)
      val s_reg_9 = FIFO[T](4)
      val v_reg_9 = FIFO[T](4)
      val delta_reg_9 = FIFO[Int](4)

      val result_10 = FIFO[T](4)
      val a_reg_10 = FIFO[T](4)
      val s_reg_10 = FIFO[T](4)
      val v_reg_10 = FIFO[T](4)
      val delta_reg_10 = FIFO[Int](4)

      val result_11 = FIFO[T](4)
      val a_reg_11 = FIFO[T](4)
      val s_reg_11 = FIFO[T](4)
      val v_reg_11 = FIFO[T](4)
      val delta_reg_11 = FIFO[Int](4)

      val result_12 = FIFO[T](4)
      val a_reg_12 = FIFO[T](4)
      val s_reg_12 = FIFO[T](4)
      val v_reg_12 = FIFO[T](4)
      val delta_reg_12 = FIFO[Int](4)

      val result_13 = FIFO[T](4)
      val a_reg_13 = FIFO[T](4)
      val s_reg_13 = FIFO[T](4)
      val v_reg_13 = FIFO[T](4)
      val delta_reg_13 = FIFO[Int](4)

      val result_14 = FIFO[T](4)
      val a_reg_14 = FIFO[T](4)
      val s_reg_14 = FIFO[T](4)
      val v_reg_14 = FIFO[T](4)
      val delta_reg_14 = FIFO[Int](4)

      val result_15 = FIFO[T](4)

      Foreach(*) { p =>

        // PASS 1

        Pipe {

          val a: T = stream_a_in.value
          val s: T = irred
          val v: T = 0
          val delta: Int = 0
          val result: T = 1

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_0.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_0.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_0.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_0.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_0.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))

        }

        // PASS 2

        Pipe {

          val a: T = a_reg_0.deq()
          val s: T = s_reg_0.deq()
          val v: T = v_reg_0.deq()
          val delta: Int = delta_reg_0.deq()
          val result: T = result_0.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_1.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_1.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_1.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_1.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_1.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 3

        Pipe {

          val a: T = a_reg_1.deq()
          val s: T = s_reg_1.deq()
          val v: T = v_reg_1.deq()
          val delta: Int = delta_reg_1.deq()
          val result: T = result_1.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_2.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_2.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_2.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_2.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_2.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 4

        Pipe {

          val a: T = a_reg_2.deq()
          val s: T = s_reg_2.deq()
          val v: T = v_reg_2.deq()
          val delta: Int = delta_reg_2.deq()
          val result: T = result_2.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_3.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_3.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_3.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_3.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_3.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 5

        Pipe {

          val a: T = a_reg_3.deq()
          val s: T = s_reg_3.deq()
          val v: T = v_reg_3.deq()
          val delta: Int = delta_reg_3.deq()
          val result: T = result_3.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_4.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_4.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_4.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_4.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_4.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 6

        Pipe {

          val a: T = a_reg_4.deq()
          val s: T = s_reg_4.deq()
          val v: T = v_reg_4.deq()
          val delta: Int = delta_reg_4.deq()
          val result: T = result_4.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_5.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_5.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_5.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_5.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_5.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 7

        Pipe {

          val a: T = a_reg_5.deq()
          val s: T = s_reg_5.deq()
          val v: T = v_reg_5.deq()
          val delta: Int = delta_reg_5.deq()
          val result: T = result_5.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_6.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_6.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_6.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_6.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_6.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 8

        Pipe {

          val a: T = a_reg_6.deq()
          val s: T = s_reg_6.deq()
          val v: T = v_reg_6.deq()
          val delta: Int = delta_reg_6.deq()
          val result: T = result_6.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_7.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_7.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_7.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_7.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_7.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 9

        Pipe {

          val a: T = a_reg_7.deq()
          val s: T = s_reg_7.deq()
          val v: T = v_reg_7.deq()
          val delta: Int = delta_reg_7.deq()
          val result: T = result_7.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_8.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_8.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_8.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_8.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_8.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 10

        Pipe {

          val a: T = a_reg_8.deq()
          val s: T = s_reg_8.deq()
          val v: T = v_reg_8.deq()
          val delta: Int = delta_reg_8.deq()
          val result: T = result_8.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_9.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_9.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_9.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_9.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_9.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 11

        Pipe {

          val a: T = a_reg_9.deq()
          val s: T = s_reg_9.deq()
          val v: T = v_reg_9.deq()
          val delta: Int = delta_reg_9.deq()
          val result: T = result_9.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_10.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_10.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_10.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_10.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_10.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 12

        Pipe {

          val a: T = a_reg_10.deq()
          val s: T = s_reg_10.deq()
          val v: T = v_reg_10.deq()
          val delta: Int = delta_reg_10.deq()
          val result: T = result_10.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_11.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_11.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_11.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_11.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_11.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 13

        Pipe {

          val a: T = a_reg_11.deq()
          val s: T = s_reg_11.deq()
          val v: T = v_reg_11.deq()
          val delta: Int = delta_reg_11.deq()
          val result: T = result_11.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_12.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_12.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_12.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_12.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_12.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 14

        Pipe {

          val a: T = a_reg_12.deq()
          val s: T = s_reg_12.deq()
          val v: T = v_reg_12.deq()
          val delta: Int = delta_reg_12.deq()
          val result: T = result_12.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_13.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_13.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_13.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_13.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_13.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 15

        Pipe {

          val a: T = a_reg_13.deq()
          val s: T = s_reg_13.deq()
          val v: T = v_reg_13.deq()
          val delta: Int = delta_reg_13.deq()
          val result: T = result_13.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb

          a_reg_14.enq(mux(flag_a, a << 1, mux(delta == 0, s << 1, a)))
          s_reg_14.enq(mux(flag_a, s, mux(flag_s, mux(delta == 0, a, (s ^ a) << 1), mux(delta == 0, a, s << 1))))
          v_reg_14.enq(mux(flag_a, v, mux(delta == 0, result, mux(flag_s, result ^ v, v))))
          delta_reg_14.enq(mux(flag_a, delta + 1, mux(delta == 0, 1, delta - 1)))
          result_14.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        // PASS 16

        Pipe {

          val a: T = a_reg_14.deq()
          val s: T = s_reg_14.deq()
          val v: T = v_reg_14.deq()
          val delta: Int = delta_reg_14.deq()
          val result: T = result_14.deq()

          // When true, the most significant bit of a is NOT 1 
          val flag_a = (a & mask_msb) != mask_msb
          // When true, the most significant bit of a is 1 
          val flag_s = (s & mask_msb) == mask_msb
          
          result_15.enq(mux(flag_a, result, mux(delta == 0, mux(flag_s, (result ^ v) << 1, v << 1), result >> 1)))
        }

        stream_out := Tup2(result_15.deq().to[I32], p == (N-1))
         
      }
    }
    assert(1 == 1) // Assert keeps spatial happy
  }
}
