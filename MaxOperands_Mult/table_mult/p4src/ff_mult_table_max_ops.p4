/* -*- P4_16 -*- */

#include <core.p4>
#include <tna.p4>

/*************************************************************************
 ************* C O N S T A N T S    A N D   T Y P E S  *******************
**************************************************************************/

#if defined(FF_8)
#define MAX_SIZE 255
#define BIT_MASK 256
#elif defined(FF_16)
#define MAX_SIZE 65535
#define BIT_MASK 65536
#endif

const bit<16> TYPE_CODING = 0x1234;

#if defined(FF_8)
typedef bit<8> field_size_t;
typedef bit<9> sum_size_t;
#elif defined(FF_16)
typedef bit<16> field_size_t;
typedef bit<17> sum_size_t;
#endif

typedef bit<48> macAddr_t;
typedef bit<32> ip4Addr_t;

/*************************************************************************
 ***********************  H E A D E R S  *********************************
 *************************************************************************/

/*  Define all the headers the program will recognize             */
/*  The actual sets of headers processed by each gress can differ */

/* Standard ethernet header */
header ethernet_h {
    macAddr_t   dst_addr;
    macAddr_t   src_addr;
    bit<16>   ether_type;
}

header ff_calc_h {
    field_size_t a;
    field_size_t b;
    field_size_t c;
    field_size_t d;
    field_size_t e;
    field_size_t f;
    field_size_t g;
    field_size_t h;
    field_size_t i;
    field_size_t j;
    field_size_t k;
    field_size_t l;
    field_size_t m;
    field_size_t n;
    field_size_t o;
    field_size_t p;
    field_size_t q;
    field_size_t r;
    field_size_t s;
    field_size_t t;
    field_size_t u;
    field_size_t v;
    field_size_t w;
    field_size_t x;
    field_size_t y;
    field_size_t z;
    field_size_t aa;
    field_size_t ab;
    field_size_t ac;
    field_size_t ad;
    bit<32> result_0;
    bit<32> result_1;
    bit<32> result_2;
    bit<32> result_3;
    bit<32> result_4;
    bit<32> result_5;
    bit<32> result_6;
    bit<32> result_7;
    bit<32> result_8;
    bit<32> result_9;
    bit<32> result_10;
    bit<32> result_11;
    bit<32> result_12;
    bit<32> result_13;
    bit<32> result_14;
}

header port_h {
    bit<16> port_num;
}


/*************************************************************************
 **************  I N G R E S S   P R O C E S S I N G   *******************
 *************************************************************************/
 
    /***********************  H E A D E R S  ************************/

struct my_ingress_headers_t {
    ethernet_h   ethernet;
    ff_calc_h    ff_calc;
    port_h       port;
}

    /******  G L O B A L   I N G R E S S   M E T A D A T A  *********/


struct my_ingress_metadata_t {
    bit<32> tmp_sum_value_0;
    bit<32> tmp_sum_value_1;
    bit<32> tmp_sum_value_2;
    bit<32> tmp_sum_value_3;
    bit<32> tmp_sum_value_4;
    bit<32> tmp_sum_value_5;
    bit<32> tmp_sum_value_6;
    bit<32> tmp_sum_value_7;
    bit<32> tmp_sum_value_8;
    bit<32> tmp_sum_value_9;
    bit<32> tmp_sum_value_10;
    bit<32> tmp_sum_value_11;
    bit<32> tmp_sum_value_12;
    bit<32> tmp_sum_value_13;
    bit<32> tmp_sum_value_14;
}


    /***********************  P A R S E R  **************************/
