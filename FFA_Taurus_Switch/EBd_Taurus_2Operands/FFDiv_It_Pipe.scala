import spatial.dsl._
import spatial.lib.ML._
import utils.io.files._
import spatial.lang.{FileBus,FileEOFBus}
import spatial.metadata.bounds._

@spatial class FFDiv_It_Pipe_v2 extends SpatialTest {
  // @struct case class AxiStream512(tdata: U512, tstrb: U64, tkeep: U64, tlast: Bit, tid: U8, tdest: U8, tuser: U64)
  
  val N = 100
  val FFSize = 8
  type T = U16
  
  def main(args: Array[String]): Unit = {
    val infile_a = buildPath(IR.config.genDir,"tungsten", "in_a.csv")
		val infile_b = buildPath(IR.config.genDir,"tungsten", "in_b.csv")
		val outfile = buildPath(IR.config.genDir,"tungsten", "out.csv")
		createDirectories(dirName(infile_a))
		// createDirectories(dirName(infile_b))
		val inputs = List.tabulate(N) {i => i}
		writeCSVNow(inputs, infile_a)
		writeCSVNow(inputs, infile_b)

		val stream_a_in  = StreamIn[U8](FileBus[U8](infile_a))
		val stream_b_in  = StreamIn[U8](FileBus[U8](infile_b))
		val stream_out  = StreamOut[Tup2[I32,Bit]](FileEOFBus[Tup2[I32,Bit]](outfile))

    Accel {
      val irred = 0x11b.to[T] // Irreducible polynomial of GF(2^8)
      
      val mask_a_2 = 0x1.to[U8] << 2
      val mask_a_3 = 0x1.to[U8] << 3

      val result_0 = FIFO[U8](4)
      val a_reg_0_0 = FIFO[U8](4)
      val a_reg_0_1 = FIFO[U8](4)
      val a_reg_0_2 = FIFO[U8](4)
      val a_reg_0_3 = FIFO[U8](4)
      val b_reg_0 = FIFO[T](4)
      val s_reg_0 = FIFO[T](4)
      val delta_reg_0 = FIFO[Int](4)
      val if_swap_reg_0_0 = FIFO[Boolean](4)
      val if_swap_reg_0_1 = FIFO[Boolean](4)
      val if_swap_reg_0_2 = FIFO[Boolean](4)

      val result_1 = FIFO[U8](4)
      val a_reg_1_0 = FIFO[U8](4)
      val a_reg_1_1 = FIFO[U8](4)
      val a_reg_1_2 = FIFO[U8](4)
      val a_reg_1_3 = FIFO[U8](4)
      val b_reg_1 = FIFO[T](4)
      val s_reg_1 = FIFO[T](4)
      val delta_reg_1 = FIFO[Int](4)
      val if_swap_reg_1_0 = FIFO[Boolean](4)
      val if_swap_reg_1_1 = FIFO[Boolean](4)
      val if_swap_reg_1_2 = FIFO[Boolean](4)

      val result_2 = FIFO[U8](4)
      val a_reg_2_0 = FIFO[U8](4)
      val a_reg_2_1 = FIFO[U8](4)
      val a_reg_2_2 = FIFO[U8](4)
      val a_reg_2_3 = FIFO[U8](4)
      val b_reg_2 = FIFO[T](4)
      val s_reg_2 = FIFO[T](4)
      val delta_reg_2 = FIFO[Int](4)
      val if_swap_reg_2_0 = FIFO[Boolean](4)
      val if_swap_reg_2_1 = FIFO[Boolean](4)
      val if_swap_reg_2_2 = FIFO[Boolean](4)

      val result_3 = FIFO[U8](4)
      val a_reg_3_0 = FIFO[U8](4)
      val a_reg_3_1 = FIFO[U8](4)
      val a_reg_3_2 = FIFO[U8](4)
      val a_reg_3_3 = FIFO[U8](4)
      val b_reg_3 = FIFO[T](4)
      val s_reg_3 = FIFO[T](4)
      val delta_reg_3 = FIFO[Int](4)
      val if_swap_reg_3_0 = FIFO[Boolean](4)
      val if_swap_reg_3_1 = FIFO[Boolean](4)
      val if_swap_reg_3_2 = FIFO[Boolean](4)

      val result_4 = FIFO[U8](4)
      val a_reg_4_0 = FIFO[U8](4)
      val a_reg_4_1 = FIFO[U8](4)
      val a_reg_4_2 = FIFO[U8](4)
      val a_reg_4_3 = FIFO[U8](4)
      val b_reg_4 = FIFO[T](4)
      val s_reg_4 = FIFO[T](4)
      val delta_reg_4 = FIFO[Int](4)
      val if_swap_reg_4_0 = FIFO[Boolean](4)
      val if_swap_reg_4_1 = FIFO[Boolean](4)
      val if_swap_reg_4_2 = FIFO[Boolean](4)

      val result_5 = FIFO[U8](4)
      val a_reg_5_0 = FIFO[U8](4)
      val a_reg_5_1 = FIFO[U8](4)
      val a_reg_5_2 = FIFO[U8](4)
      val a_reg_5_3 = FIFO[U8](4)
      val b_reg_5 = FIFO[T](4)
      val s_reg_5 = FIFO[T](4)
      val delta_reg_5 = FIFO[Int](4)
      val if_swap_reg_5_0 = FIFO[Boolean](4)
      val if_swap_reg_5_1 = FIFO[Boolean](4)
      val if_swap_reg_5_2 = FIFO[Boolean](4)

      val result_6 = FIFO[U8](4)
      val a_reg_6_0 = FIFO[U8](4)
      val a_reg_6_1 = FIFO[U8](4)
      val a_reg_6_2 = FIFO[U8](4)
      val a_reg_6_3 = FIFO[U8](4)
      val b_reg_6 = FIFO[T](4)
      val s_reg_6 = FIFO[T](4)
      val delta_reg_6 = FIFO[Int](4)
      val if_swap_reg_6_0 = FIFO[Boolean](4)
      val if_swap_reg_6_1 = FIFO[Boolean](4)
      val if_swap_reg_6_2 = FIFO[Boolean](4)

      val result_7 = FIFO[U8](4)
      val a_reg_7_0 = FIFO[U8](4)
      val a_reg_7_1 = FIFO[U8](4)
      val a_reg_7_2 = FIFO[U8](4)
      val a_reg_7_3 = FIFO[U8](4)
      val b_reg_7 = FIFO[T](4)
      val s_reg_7 = FIFO[T](4)
      val delta_reg_7 = FIFO[Int](4)
      val if_swap_reg_7_0 = FIFO[Boolean](4)
      val if_swap_reg_7_1 = FIFO[Boolean](4)
      val if_swap_reg_7_2 = FIFO[Boolean](4)

      val result_8 = FIFO[U8](4)
      val a_reg_8_0 = FIFO[U8](4)
      val a_reg_8_1 = FIFO[U8](4)
      val a_reg_8_2 = FIFO[U8](4)
      val a_reg_8_3 = FIFO[U8](4)
      val b_reg_8 = FIFO[T](4)
      val s_reg_8 = FIFO[T](4)
      val delta_reg_8 = FIFO[Int](4)
      val if_swap_reg_8_0 = FIFO[Boolean](4)
      val if_swap_reg_8_1 = FIFO[Boolean](4)
      val if_swap_reg_8_2 = FIFO[Boolean](4)

      val result_9 = FIFO[U8](4)
      val a_reg_9_0 = FIFO[U8](4)
      val a_reg_9_1 = FIFO[U8](4)
      val a_reg_9_2 = FIFO[U8](4)
      val a_reg_9_3 = FIFO[U8](4)
      val b_reg_9 = FIFO[T](4)
      val s_reg_9 = FIFO[T](4)
      val delta_reg_9 = FIFO[Int](4)
      val if_swap_reg_9_0 = FIFO[Boolean](4)
      val if_swap_reg_9_1 = FIFO[Boolean](4)
      val if_swap_reg_9_2 = FIFO[Boolean](4)

      val result_10 = FIFO[U8](4)
      val a_reg_10_0 = FIFO[U8](4)
      val a_reg_10_1 = FIFO[U8](4)
      val a_reg_10_2 = FIFO[U8](4)
      val a_reg_10_3 = FIFO[U8](4)
      val b_reg_10 = FIFO[T](4)
      val s_reg_10 = FIFO[T](4)
      val delta_reg_10 = FIFO[Int](4)
      val if_swap_reg_10_0 = FIFO[Boolean](4)
      val if_swap_reg_10_1 = FIFO[Boolean](4)
      val if_swap_reg_10_2 = FIFO[Boolean](4)

      val result_11 = FIFO[U8](4)
      val a_reg_11_0 = FIFO[U8](4)
      val a_reg_11_1 = FIFO[U8](4)
      val a_reg_11_2 = FIFO[U8](4)
      val a_reg_11_3 = FIFO[U8](4)
      val b_reg_11 = FIFO[T](4)
      val s_reg_11 = FIFO[T](4)
      val delta_reg_11 = FIFO[Int](4)
      val if_swap_reg_11_0 = FIFO[Boolean](4)
      val if_swap_reg_11_1 = FIFO[Boolean](4)
      val if_swap_reg_11_2 = FIFO[Boolean](4)

      val result_12 = FIFO[U8](4)
      val a_reg_12_0 = FIFO[U8](4)
      val a_reg_12_1 = FIFO[U8](4)
      val a_reg_12_2 = FIFO[U8](4)
      val a_reg_12_3 = FIFO[U8](4)
      val b_reg_12 = FIFO[T](4)
      val s_reg_12 = FIFO[T](4)
      val delta_reg_12 = FIFO[Int](4)
      val if_swap_reg_12_0 = FIFO[Boolean](4)
      val if_swap_reg_12_1 = FIFO[Boolean](4)
      val if_swap_reg_12_2 = FIFO[Boolean](4)

      val result_13 = FIFO[U8](4)
      val a_reg_13_0 = FIFO[U8](4)
      val a_reg_13_1 = FIFO[U8](4)
      val a_reg_13_2 = FIFO[U8](4)
      val a_reg_13_3 = FIFO[U8](4)
      val b_reg_13 = FIFO[T](4)
      //val s_reg_13 = FIFO[T](4)
      val delta_reg_13 = FIFO[Int](4)
      val if_swap_reg_13_0 = FIFO[Boolean](4)
      val if_swap_reg_13_1 = FIFO[Boolean](4)
      val if_swap_reg_13_2 = FIFO[Boolean](4)

      val result_14 = FIFO[U8](4)
      //val a_reg_14_0 = FIFO[U8](4)
      //val a_reg_14_1 = FIFO[U8](4)
      //val a_reg_14_2 = FIFO[U8](4)
      //val a_reg_14_3 = FIFO[U8](4)
      //val b_reg_14 = FIFO[T](4)
      //val s_reg_14 = FIFO[T](4)
      //val delta_reg_14 = FIFO[Int](4)
      //val if_swap_reg_14_0 = FIFO[Boolean](4)
      //val if_swap_reg_14_1 = FIFO[Boolean](4)
      //val if_swap_reg_14_2 = FIFO[Boolean](4)

      Stream.Foreach(*) { p =>
        
        // PASS 1

        Pipe {

          val a: U8 = stream_a_in.value
          val b: T = stream_b_in.value.as[T]
          val s: T = irred
          val delta: Int = -1
          val result: U8 = 0

          a_reg_0_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_0.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_0.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_0.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_0.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_0_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_0_0.deq()
        
          val if_swap = if_swap_reg_0_0.deq()
          if_swap_reg_0_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_0_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_0_1.deq()
        
          val if_swap = if_swap_reg_0_1.deq()
          if_swap_reg_0_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_0_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_0_2.deq()
        
          val if_swap = if_swap_reg_0_2.deq()
          //if_swap_reg_0.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_0_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 2

        Pipe {
          val a: U8 = a_reg_0_3.deq()
          val b: T = b_reg_0.deq()
          val s: T = s_reg_0.deq()
          val delta: Int = delta_reg_0.deq()
          val result: U8 = result_0.deq()

          a_reg_1_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_1.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_1.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_1.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_1.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_1_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_1_0.deq()
        
          val if_swap = if_swap_reg_1_0.deq()
          if_swap_reg_1_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_1_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_1_1.deq()
        
          val if_swap = if_swap_reg_1_1.deq()
          if_swap_reg_1_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_1_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_1_2.deq()
        
          val if_swap = if_swap_reg_1_2.deq()
          //if_swap_reg_1.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_1_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 3

        Pipe {
          val a: U8 = a_reg_1_3.deq()
          val b: T = b_reg_1.deq()
          val s: T = s_reg_1.deq()
          val delta: Int = delta_reg_1.deq()
          val result: U8 = result_1.deq()

          a_reg_2_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_2.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_2.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_2.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_2.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_2_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_2_0.deq()
        
          val if_swap = if_swap_reg_2_0.deq()
          if_swap_reg_2_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_2_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_2_1.deq()
        
          val if_swap = if_swap_reg_2_1.deq()
          if_swap_reg_2_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_2_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_2_2.deq()
        
          val if_swap = if_swap_reg_2_2.deq()
          //if_swap_reg_2.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_2_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 4

        Pipe {
          val a: U8 = a_reg_2_3.deq()
          val b: T = b_reg_2.deq()
          val s: T = s_reg_2.deq()
          val delta: Int = delta_reg_2.deq()
          val result: U8 = result_2.deq()

          a_reg_3_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_3.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_3.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_3.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_3.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_3_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_3_0.deq()
        
          val if_swap = if_swap_reg_3_0.deq()
          if_swap_reg_3_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_3_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_3_1.deq()
        
          val if_swap = if_swap_reg_3_1.deq()
          if_swap_reg_3_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_3_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_3_2.deq()
        
          val if_swap = if_swap_reg_3_2.deq()
          //if_swap_reg_3.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_3_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 5

        Pipe {
          val a: U8 = a_reg_3_3.deq()
          val b: T = b_reg_3.deq()
          val s: T = s_reg_3.deq()
          val delta: Int = delta_reg_3.deq()
          val result: U8 = result_3.deq()

          a_reg_4_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_4.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_4.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_4.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_4.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_4_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_4_0.deq()
        
          val if_swap = if_swap_reg_4_0.deq()
          if_swap_reg_4_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_4_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_4_1.deq()
        
          val if_swap = if_swap_reg_4_1.deq()
          if_swap_reg_4_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_4_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_4_2.deq()
        
          val if_swap = if_swap_reg_4_2.deq()
          //if_swap_reg_4.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_4_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 6

        Pipe {
          val a: U8 = a_reg_4_3.deq()
          val b: T = b_reg_4.deq()
          val s: T = s_reg_4.deq()
          val delta: Int = delta_reg_4.deq()
          val result: U8 = result_4.deq()

          a_reg_5_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_5.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_5.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_5.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_5.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_5_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_5_0.deq()
        
          val if_swap = if_swap_reg_5_0.deq()
          if_swap_reg_5_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_5_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_5_1.deq()
        
          val if_swap = if_swap_reg_5_1.deq()
          if_swap_reg_5_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_5_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_5_2.deq()
        
          val if_swap = if_swap_reg_5_2.deq()
          //if_swap_reg_5.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_5_3.enq(mux(if_swap, new_a_3, a))
        }
        
        // PASS 7

        Pipe {
          val a: U8 = a_reg_5_3.deq()
          val b: T = b_reg_5.deq()
          val s: T = s_reg_5.deq()
          val delta: Int = delta_reg_5.deq()
          val result: U8 = result_5.deq()

          a_reg_6_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_6.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_6.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_6.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_6.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_6_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_6_0.deq()
        
          val if_swap = if_swap_reg_6_0.deq()
          if_swap_reg_6_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_6_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_6_1.deq()
        
          val if_swap = if_swap_reg_6_1.deq()
          if_swap_reg_6_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_6_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_6_2.deq()
        
          val if_swap = if_swap_reg_6_2.deq()
          //if_swap_reg_6.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_6_3.enq(mux(if_swap, new_a_3, a))
        }
      
        // PASS 8

        Pipe {
          val a: U8 = a_reg_6_3.deq()
          val b: T = b_reg_6.deq()
          val s: T = s_reg_6.deq()
          val delta: Int = delta_reg_6.deq()
          val result: U8 = result_6.deq()

          a_reg_7_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_7.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_7.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_7.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_7.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_7_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_7_0.deq()
        
          val if_swap = if_swap_reg_7_0.deq()
          if_swap_reg_7_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_7_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_7_1.deq()
        
          val if_swap = if_swap_reg_7_1.deq()
          if_swap_reg_7_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_7_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_7_2.deq()
        
          val if_swap = if_swap_reg_7_2.deq()
          //if_swap_reg_7.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_7_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 9

        Pipe {
          val a: U8 = a_reg_7_3.deq()
          val b: T = b_reg_7.deq()
          val s: T = s_reg_7.deq()
          val delta: Int = delta_reg_7.deq()
          val result: U8 = result_7.deq()

          a_reg_8_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_8.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_8.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_8.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_8.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_8_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_8_0.deq()
        
          val if_swap = if_swap_reg_8_0.deq()
          if_swap_reg_8_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_8_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_8_1.deq()
        
          val if_swap = if_swap_reg_8_1.deq()
          if_swap_reg_8_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_8_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_8_2.deq()
        
          val if_swap = if_swap_reg_8_2.deq()
          //if_swap_reg_8.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_8_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 10

        Pipe {
          val a: U8 = a_reg_8_3.deq()
          val b: T = b_reg_8.deq()
          val s: T = s_reg_8.deq()
          val delta: Int = delta_reg_8.deq()
          val result: U8 = result_8.deq()

          a_reg_9_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_9.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_9.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_9.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_9.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_9_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_9_0.deq()
        
          val if_swap = if_swap_reg_9_0.deq()
          if_swap_reg_9_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_9_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_9_1.deq()
        
          val if_swap = if_swap_reg_9_1.deq()
          if_swap_reg_9_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_9_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_9_2.deq()
        
          val if_swap = if_swap_reg_9_2.deq()
          //if_swap_reg_9.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_9_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 11

        Pipe {
          val a: U8 = a_reg_9_3.deq()
          val b: T = b_reg_9.deq()
          val s: T = s_reg_9.deq()
          val delta: Int = delta_reg_9.deq()
          val result: U8 = result_9.deq()

          a_reg_10_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_10.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_10.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_10.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_10.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_10_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_10_0.deq()
        
          val if_swap = if_swap_reg_10_0.deq()
          if_swap_reg_10_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_10_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_10_1.deq()
        
          val if_swap = if_swap_reg_10_1.deq()
          if_swap_reg_10_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_10_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_10_2.deq()
        
          val if_swap = if_swap_reg_10_2.deq()
          //if_swap_reg_10.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_10_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 12

        Pipe {
          val a: U8 = a_reg_10_3.deq()
          val b: T = b_reg_10.deq()
          val s: T = s_reg_10.deq()
          val delta: Int = delta_reg_10.deq()
          val result: U8 = result_10.deq()

          a_reg_11_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_11.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_11.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_11.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_11.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_11_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_11_0.deq()
        
          val if_swap = if_swap_reg_11_0.deq()
          if_swap_reg_11_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_11_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_11_1.deq()
        
          val if_swap = if_swap_reg_11_1.deq()
          if_swap_reg_11_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_11_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_11_2.deq()
        
          val if_swap = if_swap_reg_11_2.deq()
          //if_swap_reg_11.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_11_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 13

        Pipe {
          val a: U8 = a_reg_11_3.deq()
          val b: T = b_reg_11.deq()
          val s: T = s_reg_11.deq()
          val delta: Int = delta_reg_11.deq()
          val result: U8 = result_11.deq()

          a_reg_12_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_12.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          s_reg_12.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_12.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_12.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_12_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_12_0.deq()
        
          val if_swap = if_swap_reg_12_0.deq()
          if_swap_reg_12_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_12_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_12_1.deq()
        
          val if_swap = if_swap_reg_12_1.deq()
          if_swap_reg_12_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_12_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_12_2.deq()
        
          val if_swap = if_swap_reg_12_2.deq()
          //if_swap_reg_12.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_12_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 14

        Pipe {
          val a: U8 = a_reg_12_3.deq()
          val b: T = b_reg_12.deq()
          val s: T = s_reg_12.deq()
          val delta: Int = delta_reg_12.deq()
          val result: U8 = result_12.deq()

          a_reg_13_0.enq(mux(b.bit(0) != 1, a, a ^ result))
          b_reg_13.enq(mux(b.bit(0) != 1, b >> 1, (b ^ s) >> 1))
          //s_reg_13.enq(mux(b.bit(0) != 1, s, mux(delta < 0, b, s)))
          delta_reg_13.enq(mux(b.bit(0) != 1, delta - 1, mux(delta < 0, - delta - 1, delta - 1)))
          result_13.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
          if_swap_reg_13_0.enq(a.bit(0) == 1)
        }

        // For GF(2^8), we need to do the extra XOR for bits 0, 2 and 3
        Pipe {
          val a: U8 = a_reg_13_0.deq()
        
          val if_swap = if_swap_reg_13_0.deq()
          if_swap_reg_13_1.enq(if_swap)

          // Rotate right
          val new_a_0 = ((a >> 1) | (a << FFSize - 1)).as[U8]

          val bit0 = new_a_0.bit(0).as[U8] ^ 1
          val mask0 = 0x1.to[U8]
          val new_a_1 = ((new_a_0 & ~mask0) | (bit0)).as[U8]

          a_reg_13_1.enq(mux(if_swap, new_a_1, new_a_0))
        }

        Pipe {
          val a: U8 = a_reg_13_1.deq()
        
          val if_swap = if_swap_reg_13_1.deq()
          if_swap_reg_13_2.enq(if_swap)

          val bit2 = (a & mask_a_2) ^ 1.to[U8] << 2
          val mask2 = 0x1.to[U8] << 2
          val new_a_2 = ((a & ~mask2.as[U8]) | bit2.as[U8]).as[U8]
          
          a_reg_13_2.enq(mux(if_swap, new_a_2, a))
        }

        Pipe {
          val a: U8 = a_reg_13_2.deq()
        
          val if_swap = if_swap_reg_13_2.deq()
          //if_swap_reg_13.enq(if_swap)

          val bit3 = (a & mask_a_3) ^ 1.to[U8] << 3
          val mask3 = 0x1.to[U8] << 3
          val new_a_3 = ((a & ~mask3.as[U8]) | bit3.as[U8]).as[U8]

          a_reg_13_3.enq(mux(if_swap, new_a_3, a))
        }

        // PASS 15

        Pipe {
          val a: U8 = a_reg_13_3.deq()
          val b: T = b_reg_13.deq()
          //val s: T = s_reg_13.deq()
          val delta: Int = delta_reg_13.deq()
          val result: U8 = result_13.deq()

          result_14.enq(mux(b.bit(0) != 1, result, mux(delta < 0, a, result)))
        }

        stream_out := Tup2(result_14.deq().to[I32], p == (N-1))  
      }
    }
    assert(1 == 1) // Assert keeps spatial happy
  }
}
