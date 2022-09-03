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
        val infile_e = buildPath(IR.config.genDir,"tungsten", "in_e.csv")
        val infile_f = buildPath(IR.config.genDir,"tungsten", "in_f.csv")
        val infile_g = buildPath(IR.config.genDir,"tungsten", "in_g.csv")
        val infile_h = buildPath(IR.config.genDir,"tungsten", "in_h.csv")
        val infile_i = buildPath(IR.config.genDir,"tungsten", "in_i.csv")
        val infile_j = buildPath(IR.config.genDir,"tungsten", "in_j.csv")
        val infile_k = buildPath(IR.config.genDir,"tungsten", "in_k.csv")
        val infile_l = buildPath(IR.config.genDir,"tungsten", "in_l.csv")
        val infile_m = buildPath(IR.config.genDir,"tungsten", "in_m.csv")
        val infile_n = buildPath(IR.config.genDir,"tungsten", "in_n.csv")
        val infile_o = buildPath(IR.config.genDir,"tungsten", "in_o.csv")
        val infile_p = buildPath(IR.config.genDir,"tungsten", "in_p.csv")
        val infile_q = buildPath(IR.config.genDir,"tungsten", "in_q.csv")
        val infile_r = buildPath(IR.config.genDir,"tungsten", "in_r.csv")
		
		val outfile = buildPath(IR.config.genDir,"tungsten", "out.csv")
		createDirectories(dirName(infile_a))
		// createDirectories(dirName(infile_b))
		val inputs = List.tabulate(N) {i => i % 256}
		
        writeCSVNow(inputs, infile_a)
        writeCSVNow(inputs, infile_b)
        writeCSVNow(inputs, infile_c)
        writeCSVNow(inputs, infile_d)
        writeCSVNow(inputs, infile_e)
        writeCSVNow(inputs, infile_f)
        writeCSVNow(inputs, infile_g)
        writeCSVNow(inputs, infile_h)
        writeCSVNow(inputs, infile_i)
        writeCSVNow(inputs, infile_j)
        writeCSVNow(inputs, infile_k)
        writeCSVNow(inputs, infile_l)
        writeCSVNow(inputs, infile_m)
        writeCSVNow(inputs, infile_n)
        writeCSVNow(inputs, infile_o)
        writeCSVNow(inputs, infile_p)
        writeCSVNow(inputs, infile_q)
        writeCSVNow(inputs, infile_r)

		val stream_a_in  = StreamIn[T](FileBus[T](infile_a))
		val stream_b_in  = StreamIn[T](FileBus[T](infile_b))
		val stream_c_in  = StreamIn[T](FileBus[T](infile_c))
		val stream_d_in  = StreamIn[T](FileBus[T](infile_d))
		val stream_e_in  = StreamIn[T](FileBus[T](infile_e))
		val stream_f_in  = StreamIn[T](FileBus[T](infile_f))
		val stream_g_in  = StreamIn[T](FileBus[T](infile_g))
		val stream_h_in  = StreamIn[T](FileBus[T](infile_h))
		val stream_i_in  = StreamIn[T](FileBus[T](infile_i))
		val stream_j_in  = StreamIn[T](FileBus[T](infile_j))
		val stream_k_in  = StreamIn[T](FileBus[T](infile_k))
		val stream_l_in  = StreamIn[T](FileBus[T](infile_l))
		val stream_m_in  = StreamIn[T](FileBus[T](infile_m))
		val stream_n_in  = StreamIn[T](FileBus[T](infile_n))
		val stream_o_in  = StreamIn[T](FileBus[T](infile_o))
		val stream_p_in  = StreamIn[T](FileBus[T](infile_p))
		val stream_q_in  = StreamIn[T](FileBus[T](infile_q))
		val stream_r_in  = StreamIn[T](FileBus[T](infile_r))

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

            // MULT 2 FIFOS
			val result_2_stage0 = FIFO[T](4)
			val e_stage0 = FIFO[T](4)
			val f_stage0 = FIFO[T](4)

			val result_2_stage1 = FIFO[T](4)
			val e_stage1 = FIFO[T](4)
			val f_stage1 = FIFO[T](4)

			val result_2_stage2 = FIFO[T](4)
			val e_stage2 = FIFO[T](4)
			val f_stage2 = FIFO[T](4)

			val result_2_stage3 = FIFO[T](4)
			val e_stage3 = FIFO[T](4)
			val f_stage3 = FIFO[T](4)

			val result_2_stage4 = FIFO[T](4)
			val e_stage4 = FIFO[T](4)
			val f_stage4 = FIFO[T](4)

			val result_2_stage5 = FIFO[T](4)
			val e_stage5 = FIFO[T](4)
			val f_stage5 = FIFO[T](4)

			val result_2_stage6 = FIFO[T](4)
			val e_stage6 = FIFO[T](4)
			val f_stage6 = FIFO[T](4)

			val result_2_stage7 = FIFO[T](4)

            // MULT 3 FIFOS
			val result_3_stage0 = FIFO[T](4)
			val g_stage0 = FIFO[T](4)
			val h_stage0 = FIFO[T](4)

			val result_3_stage1 = FIFO[T](4)
			val g_stage1 = FIFO[T](4)
			val h_stage1 = FIFO[T](4)

			val result_3_stage2 = FIFO[T](4)
			val g_stage2 = FIFO[T](4)
			val h_stage2 = FIFO[T](4)

			val result_3_stage3 = FIFO[T](4)
			val g_stage3 = FIFO[T](4)
			val h_stage3 = FIFO[T](4)

			val result_3_stage4 = FIFO[T](4)
			val g_stage4 = FIFO[T](4)
			val h_stage4 = FIFO[T](4)

			val result_3_stage5 = FIFO[T](4)
			val g_stage5 = FIFO[T](4)
			val h_stage5 = FIFO[T](4)

			val result_3_stage6 = FIFO[T](4)
			val g_stage6 = FIFO[T](4)
			val h_stage6 = FIFO[T](4)

			val result_3_stage7 = FIFO[T](4)

            // MULT 4 FIFOS
			val result_4_stage0 = FIFO[T](4)
			val i_stage0 = FIFO[T](4)
			val j_stage0 = FIFO[T](4)

			val result_4_stage1 = FIFO[T](4)
			val i_stage1 = FIFO[T](4)
			val j_stage1 = FIFO[T](4)

			val result_4_stage2 = FIFO[T](4)
			val i_stage2 = FIFO[T](4)
			val j_stage2 = FIFO[T](4)

			val result_4_stage3 = FIFO[T](4)
			val i_stage3 = FIFO[T](4)
			val j_stage3 = FIFO[T](4)

			val result_4_stage4 = FIFO[T](4)
			val i_stage4 = FIFO[T](4)
			val j_stage4 = FIFO[T](4)

			val result_4_stage5 = FIFO[T](4)
			val i_stage5 = FIFO[T](4)
			val j_stage5 = FIFO[T](4)

			val result_4_stage6 = FIFO[T](4)
			val i_stage6 = FIFO[T](4)
			val j_stage6 = FIFO[T](4)

			val result_4_stage7 = FIFO[T](4)

            // MULT 5 FIFOS
			val result_5_stage0 = FIFO[T](4)
			val k_stage0 = FIFO[T](4)
			val l_stage0 = FIFO[T](4)

			val result_5_stage1 = FIFO[T](4)
			val k_stage1 = FIFO[T](4)
			val l_stage1 = FIFO[T](4)

			val result_5_stage2 = FIFO[T](4)
			val k_stage2 = FIFO[T](4)
			val l_stage2 = FIFO[T](4)

			val result_5_stage3 = FIFO[T](4)
			val k_stage3 = FIFO[T](4)
			val l_stage3 = FIFO[T](4)

			val result_5_stage4 = FIFO[T](4)
			val k_stage4 = FIFO[T](4)
			val l_stage4 = FIFO[T](4)

			val result_5_stage5 = FIFO[T](4)
			val k_stage5 = FIFO[T](4)
			val l_stage5 = FIFO[T](4)

			val result_5_stage6 = FIFO[T](4)
			val k_stage6 = FIFO[T](4)
			val l_stage6 = FIFO[T](4)

			val result_5_stage7 = FIFO[T](4)

            // MULT 6 FIFOS
			val result_6_stage0 = FIFO[T](4)
			val m_stage0 = FIFO[T](4)
			val n_stage0 = FIFO[T](4)

			val result_6_stage1 = FIFO[T](4)
			val m_stage1 = FIFO[T](4)
			val n_stage1 = FIFO[T](4)

			val result_6_stage2 = FIFO[T](4)
			val m_stage2 = FIFO[T](4)
			val n_stage2 = FIFO[T](4)

			val result_6_stage3 = FIFO[T](4)
			val m_stage3 = FIFO[T](4)
			val n_stage3 = FIFO[T](4)

			val result_6_stage4 = FIFO[T](4)
			val m_stage4 = FIFO[T](4)
			val n_stage4 = FIFO[T](4)

			val result_6_stage5 = FIFO[T](4)
			val m_stage5 = FIFO[T](4)
			val n_stage5 = FIFO[T](4)

			val result_6_stage6 = FIFO[T](4)
			val m_stage6 = FIFO[T](4)
			val n_stage6 = FIFO[T](4)

			val result_6_stage7 = FIFO[T](4)

            // MULT 7 FIFOS
			val result_7_stage0 = FIFO[T](4)
			val o_stage0 = FIFO[T](4)
			val p_stage0 = FIFO[T](4)

			val result_7_stage1 = FIFO[T](4)
			val o_stage1 = FIFO[T](4)
			val p_stage1 = FIFO[T](4)

			val result_7_stage2 = FIFO[T](4)
			val o_stage2 = FIFO[T](4)
			val p_stage2 = FIFO[T](4)

			val result_7_stage3 = FIFO[T](4)
			val o_stage3 = FIFO[T](4)
			val p_stage3 = FIFO[T](4)

			val result_7_stage4 = FIFO[T](4)
			val o_stage4 = FIFO[T](4)
			val p_stage4 = FIFO[T](4)

			val result_7_stage5 = FIFO[T](4)
			val o_stage5 = FIFO[T](4)
			val p_stage5 = FIFO[T](4)

			val result_7_stage6 = FIFO[T](4)
			val o_stage6 = FIFO[T](4)
			val p_stage6 = FIFO[T](4)

			val result_7_stage7 = FIFO[T](4)

            // MULT 8 FIFOS
			val result_8_stage0 = FIFO[T](4)
			val q_stage0 = FIFO[T](4)
			val r_stage0 = FIFO[T](4)

			val result_8_stage1 = FIFO[T](4)
			val q_stage1 = FIFO[T](4)
			val r_stage1 = FIFO[T](4)

			val result_8_stage2 = FIFO[T](4)
			val q_stage2 = FIFO[T](4)
			val r_stage2 = FIFO[T](4)

			val result_8_stage3 = FIFO[T](4)
			val q_stage3 = FIFO[T](4)
			val r_stage3 = FIFO[T](4)

			val result_8_stage4 = FIFO[T](4)
			val q_stage4 = FIFO[T](4)
			val r_stage4 = FIFO[T](4)

			val result_8_stage5 = FIFO[T](4)
			val q_stage5 = FIFO[T](4)
			val r_stage5 = FIFO[T](4)

			val result_8_stage6 = FIFO[T](4)
			val q_stage6 = FIFO[T](4)
			val r_stage6 = FIFO[T](4)

			val result_8_stage7 = FIFO[T](4)


            val irred = 0x11b.to[T] // Irreducible polynomial of GF(2^8) 
            val mask_a = 0x1.to[T] << 7

			Foreach(*) { p=>
            
                Pipe {

                    // PASS 0
                    val a = stream_a_in.value
                    val b = stream_b_in.value
                    val c = stream_c_in.value
                    val d = stream_d_in.value
                    val e = stream_e_in.value
                    val f = stream_f_in.value
                    val g = stream_g_in.value
                    val h = stream_h_in.value
                    val i = stream_i_in.value
                    val j = stream_j_in.value
                    val k = stream_k_in.value
                    val l = stream_l_in.value
                    val m = stream_m_in.value
                    val n = stream_n_in.value
                    val o = stream_o_in.value
                    val p = stream_p_in.value
                    val q = stream_q_in.value
                    val r = stream_r_in.value
                    
                    val result_0 = 0
                    val result_1 = 0
                    val result_2 = 0
                    val result_3 = 0
                    val result_4 = 0
                    val result_5 = 0
                    val result_6 = 0
                    val result_7 = 0
                    val result_8 = 0

				    result_0_stage0.enq(mux(b.bit(0) == 1, result_0 ^ a, result_0))
				    result_1_stage0.enq(mux(d.bit(0) == 1, result_1 ^ c, result_1))
				    result_2_stage0.enq(mux(f.bit(0) == 1, result_2 ^ e, result_2))
				    result_3_stage0.enq(mux(h.bit(0) == 1, result_3 ^ g, result_3))
				    result_4_stage0.enq(mux(j.bit(0) == 1, result_4 ^ i, result_4))
				    result_5_stage0.enq(mux(l.bit(0) == 1, result_5 ^ k, result_5))
				    result_6_stage0.enq(mux(n.bit(0) == 1, result_6 ^ m, result_6))
				    result_7_stage0.enq(mux(p.bit(0) == 1, result_7 ^ o, result_7))
				    result_8_stage0.enq(mux(r.bit(0) == 1, result_8 ^ q, result_8))

                    val flag_a = (a & mask_a) == mask_a
                    val flag_c = (c & mask_a) == mask_a
                    val flag_e = (e & mask_a) == mask_a
                    val flag_g = (g & mask_a) == mask_a
                    val flag_i = (i & mask_a) == mask_a
                    val flag_k = (k & mask_a) == mask_a
                    val flag_m = (m & mask_a) == mask_a
                    val flag_o = (o & mask_a) == mask_a
                    val flag_q = (q & mask_a) == mask_a


                    a_stage0.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
                    c_stage0.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
                    e_stage0.enq(mux(flag_e, (e << 1) ^ irred, e << 1))
                    g_stage0.enq(mux(flag_g, (g << 1) ^ irred, g << 1))
                    i_stage0.enq(mux(flag_i, (i << 1) ^ irred, i << 1))
                    k_stage0.enq(mux(flag_k, (k << 1) ^ irred, k << 1))
                    m_stage0.enq(mux(flag_m, (m << 1) ^ irred, m << 1))
                    o_stage0.enq(mux(flag_o, (o << 1) ^ irred, o << 1))
                    q_stage0.enq(mux(flag_q, (q << 1) ^ irred, q << 1))
                    b_stage0.enq(b >> 1)
                    d_stage0.enq(d >> 1)
                    f_stage0.enq(f >> 1)
                    h_stage0.enq(h >> 1)
                    j_stage0.enq(j >> 1)
                    l_stage0.enq(l >> 1)
                    n_stage0.enq(n >> 1)
                    p_stage0.enq(p >> 1)
                    r_stage0.enq(r >> 1)
                    

                }

                Pipe {

                    // PASS 1
                    val a = a_stage0.deq()
                    val b = b_stage0.deq()
                    val c = c_stage0.deq()
                    val d = d_stage0.deq()
                    val e = e_stage0.deq()
                    val f = f_stage0.deq()
                    val g = g_stage0.deq()
                    val h = h_stage0.deq()
                    val i = i_stage0.deq()
                    val j = j_stage0.deq()
                    val k = k_stage0.deq()
                    val l = l_stage0.deq()
                    val m = m_stage0.deq()
                    val n = n_stage0.deq()
                    val o = o_stage0.deq()
                    val p = p_stage0.deq()
                    val q = q_stage0.deq()
                    val r = r_stage0.deq()
                    
                    val result_0 = result_0_stage0.deq()
                    val result_1 = result_1_stage0.deq()
                    val result_2 = result_2_stage0.deq()
                    val result_3 = result_3_stage0.deq()
                    val result_4 = result_4_stage0.deq()
                    val result_5 = result_5_stage0.deq()
                    val result_6 = result_6_stage0.deq()
                    val result_7 = result_7_stage0.deq()
                    val result_8 = result_8_stage0.deq()

				    result_0_stage1.enq(mux(b.bit(0) == 1, result_0 ^ a, result_0))
				    result_1_stage1.enq(mux(d.bit(0) == 1, result_1 ^ c, result_1))
				    result_2_stage1.enq(mux(f.bit(0) == 1, result_2 ^ e, result_2))
				    result_3_stage1.enq(mux(h.bit(0) == 1, result_3 ^ g, result_3))
				    result_4_stage1.enq(mux(j.bit(0) == 1, result_4 ^ i, result_4))
				    result_5_stage1.enq(mux(l.bit(0) == 1, result_5 ^ k, result_5))
				    result_6_stage1.enq(mux(n.bit(0) == 1, result_6 ^ m, result_6))
				    result_7_stage1.enq(mux(p.bit(0) == 1, result_7 ^ o, result_7))
				    result_8_stage1.enq(mux(r.bit(0) == 1, result_8 ^ q, result_8))

                    val flag_a = (a & mask_a) == mask_a
                    val flag_c = (c & mask_a) == mask_a
                    val flag_e = (e & mask_a) == mask_a
                    val flag_g = (g & mask_a) == mask_a
                    val flag_i = (i & mask_a) == mask_a
                    val flag_k = (k & mask_a) == mask_a
                    val flag_m = (m & mask_a) == mask_a
                    val flag_o = (o & mask_a) == mask_a
                    val flag_q = (q & mask_a) == mask_a


                    a_stage1.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
                    c_stage1.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
                    e_stage1.enq(mux(flag_e, (e << 1) ^ irred, e << 1))
                    g_stage1.enq(mux(flag_g, (g << 1) ^ irred, g << 1))
                    i_stage1.enq(mux(flag_i, (i << 1) ^ irred, i << 1))
                    k_stage1.enq(mux(flag_k, (k << 1) ^ irred, k << 1))
                    m_stage1.enq(mux(flag_m, (m << 1) ^ irred, m << 1))
                    o_stage1.enq(mux(flag_o, (o << 1) ^ irred, o << 1))
                    q_stage1.enq(mux(flag_q, (q << 1) ^ irred, q << 1))
                    b_stage1.enq(b >> 1)
                    d_stage1.enq(d >> 1)
                    f_stage1.enq(f >> 1)
                    h_stage1.enq(h >> 1)
                    j_stage1.enq(j >> 1)
                    l_stage1.enq(l >> 1)
                    n_stage1.enq(n >> 1)
                    p_stage1.enq(p >> 1)
                    r_stage1.enq(r >> 1)

                }
                
                Pipe {

                    // PASS 2
                    val a = a_stage1.deq()
                    val b = b_stage1.deq()
                    val c = c_stage1.deq()
                    val d = d_stage1.deq()
                    val e = e_stage1.deq()
                    val f = f_stage1.deq()
                    val g = g_stage1.deq()
                    val h = h_stage1.deq()
                    val i = i_stage1.deq()
                    val j = j_stage1.deq()
                    val k = k_stage1.deq()
                    val l = l_stage1.deq()
                    val m = m_stage1.deq()
                    val n = n_stage1.deq()
                    val o = o_stage1.deq()
                    val p = p_stage1.deq()
                    val q = q_stage1.deq()
                    val r = r_stage1.deq()
                    
                    val result_0 = result_0_stage1.deq()
                    val result_1 = result_1_stage1.deq()
                    val result_2 = result_2_stage1.deq()
                    val result_3 = result_3_stage1.deq()
                    val result_4 = result_4_stage1.deq()
                    val result_5 = result_5_stage1.deq()
                    val result_6 = result_6_stage1.deq()
                    val result_7 = result_7_stage1.deq()
                    val result_8 = result_8_stage1.deq()

				    result_0_stage2.enq(mux(b.bit(0) == 1, result_0 ^ a, result_0))
				    result_1_stage2.enq(mux(d.bit(0) == 1, result_1 ^ c, result_1))
				    result_2_stage2.enq(mux(f.bit(0) == 1, result_2 ^ e, result_2))
				    result_3_stage2.enq(mux(h.bit(0) == 1, result_3 ^ g, result_3))
				    result_4_stage2.enq(mux(j.bit(0) == 1, result_4 ^ i, result_4))
				    result_5_stage2.enq(mux(l.bit(0) == 1, result_5 ^ k, result_5))
				    result_6_stage2.enq(mux(n.bit(0) == 1, result_6 ^ m, result_6))
				    result_7_stage2.enq(mux(p.bit(0) == 1, result_7 ^ o, result_7))
				    result_8_stage2.enq(mux(r.bit(0) == 1, result_8 ^ q, result_8))

                    val flag_a = (a & mask_a) == mask_a
                    val flag_c = (c & mask_a) == mask_a
                    val flag_e = (e & mask_a) == mask_a
                    val flag_g = (g & mask_a) == mask_a
                    val flag_i = (i & mask_a) == mask_a
                    val flag_k = (k & mask_a) == mask_a
                    val flag_m = (m & mask_a) == mask_a
                    val flag_o = (o & mask_a) == mask_a
                    val flag_q = (q & mask_a) == mask_a


                    a_stage2.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
                    c_stage2.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
                    e_stage2.enq(mux(flag_e, (e << 1) ^ irred, e << 1))
                    g_stage2.enq(mux(flag_g, (g << 1) ^ irred, g << 1))
                    i_stage2.enq(mux(flag_i, (i << 1) ^ irred, i << 1))
                    k_stage2.enq(mux(flag_k, (k << 1) ^ irred, k << 1))
                    m_stage2.enq(mux(flag_m, (m << 1) ^ irred, m << 1))
                    o_stage2.enq(mux(flag_o, (o << 1) ^ irred, o << 1))
                    q_stage2.enq(mux(flag_q, (q << 1) ^ irred, q << 1))
                    b_stage2.enq(b >> 1)
                    d_stage2.enq(d >> 1)
                    f_stage2.enq(f >> 1)
                    h_stage2.enq(h >> 1)
                    j_stage2.enq(j >> 1)
                    l_stage2.enq(l >> 1)
                    n_stage2.enq(n >> 1)
                    p_stage2.enq(p >> 1)
                    r_stage2.enq(r >> 1)

                }

                Pipe {

                    // PASS 3
                    val a = a_stage2.deq()
                    val b = b_stage2.deq()
                    val c = c_stage2.deq()
                    val d = d_stage2.deq()
                    val e = e_stage2.deq()
                    val f = f_stage2.deq()
                    val g = g_stage2.deq()
                    val h = h_stage2.deq()
                    val i = i_stage2.deq()
                    val j = j_stage2.deq()
                    val k = k_stage2.deq()
                    val l = l_stage2.deq()
                    val m = m_stage2.deq()
                    val n = n_stage2.deq()
                    val o = o_stage2.deq()
                    val p = p_stage2.deq()
                    val q = q_stage2.deq()
                    val r = r_stage2.deq()
                    
                    val result_0 = result_0_stage2.deq()
                    val result_1 = result_1_stage2.deq()
                    val result_2 = result_2_stage2.deq()
                    val result_3 = result_3_stage2.deq()
                    val result_4 = result_4_stage2.deq()
                    val result_5 = result_5_stage2.deq()
                    val result_6 = result_6_stage2.deq()
                    val result_7 = result_7_stage2.deq()
                    val result_8 = result_8_stage2.deq()

				    result_0_stage3.enq(mux(b.bit(0) == 1, result_0 ^ a, result_0))
				    result_1_stage3.enq(mux(d.bit(0) == 1, result_1 ^ c, result_1))
				    result_2_stage3.enq(mux(f.bit(0) == 1, result_2 ^ e, result_2))
				    result_3_stage3.enq(mux(h.bit(0) == 1, result_3 ^ g, result_3))
				    result_4_stage3.enq(mux(j.bit(0) == 1, result_4 ^ i, result_4))
				    result_5_stage3.enq(mux(l.bit(0) == 1, result_5 ^ k, result_5))
				    result_6_stage3.enq(mux(n.bit(0) == 1, result_6 ^ m, result_6))
				    result_7_stage3.enq(mux(p.bit(0) == 1, result_7 ^ o, result_7))
				    result_8_stage3.enq(mux(r.bit(0) == 1, result_8 ^ q, result_8))

                    val flag_a = (a & mask_a) == mask_a
                    val flag_c = (c & mask_a) == mask_a
                    val flag_e = (e & mask_a) == mask_a
                    val flag_g = (g & mask_a) == mask_a
                    val flag_i = (i & mask_a) == mask_a
                    val flag_k = (k & mask_a) == mask_a
                    val flag_m = (m & mask_a) == mask_a
                    val flag_o = (o & mask_a) == mask_a
                    val flag_q = (q & mask_a) == mask_a


                    a_stage3.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
                    c_stage3.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
                    e_stage3.enq(mux(flag_e, (e << 1) ^ irred, e << 1))
                    g_stage3.enq(mux(flag_g, (g << 1) ^ irred, g << 1))
                    i_stage3.enq(mux(flag_i, (i << 1) ^ irred, i << 1))
                    k_stage3.enq(mux(flag_k, (k << 1) ^ irred, k << 1))
                    m_stage3.enq(mux(flag_m, (m << 1) ^ irred, m << 1))
                    o_stage3.enq(mux(flag_o, (o << 1) ^ irred, o << 1))
                    q_stage3.enq(mux(flag_q, (q << 1) ^ irred, q << 1))
                    b_stage3.enq(b >> 1)
                    d_stage3.enq(d >> 1)
                    f_stage3.enq(f >> 1)
                    h_stage3.enq(h >> 1)
                    j_stage3.enq(j >> 1)
                    l_stage3.enq(l >> 1)
                    n_stage3.enq(n >> 1)
                    p_stage3.enq(p >> 1)
                    r_stage3.enq(r >> 1)

                }

                Pipe {

                    // PASS 4
                    val a = a_stage3.deq()
                    val b = b_stage3.deq()
                    val c = c_stage3.deq()
                    val d = d_stage3.deq()
                    val e = e_stage3.deq()
                    val f = f_stage3.deq()
                    val g = g_stage3.deq()
                    val h = h_stage3.deq()
                    val i = i_stage3.deq()
                    val j = j_stage3.deq()
                    val k = k_stage3.deq()
                    val l = l_stage3.deq()
                    val m = m_stage3.deq()
                    val n = n_stage3.deq()
                    val o = o_stage3.deq()
                    val p = p_stage3.deq()
                    val q = q_stage3.deq()
                    val r = r_stage3.deq()
                    
                    val result_0 = result_0_stage3.deq()
                    val result_1 = result_1_stage3.deq()
                    val result_2 = result_2_stage3.deq()
                    val result_3 = result_3_stage3.deq()
                    val result_4 = result_4_stage3.deq()
                    val result_5 = result_5_stage3.deq()
                    val result_6 = result_6_stage3.deq()
                    val result_7 = result_7_stage3.deq()
                    val result_8 = result_8_stage3.deq()

				    result_0_stage4.enq(mux(b.bit(0) == 1, result_0 ^ a, result_0))
				    result_1_stage4.enq(mux(d.bit(0) == 1, result_1 ^ c, result_1))
				    result_2_stage4.enq(mux(f.bit(0) == 1, result_2 ^ e, result_2))
				    result_3_stage4.enq(mux(h.bit(0) == 1, result_3 ^ g, result_3))
				    result_4_stage4.enq(mux(j.bit(0) == 1, result_4 ^ i, result_4))
				    result_5_stage4.enq(mux(l.bit(0) == 1, result_5 ^ k, result_5))
				    result_6_stage4.enq(mux(n.bit(0) == 1, result_6 ^ m, result_6))
				    result_7_stage4.enq(mux(p.bit(0) == 1, result_7 ^ o, result_7))
				    result_8_stage4.enq(mux(r.bit(0) == 1, result_8 ^ q, result_8))

                    val flag_a = (a & mask_a) == mask_a
                    val flag_c = (c & mask_a) == mask_a
                    val flag_e = (e & mask_a) == mask_a
                    val flag_g = (g & mask_a) == mask_a
                    val flag_i = (i & mask_a) == mask_a
                    val flag_k = (k & mask_a) == mask_a
                    val flag_m = (m & mask_a) == mask_a
                    val flag_o = (o & mask_a) == mask_a
                    val flag_q = (q & mask_a) == mask_a


                    a_stage4.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
                    c_stage4.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
                    e_stage4.enq(mux(flag_e, (e << 1) ^ irred, e << 1))
                    g_stage4.enq(mux(flag_g, (g << 1) ^ irred, g << 1))
                    i_stage4.enq(mux(flag_i, (i << 1) ^ irred, i << 1))
                    k_stage4.enq(mux(flag_k, (k << 1) ^ irred, k << 1))
                    m_stage4.enq(mux(flag_m, (m << 1) ^ irred, m << 1))
                    o_stage4.enq(mux(flag_o, (o << 1) ^ irred, o << 1))
                    q_stage4.enq(mux(flag_q, (q << 1) ^ irred, q << 1))
                    b_stage4.enq(b >> 1)
                    d_stage4.enq(d >> 1)
                    f_stage4.enq(f >> 1)
                    h_stage4.enq(h >> 1)
                    j_stage4.enq(j >> 1)
                    l_stage4.enq(l >> 1)
                    n_stage4.enq(n >> 1)
                    p_stage4.enq(p >> 1)
                    r_stage4.enq(r >> 1)

                }

                Pipe {

                    // PASS 5
                    val a = a_stage4.deq()
                    val b = b_stage4.deq()
                    val c = c_stage4.deq()
                    val d = d_stage4.deq()
                    val e = e_stage4.deq()
                    val f = f_stage4.deq()
                    val g = g_stage4.deq()
                    val h = h_stage4.deq()
                    val i = i_stage4.deq()
                    val j = j_stage4.deq()
                    val k = k_stage4.deq()
                    val l = l_stage4.deq()
                    val m = m_stage4.deq()
                    val n = n_stage4.deq()
                    val o = o_stage4.deq()
                    val p = p_stage4.deq()
                    val q = q_stage4.deq()
                    val r = r_stage4.deq()
                    
                    val result_0 = result_0_stage4.deq()
                    val result_1 = result_1_stage4.deq()
                    val result_2 = result_2_stage4.deq()
                    val result_3 = result_3_stage4.deq()
                    val result_4 = result_4_stage4.deq()
                    val result_5 = result_5_stage4.deq()
                    val result_6 = result_6_stage4.deq()
                    val result_7 = result_7_stage4.deq()
                    val result_8 = result_8_stage4.deq()

                    result_0_stage5.enq(mux(b.bit(0) == 1, result_0 ^ a, result_0))
                    result_1_stage5.enq(mux(d.bit(0) == 1, result_1 ^ c, result_1))
                    result_2_stage5.enq(mux(f.bit(0) == 1, result_2 ^ e, result_2))
                    result_3_stage5.enq(mux(h.bit(0) == 1, result_3 ^ g, result_3))
                    result_4_stage5.enq(mux(j.bit(0) == 1, result_4 ^ i, result_4))
                    result_5_stage5.enq(mux(l.bit(0) == 1, result_5 ^ k, result_5))
                    result_6_stage5.enq(mux(n.bit(0) == 1, result_6 ^ m, result_6))
                    result_7_stage5.enq(mux(p.bit(0) == 1, result_7 ^ o, result_7))
                    result_8_stage5.enq(mux(r.bit(0) == 1, result_8 ^ q, result_8))

                    val flag_a = (a & mask_a) == mask_a
                    val flag_c = (c & mask_a) == mask_a
                    val flag_e = (e & mask_a) == mask_a
                    val flag_g = (g & mask_a) == mask_a
                    val flag_i = (i & mask_a) == mask_a
                    val flag_k = (k & mask_a) == mask_a
                    val flag_m = (m & mask_a) == mask_a
                    val flag_o = (o & mask_a) == mask_a
                    val flag_q = (q & mask_a) == mask_a


                    a_stage5.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
                    c_stage5.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
                    e_stage5.enq(mux(flag_e, (e << 1) ^ irred, e << 1))
                    g_stage5.enq(mux(flag_g, (g << 1) ^ irred, g << 1))
                    i_stage5.enq(mux(flag_i, (i << 1) ^ irred, i << 1))
                    k_stage5.enq(mux(flag_k, (k << 1) ^ irred, k << 1))
                    m_stage5.enq(mux(flag_m, (m << 1) ^ irred, m << 1))
                    o_stage5.enq(mux(flag_o, (o << 1) ^ irred, o << 1))
                    q_stage5.enq(mux(flag_q, (q << 1) ^ irred, q << 1))
                    b_stage5.enq(b >> 1)
                    d_stage5.enq(d >> 1)
                    f_stage5.enq(f >> 1)
                    h_stage5.enq(h >> 1)
                    j_stage5.enq(j >> 1)
                    l_stage5.enq(l >> 1)
                    n_stage5.enq(n >> 1)
                    p_stage5.enq(p >> 1)
                    r_stage5.enq(r >> 1)

                }

                Pipe {
                    
                    // PASS 6
                    val a = a_stage5.deq()
                    val b = b_stage5.deq()
                    val c = c_stage5.deq()
                    val d = d_stage5.deq()
                    val e = e_stage5.deq()
                    val f = f_stage5.deq()
                    val g = g_stage5.deq()
                    val h = h_stage5.deq()
                    val i = i_stage5.deq()
                    val j = j_stage5.deq()
                    val k = k_stage5.deq()
                    val l = l_stage5.deq()
                    val m = m_stage5.deq()
                    val n = n_stage5.deq()
                    val o = o_stage5.deq()
                    val p = p_stage5.deq()
                    val q = q_stage5.deq()
                    val r = r_stage5.deq()
                    
                    val result_0 = result_0_stage5.deq()
                    val result_1 = result_1_stage5.deq()
                    val result_2 = result_2_stage5.deq()
                    val result_3 = result_3_stage5.deq()
                    val result_4 = result_4_stage5.deq()
                    val result_5 = result_5_stage5.deq()
                    val result_6 = result_6_stage5.deq()
                    val result_7 = result_7_stage5.deq()
                    val result_8 = result_8_stage5.deq()

				    result_0_stage6.enq(mux(b.bit(0) == 1, result_0 ^ a, result_0))
				    result_1_stage6.enq(mux(d.bit(0) == 1, result_1 ^ c, result_1))
				    result_2_stage6.enq(mux(f.bit(0) == 1, result_2 ^ e, result_2))
				    result_3_stage6.enq(mux(h.bit(0) == 1, result_3 ^ g, result_3))
				    result_4_stage6.enq(mux(j.bit(0) == 1, result_4 ^ i, result_4))
				    result_5_stage6.enq(mux(l.bit(0) == 1, result_5 ^ k, result_5))
				    result_6_stage6.enq(mux(n.bit(0) == 1, result_6 ^ m, result_6))
				    result_7_stage6.enq(mux(p.bit(0) == 1, result_7 ^ o, result_7))
				    result_8_stage6.enq(mux(r.bit(0) == 1, result_8 ^ q, result_8))

                    val flag_a = (a & mask_a) == mask_a
                    val flag_c = (c & mask_a) == mask_a
                    val flag_e = (e & mask_a) == mask_a
                    val flag_g = (g & mask_a) == mask_a
                    val flag_i = (i & mask_a) == mask_a
                    val flag_k = (k & mask_a) == mask_a
                    val flag_m = (m & mask_a) == mask_a
                    val flag_o = (o & mask_a) == mask_a
                    val flag_q = (q & mask_a) == mask_a


                    a_stage6.enq(mux(flag_a, (a << 1) ^ irred, a << 1))
                    c_stage6.enq(mux(flag_c, (c << 1) ^ irred, c << 1))
                    e_stage6.enq(mux(flag_e, (e << 1) ^ irred, e << 1))
                    g_stage6.enq(mux(flag_g, (g << 1) ^ irred, g << 1))
                    i_stage6.enq(mux(flag_i, (i << 1) ^ irred, i << 1))
                    k_stage6.enq(mux(flag_k, (k << 1) ^ irred, k << 1))
                    m_stage6.enq(mux(flag_m, (m << 1) ^ irred, m << 1))
                    o_stage6.enq(mux(flag_o, (o << 1) ^ irred, o << 1))
                    q_stage6.enq(mux(flag_q, (q << 1) ^ irred, q << 1))
                    b_stage6.enq(b >> 1)
                    d_stage6.enq(d >> 1)
                    f_stage6.enq(f >> 1)
                    h_stage6.enq(h >> 1)
                    j_stage6.enq(j >> 1)
                    l_stage6.enq(l >> 1)
                    n_stage6.enq(n >> 1)
                    p_stage6.enq(p >> 1)
                    r_stage6.enq(r >> 1)

                }

                Pipe {

                    // PASS 7
                    val a = a_stage6.deq()
                    val b = b_stage6.deq()
                    val c = c_stage6.deq()
                    val d = d_stage6.deq()
                    val e = e_stage6.deq()
                    val f = f_stage6.deq()
                    val g = g_stage6.deq()
                    val h = h_stage6.deq()
                    val i = i_stage6.deq()
                    val j = j_stage6.deq()
                    val k = k_stage6.deq()
                    val l = l_stage6.deq()
                    val m = m_stage6.deq()
                    val n = n_stage6.deq()
                    val o = o_stage6.deq()
                    val p = p_stage6.deq()
                    val q = q_stage6.deq()
                    val r = r_stage6.deq()
                    
                    val result_0 = result_0_stage6.deq()
                    val result_1 = result_1_stage6.deq()
                    val result_2 = result_2_stage6.deq()
                    val result_3 = result_3_stage6.deq()
                    val result_4 = result_4_stage6.deq()
                    val result_5 = result_5_stage6.deq()
                    val result_6 = result_6_stage6.deq()
                    val result_7 = result_7_stage6.deq()
                    val result_8 = result_8_stage6.deq()

				    result_0_stage7.enq(mux(b.bit(0) == 1, result_0 ^ a, result_0))
				    result_1_stage7.enq(mux(d.bit(0) == 1, result_1 ^ c, result_1))
				    result_2_stage7.enq(mux(f.bit(0) == 1, result_2 ^ e, result_2))
				    result_3_stage7.enq(mux(h.bit(0) == 1, result_3 ^ g, result_3))
				    result_4_stage7.enq(mux(j.bit(0) == 1, result_4 ^ i, result_4))
				    result_5_stage7.enq(mux(l.bit(0) == 1, result_5 ^ k, result_5))
				    result_6_stage7.enq(mux(n.bit(0) == 1, result_6 ^ m, result_6))
				    result_7_stage7.enq(mux(p.bit(0) == 1, result_7 ^ o, result_7))
				    result_8_stage7.enq(mux(r.bit(0) == 1, result_8 ^ q, result_8))
                }

                // STREAM OUT
				stream_out := Tup2((result_0_stage7.deq() + result_1_stage7.deq() + result_2_stage7.deq() + result_3_stage7.deq() + result_4_stage7.deq() + result_5_stage7.deq() + result_6_stage7.deq() + result_7_stage7.deq() + result_8_stage7.deq()).to[I32], p == (N-1))
            }
		}
		assert(true)
	}
}