parser IngressParser(packet_in        pkt,
    /* User */    
    out my_ingress_headers_t          hdr,
    out my_ingress_metadata_t         meta,
    /* Intrinsic */
    out ingress_intrinsic_metadata_t  ig_intr_md)
{
    /* This is a mandatory state, required by Tofino Architecture */
     state start {
        pkt.extract(ig_intr_md);
        pkt.advance(PORT_METADATA_SIZE);
        transition parse_ethernet;
    }

    state parse_ethernet {
        pkt.extract(hdr.ethernet);
        meta.tmp_sum_value_0 = 0;
        meta.tmp_sum_value_1 = 0;
        meta.tmp_sum_value_2 = 0;
        meta.tmp_sum_value_3 = 0;
        meta.tmp_sum_value_4 = 0;
        meta.tmp_sum_value_5 = 0;
        meta.tmp_sum_value_6 = 0;
        meta.tmp_sum_value_7 = 0;
        meta.tmp_sum_value_8 = 0;
        meta.tmp_sum_value_9 = 0;
        meta.tmp_sum_value_10 = 0;
        meta.tmp_sum_value_11 = 0;
        meta.tmp_sum_value_12 = 0;
        meta.tmp_sum_value_13 = 0;
        meta.tmp_sum_value_14 = 0;
        transition select(hdr.ethernet.ether_type) {
            TYPE_CODING: parse_ff_calc;
            default: accept;
        }
    }

    state parse_ff_calc {
        pkt.extract(hdr.ff_calc);
        transition parse_port;
    }

    state parse_port {
        pkt.extract(hdr.port);
        transition accept;
    }
}

    /***************** M A T C H - A C T I O N  *********************/

