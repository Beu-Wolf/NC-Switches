import spatial.dsl._
import spatial.lib.ML._
import utils.io.files._
import spatial.lang.{FileBus,FileEOFBus}
import spatial.metadata.bounds._

@spatial class FFMult_It_Opt extends SpatialTest {

	val N = 1024
	type T = U8

	def main(args: Array[String]): Unit = {

        val infile_a = buildPath(IR.config.genDir,"tungsten", "in_a.csv")
        val infile_b = buildPath(IR.config.genDir,"tungsten", "in_b.csv")
        val infile_c = buildPath(IR.config.genDir,"tungsten", "in_c.csv")
        val infile_d = buildPath(IR.config.genDir,"tungsten", "in_d.csv")
		
		val outfile = buildPath(IR.config.genDir,"tungsten", "out.csv")
		createDirectories(dirName(infile_a))
		// createDirectories(dirName(infile_b))
		val inputs = List.tabulate(N) {i => i % 256}
		
        writeCSVNow(inputs, infile_a)
        writeCSVNow(inputs, infile_b)
        writeCSVNow(inputs, infile_c)
        writeCSVNow(inputs, infile_d)

		val stream_a_in  = StreamIn[T](FileBus[T](infile_a))
		val stream_b_in  = StreamIn[T](FileBus[T](infile_b))
		val stream_c_in  = StreamIn[T](FileBus[T](infile_c))
		val stream_d_in  = StreamIn[T](FileBus[T](infile_d))

		val stream_out  = StreamOut[Tup2[I32,Bit]](FileEOFBus[Tup2[I32,Bit]](outfile))


        Accel {

            // MULT 0 FIFOS
			val result_0_stage0 = FIFO[T](4)
			val a_stage0 = FIFO[T](4)
			val b_stage0 = FIFO[T](4)

			val result_0_stage1 = FIFO[T](4)
			val a_stage1 = FIFO[T](4)
			val b_stage1 = FIFO[T](4)

			val result_0_stage2 = FIFO[T](4)
			val a_stage2 = FIFO[T](4)
			val b_stage2 = FIFO[T](4)

			val result_0_stage3 = FIFO[T](4)
			val a_stage3 = FIFO[T](4)
			val b_stage3 = FIFO[T](4)

			val result_0_stage4 = FIFO[T](4)
			val a_stage4 = FIFO[T](4)
			val b_stage4 = FIFO[T](4)

			val result_0_stage5 = FIFO[T](4)
			val a_stage5 = FIFO[T](4)
			val b_stage5 = FIFO[T](4)

			val result_0_stage6 = FIFO[T](4)
			val a_stage6 = FIFO[T](4)
			val b_stage6 = FIFO[T](4)

			val result_0_stage7 = FIFO[T](4)

            // MULT 1 FIFOS
			val result_1_stage0 = FIFO[T](4)
			val c_stage0 = FIFO[T](4)
			val d_stage0 = FIFO[T](4)

			val result_1_stage1 = FIFO[T](4)
			val c_stage1 = FIFO[T](4)
			val d_stage1 = FIFO[T](4)

			val result_1_stage2 = FIFO[T](4)
			val c_stage2 = FIFO[T](4)
			val d_stage2 = FIFO[T](4)

			val result_1_stage3 = FIFO[T](4)
			val c_stage3 = FIFO[T](4)
			val d_stage3 = FIFO[T](4)

			val result_1_stage4 = FIFO[T](4)
			val c_stage4 = FIFO[T](4)
			val d_stage4 = FIFO[T](4)

			val result_1_stage5 = FIFO[T](4)
			val c_stage5 = FIFO[T](4)
			val d_stage5 = FIFO[T](4)

			val result_1_stage6 = FIFO[T](4)
			val c_stage6 = FIFO[T](4)
			val d_stage6 = FIFO[T](4)

			val result_1_stage7 = FIFO[T](4)


            val irred = 0x11b.to[T] // Irreducible polynomial of GF(2^8) 
            val mask_a = 0x1.to[T] << 7

			Foreach(*) { p=>
            
                // ELEMENT 0
				Pipe {

					// PASS 0
					val a = stream_a_in.value
					val b = stream_b_in.value

					val result = 0

					result_0_stage0.enq(mux(b.bit(0) == 1, result ^ a, result))

					val flag_a = (a & mask_a) == mask_a

					a_stage0.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
					b_stage0.enq(b >> 1)
				}

				Pipe {

					// PASS 1
					val a = a_stage0.deq()
					val b = b_stage0.deq()

					val result = result_0_stage0.deq()

					result_0_stage1.enq(mux(b.bit(0) == 1, result ^ a, result))

					val flag_a = (a & mask_a) == mask_a

					a_stage1.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
					b_stage1.enq(b >> 1)
				}

				Pipe {

					// PASS 2
					val a = a_stage1.deq()
					val b = b_stage1.deq()

					val result = result_0_stage1.deq()

					result_0_stage2.enq(mux(b.bit(0) == 1, result ^ a, result))

					val flag_a = (a & mask_a) == mask_a

					a_stage2.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
					b_stage2.enq(b >> 1)
				}

				Pipe {

					// PASS 3
					val a = a_stage2.deq()
					val b = b_stage2.deq()

					val result = result_0_stage2.deq()

					result_0_stage3.enq(mux(b.bit(0) == 1, result ^ a, result))

					val flag_a = (a & mask_a) == mask_a

					a_stage3.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
					b_stage3.enq(b >> 1)
				}

				Pipe {

					// PASS 4
					val a = a_stage3.deq()
					val b = b_stage3.deq()

					val result = result_0_stage3.deq()

					result_0_stage4.enq(mux(b.bit(0) == 1, result ^ a, result))

					val flag_a = (a & mask_a) == mask_a

					a_stage4.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
					b_stage4.enq(b >> 1)
				}

				Pipe {

					// PASS 5
					val a = a_stage4.deq()
					val b = b_stage4.deq()

					val result = result_0_stage4.deq()

					result_0_stage5.enq(mux(b.bit(0) == 1, result ^ a, result))

					val flag_a = (a & mask_a) == mask_a

					a_stage5.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
					b_stage5.enq(b >> 1)
				}

				Pipe {

					// PASS 6
					val a = a_stage5.deq()
					val b = b_stage5.deq()

					val result = result_0_stage5.deq()

					result_0_stage6.enq(mux(b.bit(0) == 1, result ^ a, result))

					val flag_a = (a & mask_a) == mask_a

					a_stage6.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
					b_stage6.enq(b >> 1)
				}

				Pipe {

					// PASS 7
					val a = a_stage6.deq()
					val b = b_stage6.deq()

					val result = result_0_stage6.deq()

					result_0_stage7.enq(mux(b.bit(0) == 1, result ^ a, result))

				}

                // ELEMENT 1
				Pipe {

					// PASS 0
					val c = stream_c_in.value
					val d = stream_d_in.value

					val result = 0

					result_1_stage0.enq(mux(d.bit(0) == 1, result ^ c, result))

					val flag_c = (c & mask_a) == mask_a

					c_stage0.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
					d_stage0.enq(d >> 1)
				}

				Pipe {

					// PASS 1
					val c = c_stage0.deq()
					val d = d_stage0.deq()

					val result = result_1_stage0.deq()

					result_1_stage1.enq(mux(d.bit(0) == 1, result ^ c, result))

					val flag_c = (c & mask_a) == mask_a

					c_stage1.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
					d_stage1.enq(d >> 1)
				}

				Pipe {

					// PASS 2
					val c = c_stage1.deq()
					val d = d_stage1.deq()

					val result = result_1_stage1.deq()

					result_1_stage2.enq(mux(d.bit(0) == 1, result ^ c, result))

					val flag_c = (c & mask_a) == mask_a

					c_stage2.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
					d_stage2.enq(d >> 1)
				}

				Pipe {

					// PASS 3
					val c = c_stage2.deq()
					val d = d_stage2.deq()

					val result = result_1_stage2.deq()

					result_1_stage3.enq(mux(d.bit(0) == 1, result ^ c, result))

					val flag_c = (c & mask_a) == mask_a

					c_stage3.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
					d_stage3.enq(d >> 1)
				}

				Pipe {

					// PASS 4
					val c = c_stage3.deq()
					val d = d_stage3.deq()

					val result = result_1_stage3.deq()

					result_1_stage4.enq(mux(d.bit(0) == 1, result ^ c, result))

					val flag_c = (c & mask_a) == mask_a

					c_stage4.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
					d_stage4.enq(d >> 1)
				}

				Pipe {

					// PASS 5
					val c = c_stage4.deq()
					val d = d_stage4.deq()

					val result = result_1_stage4.deq()

					result_1_stage5.enq(mux(d.bit(0) == 1, result ^ c, result))

					val flag_c = (c & mask_a) == mask_a

					c_stage5.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
					d_stage5.enq(d >> 1)
				}

				Pipe {

					// PASS 6
					val c = c_stage5.deq()
					val d = d_stage5.deq()

					val result = result_1_stage5.deq()

					result_1_stage6.enq(mux(d.bit(0) == 1, result ^ c, result))

					val flag_c = (c & mask_a) == mask_a

					c_stage6.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
					d_stage6.enq(d >> 1)
				}

				Pipe {

					// PASS 7
					val c = c_stage6.deq()
					val d = d_stage6.deq()

					val result = result_1_stage6.deq()

					result_1_stage7.enq(mux(d.bit(0) == 1, result ^ c, result))

				}


                // STREAM OUT
				stream_out := Tup2((result_0_stage7.deq() + result_1_stage7.deq()).to[I32], p == (N-1))
            }
		}
		assert(true)
	}
}
