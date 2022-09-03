import spatial.dsl._
import spatial.lib.ML._
import utils.io.files._
import spatial.lang.{FileBus,FileEOFBus}
import spatial.metadata.bounds._

@spatial class FFMult_It_Opt extends SpatialTest {

	val N = 1024
	type T = U8

	def main(args: Array[String]): Unit = {

        val infile_X = buildPath(IR.config.genDir,"tungsten", "in_X.csv")
		
		val outfile = buildPath(IR.config.genDir,"tungsten", "out.csv")
		createDirectories(dirName(infile_a))
		// createDirectories(dirName(infile_b))
		val inputs = List.tabulate(N) {i => i % 256}
		
        writeCSVNow(inputs, infile_X)

		val stream_X_in  = StreamIn[T](FileBus[T](infile_X))

		val stream_out  = StreamOut[Tup2[I32,Bit]](FileEOFBus[Tup2[I32,Bit]](outfile))


        Accel {

            // MULT X FIFOS

            val irred = 0x11b.to[T] // Irreducible polynomial of GF(2^8) 
            val mask_a = 0x1.to[T] << 7

			Foreach(*) { p=>
            
                Pipe {

                    // PASS 0
                    val X = stream_X_in.value
                    
                    val result_X = 0

				    result_X_stageY.enq(mux(Z.bit(0) == 1, result_X ^ W, result_X))

                    val flag_X = (X & mask_a) == mask_a


                    X_stageY.enq(mux(flag_X, (X << 1) ^ irred, X << 1))
                    X_stageY.enq(X >> 1)
                    

                }

                Pipe {

                    // PASS 1
                    val X = X_stageY.deq()
                    
                    val result_X = result_X_stageY.deq()

				    result_X_stageY.enq(mux(Z.bit(0) == 1, result_X ^ W, result_X))

                    val flag_X = (X & mask_a) == mask_a


                    X_stageY.enq(mux(flag_X, (X << 1) ^ irred, X << 1))
                    X_stageY.enq(X >> 1)

                }
                
                Pipe {

                    // PASS 2
                    val X = X_stageY.deq()
                    
                    val result_X = result_X_stageY.deq()

				    result_X_stageY.enq(mux(Z.bit(0) == 1, result_X ^ W, result_X))

                    val flag_X = (X & mask_a) == mask_a


                    X_stageY.enq(mux(flag_X, (X << 1) ^ irred, X << 1))
                    X_stageY.enq(X >> 1)

                }

                Pipe {

                    // PASS 3
                    val X = X_stageY.deq()
                    
                    val result_X = result_X_stageY.deq()

				    result_X_stageY.enq(mux(Z.bit(0) == 1, result_X ^ W, result_X))

                    val flag_X = (X & mask_a) == mask_a


                    X_stageY.enq(mux(flag_X, (X << 1) ^ irred, X << 1))
                    X_stageY.enq(X >> 1)

                }

                Pipe {

                    // PASS 4
                    val X = X_stageY.deq()
                    
                    val result_X = result_X_stageY.deq()

				    result_X_stageY.enq(mux(Z.bit(0) == 1, result_X ^ W, result_X))

                    val flag_X = (X & mask_a) == mask_a


                    X_stageY.enq(mux(flag_X, (X << 1) ^ irred, X << 1))
                    X_stageY.enq(X >> 1)

                }

                Pipe {

                    // PASS 5
                    val X = X_stageY.deq()
                    
                    val result_X = result_X_stageY.deq()

                    result_X_stageY.enq(mux(Z.bit(0) == 1, result_X ^ W, result_X))

                    val flag_X = (X & mask_a) == mask_a


                    X_stageY.enq(mux(flag_X, (X << 1) ^ irred, X << 1))
                    X_stageY.enq(X >> 1)

                }

                Pipe {
                    
                    // PASS 6
                    val X = X_stageY.deq()
                    
                    val result_X = result_X_stageY.deq()

				    result_X_stageY.enq(mux(Z.bit(0) == 1, result_X ^ W, result_X))

                    val flag_X = (X & mask_a) == mask_a


                    X_stageY.enq(mux(flag_X, (X << 1) ^ irred, X << 1))
                    X_stageY.enq(X >> 1)

                }

                Pipe {

                    // PASS 7
                    val X = X_stageY.deq()
                    
                    val result_X = result_X_stageY.deq()

				    result_X_stageY.enq(mux(Z.bit(0) == 1, result_X ^ W, result_X))
                }

                // STREAM OUT
            }
		}
		assert(true)
	}
}