control Ingress(
    /* User */
    inout my_ingress_headers_t                       hdr,
    inout my_ingress_metadata_t                      meta,
    /* Intrinsic */
    in    ingress_intrinsic_metadata_t               ig_intr_md,
    in    ingress_intrinsic_metadata_from_parser_t   ig_prsr_md,
    inout ingress_intrinsic_metadata_for_deparser_t  ig_dprsr_md,
    inout ingress_intrinsic_metadata_for_tm_t        ig_tm_md)
{
    /* LOG vars here */
    bit<32> log_a = 0;
    bit<32> log_b = 0;
    bit<32> log_c = 0;
    bit<32> log_d = 0;
    bit<32> log_e = 0;
    bit<32> log_f = 0;
    bit<32> log_g = 0;
    bit<32> log_h = 0;
    bit<32> log_i = 0;
    bit<32> log_j = 0;
    bit<32> log_k = 0;
    bit<32> log_l = 0;
    bit<32> log_m = 0;
    bit<32> log_n = 0;
    bit<32> log_o = 0;
    bit<32> log_p = 0;
    bit<32> log_q = 0;
    bit<32> log_r = 0;
    bit<32> log_s = 0;
    bit<32> log_t = 0;
    bit<32> log_u = 0;
    bit<32> log_v = 0;
    bit<32> log_w = 0;
    bit<32> log_x = 0;
    bit<32> log_y = 0;
    bit<32> log_z = 0;
    bit<32> log_aa = 0;
    bit<32> log_ab = 0;
    bit<32> log_ac = 0;
    bit<32> log_ad = 0;


    action send(PortId_t port) {
        ig_tm_md.ucast_egress_port = port;
    }

    action drop() {
        ig_dprsr_md.drop_ctl = 1;
    }

    /* Action log here */
	action get_log_a(bit<32> log_value) {
		log_a = log_value;
	}

	action get_log_b(bit<32> log_value) {
		log_b = log_value;
	}

	action get_log_c(bit<32> log_value) {
		log_c = log_value;
	}

	action get_log_d(bit<32> log_value) {
		log_d = log_value;
	}

	action get_log_e(bit<32> log_value) {
		log_e = log_value;
	}

	action get_log_f(bit<32> log_value) {
		log_f = log_value;
	}

	action get_log_g(bit<32> log_value) {
		log_g = log_value;
	}

	action get_log_h(bit<32> log_value) {
		log_h = log_value;
	}

	action get_log_i(bit<32> log_value) {
		log_i = log_value;
	}

	action get_log_j(bit<32> log_value) {
		log_j = log_value;
	}

	action get_log_k(bit<32> log_value) {
		log_k = log_value;
	}

	action get_log_l(bit<32> log_value) {
		log_l = log_value;
	}

	action get_log_m(bit<32> log_value) {
		log_m = log_value;
	}

	action get_log_n(bit<32> log_value) {
		log_n = log_value;
	}

	action get_log_o(bit<32> log_value) {
		log_o = log_value;
	}

	action get_log_p(bit<32> log_value) {
		log_p = log_value;
	}

	action get_log_q(bit<32> log_value) {
		log_q = log_value;
	}

	action get_log_r(bit<32> log_value) {
		log_r = log_value;
	}

	action get_log_s(bit<32> log_value) {
		log_s = log_value;
	}

	action get_log_t(bit<32> log_value) {
		log_t = log_value;
	}

	action get_log_u(bit<32> log_value) {
		log_u = log_value;
	}

	action get_log_v(bit<32> log_value) {
		log_v = log_value;
	}

	action get_log_w(bit<32> log_value) {
		log_w = log_value;
	}

	action get_log_x(bit<32> log_value) {
		log_x = log_value;
	}

	action get_log_y(bit<32> log_value) {
		log_y = log_value;
	}

	action get_log_z(bit<32> log_value) {
		log_z = log_value;
	}

	action get_log_aa(bit<32> log_value) {
		log_aa = log_value;
	}

	action get_log_ab(bit<32> log_value) {
		log_ab = log_value;
	}

	action get_log_ac(bit<32> log_value) {
		log_ac = log_value;
	}

	action get_log_ad(bit<32> log_value) {
		log_ad = log_value;
	}

    

    /* Action antilog here*/
	action get_antilog_0(bit<32> antilog_value) {
		hdr.ff_calc.result_0 = antilog_value;
	}

	action get_antilog_1(bit<32> antilog_value) {
		hdr.ff_calc.result_1 = antilog_value;
	}

	action get_antilog_2(bit<32> antilog_value) {
		hdr.ff_calc.result_2 = antilog_value;
	}

	action get_antilog_3(bit<32> antilog_value) {
		hdr.ff_calc.result_3 = antilog_value;
	}

	action get_antilog_4(bit<32> antilog_value) {
		hdr.ff_calc.result_4 = antilog_value;
	}

	action get_antilog_5(bit<32> antilog_value) {
		hdr.ff_calc.result_5 = antilog_value;
	}

	action get_antilog_6(bit<32> antilog_value) {
		hdr.ff_calc.result_6 = antilog_value;
	}

	action get_antilog_7(bit<32> antilog_value) {
		hdr.ff_calc.result_7 = antilog_value;
	}

	action get_antilog_8(bit<32> antilog_value) {
		hdr.ff_calc.result_8 = antilog_value;
	}

	action get_antilog_9(bit<32> antilog_value) {
		hdr.ff_calc.result_9 = antilog_value;
	}

	action get_antilog_10(bit<32> antilog_value) {
		hdr.ff_calc.result_10 = antilog_value;
	}

	action get_antilog_11(bit<32> antilog_value) {
		hdr.ff_calc.result_11 = antilog_value;
	}

	action get_antilog_12(bit<32> antilog_value) {
		hdr.ff_calc.result_12 = antilog_value;
	}

	action get_antilog_13(bit<32> antilog_value) {
		hdr.ff_calc.result_13 = antilog_value;
	}

	action get_antilog_14(bit<32> antilog_value) {
		hdr.ff_calc.result_14 = antilog_value;
	}

    

    action sub_max(inout bit<32> meta_var) {
        meta_var = meta_var - MAX_SIZE;
    }

    action sum_vals(inout bit<32> meta_var, bit<32> val1, bit<32> val2) {
        meta_var = val1 + val2;
    }

    table table_forward {
        key = {
            hdr.port.port_num: exact;
        }
        actions = {
            send; drop;
        }
        size = 65536;
    }

    /* Tables log here */
	table table_log_a {
		 key = {
			hdr.ff_calc.a: exact;
		}
		actions = {
			 get_log_a;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_b {
		 key = {
			hdr.ff_calc.b: exact;
		}
		actions = {
			 get_log_b;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_c {
		 key = {
			hdr.ff_calc.c: exact;
		}
		actions = {
			 get_log_c;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_d {
		 key = {
			hdr.ff_calc.d: exact;
		}
		actions = {
			 get_log_d;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_e {
		 key = {
			hdr.ff_calc.e: exact;
		}
		actions = {
			 get_log_e;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_f {
		 key = {
			hdr.ff_calc.f: exact;
		}
		actions = {
			 get_log_f;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_g {
		 key = {
			hdr.ff_calc.g: exact;
		}
		actions = {
			 get_log_g;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_h {
		 key = {
			hdr.ff_calc.h: exact;
		}
		actions = {
			 get_log_h;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_i {
		 key = {
			hdr.ff_calc.i: exact;
		}
		actions = {
			 get_log_i;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_j {
		 key = {
			hdr.ff_calc.j: exact;
		}
		actions = {
			 get_log_j;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_k {
		 key = {
			hdr.ff_calc.k: exact;
		}
		actions = {
			 get_log_k;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_l {
		 key = {
			hdr.ff_calc.l: exact;
		}
		actions = {
			 get_log_l;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_m {
		 key = {
			hdr.ff_calc.m: exact;
		}
		actions = {
			 get_log_m;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_n {
		 key = {
			hdr.ff_calc.n: exact;
		}
		actions = {
			 get_log_n;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_o {
		 key = {
			hdr.ff_calc.o: exact;
		}
		actions = {
			 get_log_o;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_p {
		 key = {
			hdr.ff_calc.p: exact;
		}
		actions = {
			 get_log_p;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_q {
		 key = {
			hdr.ff_calc.q: exact;
		}
		actions = {
			 get_log_q;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_r {
		 key = {
			hdr.ff_calc.r: exact;
		}
		actions = {
			 get_log_r;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_s {
		 key = {
			hdr.ff_calc.s: exact;
		}
		actions = {
			 get_log_s;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_t {
		 key = {
			hdr.ff_calc.t: exact;
		}
		actions = {
			 get_log_t;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_u {
		 key = {
			hdr.ff_calc.u: exact;
		}
		actions = {
			 get_log_u;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_v {
		 key = {
			hdr.ff_calc.v: exact;
		}
		actions = {
			 get_log_v;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_w {
		 key = {
			hdr.ff_calc.w: exact;
		}
		actions = {
			 get_log_w;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_x {
		 key = {
			hdr.ff_calc.x: exact;
		}
		actions = {
			 get_log_x;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_y {
		 key = {
			hdr.ff_calc.y: exact;
		}
		actions = {
			 get_log_y;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_z {
		 key = {
			hdr.ff_calc.z: exact;
		}
		actions = {
			 get_log_z;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_aa {
		 key = {
			hdr.ff_calc.aa: exact;
		}
		actions = {
			 get_log_aa;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_ab {
		 key = {
			hdr.ff_calc.ab: exact;
		}
		actions = {
			 get_log_ab;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_ac {
		 key = {
			hdr.ff_calc.ac: exact;
		}
		actions = {
			 get_log_ac;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_log_ad {
		 key = {
			hdr.ff_calc.ad: exact;
		}
		actions = {
			 get_log_ad;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

    

    /* Tables antilog here*/
	table table_antilog_0 {
		key = {
			meta.tmp_sum_value_0: exact;
		}
		actions = {
			 get_antilog_0;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_1 {
		key = {
			meta.tmp_sum_value_1: exact;
		}
		actions = {
			 get_antilog_1;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_2 {
		key = {
			meta.tmp_sum_value_2: exact;
		}
		actions = {
			 get_antilog_2;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_3 {
		key = {
			meta.tmp_sum_value_3: exact;
		}
		actions = {
			 get_antilog_3;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_4 {
		key = {
			meta.tmp_sum_value_4: exact;
		}
		actions = {
			 get_antilog_4;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_5 {
		key = {
			meta.tmp_sum_value_5: exact;
		}
		actions = {
			 get_antilog_5;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_6 {
		key = {
			meta.tmp_sum_value_6: exact;
		}
		actions = {
			 get_antilog_6;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_7 {
		key = {
			meta.tmp_sum_value_7: exact;
		}
		actions = {
			 get_antilog_7;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_8 {
		key = {
			meta.tmp_sum_value_8: exact;
		}
		actions = {
			 get_antilog_8;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_9 {
		key = {
			meta.tmp_sum_value_9: exact;
		}
		actions = {
			 get_antilog_9;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_10 {
		key = {
			meta.tmp_sum_value_10: exact;
		}
		actions = {
			 get_antilog_10;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_11 {
		key = {
			meta.tmp_sum_value_11: exact;
		}
		actions = {
			 get_antilog_11;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_12 {
		key = {
			meta.tmp_sum_value_12: exact;
		}
		actions = {
			 get_antilog_12;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_13 {
		key = {
			meta.tmp_sum_value_13: exact;
		}
		actions = {
			 get_antilog_13;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

	table table_antilog_14 {
		key = {
			meta.tmp_sum_value_14: exact;
		}
		actions = {
			 get_antilog_14;
		}
		#if defined(FF_8)
		size = 256;
		#elif defined(FF_16)
		size = 65536;
		#endif
	}

    

    apply {

        table_forward.apply();

        table_log_a.apply();
        table_log_b.apply();
        table_log_c.apply();
        table_log_d.apply();
        table_log_e.apply();
        table_log_f.apply();
        table_log_g.apply();
        table_log_h.apply();
        table_log_i.apply();
        table_log_j.apply();
        table_log_k.apply();
        table_log_l.apply();
        table_log_m.apply();
        table_log_n.apply();
        table_log_o.apply();
        table_log_p.apply();
        table_log_q.apply();
        table_log_r.apply();
        table_log_s.apply();
        table_log_t.apply();
        table_log_u.apply();
        table_log_v.apply();
        table_log_w.apply();
        table_log_x.apply();
        table_log_y.apply();
        table_log_z.apply();
        table_log_aa.apply();
        table_log_ab.apply();
        table_log_ac.apply();
        table_log_ad.apply();
        
        
        sum_vals(meta.tmp_sum_value_0, log_a, log_b);
        sum_vals(meta.tmp_sum_value_1, log_c, log_d);
        sum_vals(meta.tmp_sum_value_2, log_e, log_f);
        sum_vals(meta.tmp_sum_value_3, log_g, log_h);
        sum_vals(meta.tmp_sum_value_4, log_i, log_j);
        sum_vals(meta.tmp_sum_value_5, log_k, log_l);
        sum_vals(meta.tmp_sum_value_6, log_m, log_n);
        sum_vals(meta.tmp_sum_value_7, log_o, log_p);
        sum_vals(meta.tmp_sum_value_8, log_q, log_r);
        sum_vals(meta.tmp_sum_value_9, log_s, log_t);
        sum_vals(meta.tmp_sum_value_10, log_u, log_v);
        sum_vals(meta.tmp_sum_value_11, log_w, log_x);
        sum_vals(meta.tmp_sum_value_12, log_y, log_z);
        sum_vals(meta.tmp_sum_value_13, log_aa, log_ab);
        sum_vals(meta.tmp_sum_value_14, log_ac, log_ad);

		if(meta.tmp_sum_value_0 == MAX_SIZE || meta.tmp_sum_value_0 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_0);
		}

		if(meta.tmp_sum_value_1 == MAX_SIZE || meta.tmp_sum_value_1 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_1);
		}

		if(meta.tmp_sum_value_2 == MAX_SIZE || meta.tmp_sum_value_2 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_2);
		}

		if(meta.tmp_sum_value_3 == MAX_SIZE || meta.tmp_sum_value_3 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_3);
		}

		if(meta.tmp_sum_value_4 == MAX_SIZE || meta.tmp_sum_value_4 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_4);
		}

		if(meta.tmp_sum_value_5 == MAX_SIZE || meta.tmp_sum_value_5 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_5);
		}

		if(meta.tmp_sum_value_6 == MAX_SIZE || meta.tmp_sum_value_6 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_6);
		}

		if(meta.tmp_sum_value_7 == MAX_SIZE || meta.tmp_sum_value_7 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_7);
		}

		if(meta.tmp_sum_value_8 == MAX_SIZE || meta.tmp_sum_value_8 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_8);
		}

		if(meta.tmp_sum_value_9 == MAX_SIZE || meta.tmp_sum_value_9 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_9);
		}

		if(meta.tmp_sum_value_10 == MAX_SIZE || meta.tmp_sum_value_10 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_10);
		}

		if(meta.tmp_sum_value_11 == MAX_SIZE || meta.tmp_sum_value_11 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_11);
		}

		if(meta.tmp_sum_value_12 == MAX_SIZE || meta.tmp_sum_value_12 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_12);
		}

		if(meta.tmp_sum_value_13 == MAX_SIZE || meta.tmp_sum_value_13 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_13);
		}

		if(meta.tmp_sum_value_14 == MAX_SIZE || meta.tmp_sum_value_14 & BIT_MASK != 0) {
			sub_max(meta.tmp_sum_value_14);
		}

        
        table_antilog_0.apply();
        table_antilog_1.apply();
        table_antilog_2.apply();
        table_antilog_3.apply();
        table_antilog_4.apply();
        table_antilog_5.apply();
        table_antilog_6.apply();
        table_antilog_7.apply();
        table_antilog_8.apply();
        table_antilog_9.apply();
        table_antilog_10.apply();
        table_antilog_11.apply();
        table_antilog_12.apply();
        table_antilog_13.apply();
        table_antilog_14.apply();

    }
}

    /*********************  D E P A R S E R  ************************/

control IngressDeparser(packet_out pkt,
    /* User */
    inout my_ingress_headers_t                       hdr,
    in    my_ingress_metadata_t                      meta,
    /* Intrinsic */
    in    ingress_intrinsic_metadata_for_deparser_t  ig_dprsr_md)
{
    apply {
        pkt.emit(hdr);
    }
}


/*************************************************************************
 ****************  E G R E S S   P R O C E S S I N G   *******************
 *************************************************************************/

    /***********************  H E A D E R S  ************************/

struct my_egress_headers_t {
}

    /********  G L O B A L   E G R E S S   M E T A D A T A  *********/

struct my_egress_metadata_t {
}

    /***********************  P A R S E R  **************************/

parser EgressParser(packet_in        pkt,
    /* User */
    out my_egress_headers_t          hdr,
    out my_egress_metadata_t         meta,
    /* Intrinsic */
    out egress_intrinsic_metadata_t  eg_intr_md)
{
    /* This is a mandatory state, required by Tofino Architecture */
    state start {
        pkt.extract(eg_intr_md);
        transition accept;
    }
}

    /***************** M A T C H - A C T I O N  *********************/

control Egress(
    /* User */
    inout my_egress_headers_t                          hdr,
    inout my_egress_metadata_t                         meta,
    /* Intrinsic */    
    in    egress_intrinsic_metadata_t                  eg_intr_md,
    in    egress_intrinsic_metadata_from_parser_t      eg_prsr_md,
    inout egress_intrinsic_metadata_for_deparser_t     eg_dprsr_md,
    inout egress_intrinsic_metadata_for_output_port_t  eg_oport_md)
{
    apply {
    }
}

    /*********************  D E P A R S E R  ************************/

control EgressDeparser(packet_out pkt,
    /* User */
    inout my_egress_headers_t                       hdr,
    in    my_egress_metadata_t                      meta,
    /* Intrinsic */
    in    egress_intrinsic_metadata_for_deparser_t  eg_dprsr_md)
{
    apply {
        pkt.emit(hdr);
    }
}


/************ F I N A L   P A C K A G E ******************************/
Pipeline(
    IngressParser(),
    Ingress(),
    IngressDeparser(),
    EgressParser(),
    Egress(),
    EgressDeparser()
) pipe;

Switch(pipe) main;
