/* -*- P4_16 -*- */

#include <core.p4>
#include <tna.p4>

/*************************************************************************
 ************* C O N S T A N T S    A N D   T Y P E S  *******************
**************************************************************************/

#define IRRED_POLY 0x1b
#define HIGH_BIT_MASK 128

const bit<16> TYPE_CODING = 0x1234;
const bit<16> TYPE_RESPONSE= 0x2345;

typedef bit<9> egressSpec_t;
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

header ff_calc_t {
    bit<8> a;
    bit<8> b;
    bit<8> c;
    bit<8> d;
    bit<8> e;
    bit<8> f;
    bit<8> g;
    bit<8> h;
    bit<8> i;
    bit<8> j;
    bit<8> k;
    bit<8> l;
    bit<8> m;
    bit<8> n;
    bit<8> o;
    bit<8> p;
    bit<8> result_0;
    bit<8> result_1;
    bit<8> result_2;
    bit<8> result_3;
    bit<8> result_4;
    bit<8> result_5;
    bit<8> result_6;
    bit<8> result_7;
}

header port_h {
    bit<16> port_num;
}



/*************************************************************************
 **************  I N G R E S S   P R O C E S S I N G   *******************
 *************************************************************************/
 
    /***********************  H E A D E R S  ************************/

struct my_ingress_headers_t {
    ethernet_h  ethernet;
    ff_calc_t   ff_calc;
    port_h      port;
}

    /******  G L O B A L   I N G R E S S   M E T A D A T A  *********/

struct my_ingress_metadata_t {
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

    bit<8> high_bit_f_0;
    bit<8> high_bit_f_1;
    bit<8> high_bit_f_2;
    bit<8> high_bit_f_3;
    bit<8> high_bit_f_4;
    bit<8> high_bit_f_5;
    bit<8> high_bit_f_6;
    bit<8> high_bit_f_7;

    bit<8> low_bit_f_0;
    bit<8> low_bit_f_1;
    bit<8> low_bit_f_2;
    bit<8> low_bit_f_3;
    bit<8> low_bit_f_4;
    bit<8> low_bit_f_5;
    bit<8> low_bit_f_6;
    bit<8> low_bit_f_7;

    action action_ff_mult() {
        
        // ELEMENT 0
		high_bit_f_0 = hdr.ff_calc.a & HIGH_BIT_MASK;

		hdr.ff_calc.a = hdr.ff_calc.a << 1;
		hdr.ff_calc.b = hdr.ff_calc.b >> 1;

        // ELEMENT 1
		high_bit_f_1 = hdr.ff_calc.c & HIGH_BIT_MASK;

		hdr.ff_calc.c = hdr.ff_calc.c << 1;
		hdr.ff_calc.d = hdr.ff_calc.d >> 1;

        // ELEMENT 2
		high_bit_f_2 = hdr.ff_calc.e & HIGH_BIT_MASK;

		hdr.ff_calc.e = hdr.ff_calc.e << 1;
		hdr.ff_calc.f = hdr.ff_calc.f >> 1;

        // ELEMENT 3
		high_bit_f_3 = hdr.ff_calc.g & HIGH_BIT_MASK;

		hdr.ff_calc.g = hdr.ff_calc.g << 1;
		hdr.ff_calc.h = hdr.ff_calc.h >> 1;

        // ELEMENT 4
		high_bit_f_4 = hdr.ff_calc.i & HIGH_BIT_MASK;

		hdr.ff_calc.i = hdr.ff_calc.i << 1;
		hdr.ff_calc.j = hdr.ff_calc.j >> 1;

        // ELEMENT 5
		high_bit_f_5 = hdr.ff_calc.k & HIGH_BIT_MASK;

		hdr.ff_calc.k = hdr.ff_calc.k << 1;
		hdr.ff_calc.l = hdr.ff_calc.l >> 1;

        // ELEMENT 6
		high_bit_f_6 = hdr.ff_calc.m & HIGH_BIT_MASK;

		hdr.ff_calc.m = hdr.ff_calc.m << 1;
		hdr.ff_calc.n = hdr.ff_calc.n >> 1;

        // ELEMENT 7
		high_bit_f_7 = hdr.ff_calc.o & HIGH_BIT_MASK;

		hdr.ff_calc.o = hdr.ff_calc.o << 1;
		hdr.ff_calc.p = hdr.ff_calc.p >> 1;



    }


    action action_forward(PortId_t port) {
        ig_tm_md.ucast_egress_port = port;
        action_ff_mult();
    }

    table table_forward {
        key = {
            hdr.port.port_num: exact;
        }
        actions = {
            action_forward; action_ff_mult();
        }
        size = 65536;
        default_action = action_ff_mult();
    }

    

    apply {

        // PASS 1
        low_bit_f_0 = hdr.ff_calc.b & 0x1;
        low_bit_f_1 = hdr.ff_calc.d & 0x1;
        low_bit_f_2 = hdr.ff_calc.f & 0x1;
        low_bit_f_3 = hdr.ff_calc.h & 0x1;
        low_bit_f_4 = hdr.ff_calc.j & 0x1;
        low_bit_f_5 = hdr.ff_calc.l & 0x1;
        low_bit_f_6 = hdr.ff_calc.n & 0x1;
        low_bit_f_7 = hdr.ff_calc.p & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			hdr.ff_calc.result_0 = hdr.ff_calc.result_0 ^ hdr.ff_calc.a;

		if (low_bit_f_1 != 0)
			hdr.ff_calc.result_1 = hdr.ff_calc.result_1 ^ hdr.ff_calc.c;

		if (low_bit_f_2 != 0)
			hdr.ff_calc.result_2 = hdr.ff_calc.result_2 ^ hdr.ff_calc.e;

		if (low_bit_f_3 != 0)
			hdr.ff_calc.result_3 = hdr.ff_calc.result_3 ^ hdr.ff_calc.g;

		if (low_bit_f_4 != 0)
			hdr.ff_calc.result_4 = hdr.ff_calc.result_4 ^ hdr.ff_calc.i;

		if (low_bit_f_5 != 0)
			hdr.ff_calc.result_5 = hdr.ff_calc.result_5 ^ hdr.ff_calc.k;

		if (low_bit_f_6 != 0)
			hdr.ff_calc.result_6 = hdr.ff_calc.result_6 ^ hdr.ff_calc.m;

		if (low_bit_f_7 != 0)
			hdr.ff_calc.result_7 = hdr.ff_calc.result_7 ^ hdr.ff_calc.o;



        table_forward.apply();

        // If high bit here
		if (high_bit_f_0 != 0)
			hdr.ff_calc.a = hdr.ff_calc.a ^ IRRED_POLY;

		if (high_bit_f_1 != 0)
			hdr.ff_calc.c = hdr.ff_calc.c ^ IRRED_POLY;

		if (high_bit_f_2 != 0)
			hdr.ff_calc.e = hdr.ff_calc.e ^ IRRED_POLY;

		if (high_bit_f_3 != 0)
			hdr.ff_calc.g = hdr.ff_calc.g ^ IRRED_POLY;

		if (high_bit_f_4 != 0)
			hdr.ff_calc.i = hdr.ff_calc.i ^ IRRED_POLY;

		if (high_bit_f_5 != 0)
			hdr.ff_calc.k = hdr.ff_calc.k ^ IRRED_POLY;

		if (high_bit_f_6 != 0)
			hdr.ff_calc.m = hdr.ff_calc.m ^ IRRED_POLY;

		if (high_bit_f_7 != 0)
			hdr.ff_calc.o = hdr.ff_calc.o ^ IRRED_POLY;


        // PASS 2
        low_bit_f_0 = hdr.ff_calc.b & 0x1;
        low_bit_f_1 = hdr.ff_calc.d & 0x1;
        low_bit_f_2 = hdr.ff_calc.f & 0x1;
        low_bit_f_3 = hdr.ff_calc.h & 0x1;
        low_bit_f_4 = hdr.ff_calc.j & 0x1;
        low_bit_f_5 = hdr.ff_calc.l & 0x1;
        low_bit_f_6 = hdr.ff_calc.n & 0x1;
        low_bit_f_7 = hdr.ff_calc.p & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			hdr.ff_calc.result_0 = hdr.ff_calc.result_0 ^ hdr.ff_calc.a;

		if (low_bit_f_1 != 0)
			hdr.ff_calc.result_1 = hdr.ff_calc.result_1 ^ hdr.ff_calc.c;

		if (low_bit_f_2 != 0)
			hdr.ff_calc.result_2 = hdr.ff_calc.result_2 ^ hdr.ff_calc.e;

		if (low_bit_f_3 != 0)
			hdr.ff_calc.result_3 = hdr.ff_calc.result_3 ^ hdr.ff_calc.g;

		if (low_bit_f_4 != 0)
			hdr.ff_calc.result_4 = hdr.ff_calc.result_4 ^ hdr.ff_calc.i;

		if (low_bit_f_5 != 0)
			hdr.ff_calc.result_5 = hdr.ff_calc.result_5 ^ hdr.ff_calc.k;

		if (low_bit_f_6 != 0)
			hdr.ff_calc.result_6 = hdr.ff_calc.result_6 ^ hdr.ff_calc.m;

		if (low_bit_f_7 != 0)
			hdr.ff_calc.result_7 = hdr.ff_calc.result_7 ^ hdr.ff_calc.o;



        action_ff_mult();

        // If high bit here
		if (high_bit_f_0 != 0)
			hdr.ff_calc.a = hdr.ff_calc.a ^ IRRED_POLY;

		if (high_bit_f_1 != 0)
			hdr.ff_calc.c = hdr.ff_calc.c ^ IRRED_POLY;

		if (high_bit_f_2 != 0)
			hdr.ff_calc.e = hdr.ff_calc.e ^ IRRED_POLY;

		if (high_bit_f_3 != 0)
			hdr.ff_calc.g = hdr.ff_calc.g ^ IRRED_POLY;

		if (high_bit_f_4 != 0)
			hdr.ff_calc.i = hdr.ff_calc.i ^ IRRED_POLY;

		if (high_bit_f_5 != 0)
			hdr.ff_calc.k = hdr.ff_calc.k ^ IRRED_POLY;

		if (high_bit_f_6 != 0)
			hdr.ff_calc.m = hdr.ff_calc.m ^ IRRED_POLY;

		if (high_bit_f_7 != 0)
			hdr.ff_calc.o = hdr.ff_calc.o ^ IRRED_POLY;

        

        // PASS 3
        low_bit_f_0 = hdr.ff_calc.b & 0x1;
        low_bit_f_1 = hdr.ff_calc.d & 0x1;
        low_bit_f_2 = hdr.ff_calc.f & 0x1;
        low_bit_f_3 = hdr.ff_calc.h & 0x1;
        low_bit_f_4 = hdr.ff_calc.j & 0x1;
        low_bit_f_5 = hdr.ff_calc.l & 0x1;
        low_bit_f_6 = hdr.ff_calc.n & 0x1;
        low_bit_f_7 = hdr.ff_calc.p & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			hdr.ff_calc.result_0 = hdr.ff_calc.result_0 ^ hdr.ff_calc.a;

		if (low_bit_f_1 != 0)
			hdr.ff_calc.result_1 = hdr.ff_calc.result_1 ^ hdr.ff_calc.c;

		if (low_bit_f_2 != 0)
			hdr.ff_calc.result_2 = hdr.ff_calc.result_2 ^ hdr.ff_calc.e;

		if (low_bit_f_3 != 0)
			hdr.ff_calc.result_3 = hdr.ff_calc.result_3 ^ hdr.ff_calc.g;

		if (low_bit_f_4 != 0)
			hdr.ff_calc.result_4 = hdr.ff_calc.result_4 ^ hdr.ff_calc.i;

		if (low_bit_f_5 != 0)
			hdr.ff_calc.result_5 = hdr.ff_calc.result_5 ^ hdr.ff_calc.k;

		if (low_bit_f_6 != 0)
			hdr.ff_calc.result_6 = hdr.ff_calc.result_6 ^ hdr.ff_calc.m;

		if (low_bit_f_7 != 0)
			hdr.ff_calc.result_7 = hdr.ff_calc.result_7 ^ hdr.ff_calc.o;



        action_ff_mult();

        // If high bit here
		if (high_bit_f_0 != 0)
			hdr.ff_calc.a = hdr.ff_calc.a ^ IRRED_POLY;

		if (high_bit_f_1 != 0)
			hdr.ff_calc.c = hdr.ff_calc.c ^ IRRED_POLY;

		if (high_bit_f_2 != 0)
			hdr.ff_calc.e = hdr.ff_calc.e ^ IRRED_POLY;

		if (high_bit_f_3 != 0)
			hdr.ff_calc.g = hdr.ff_calc.g ^ IRRED_POLY;

		if (high_bit_f_4 != 0)
			hdr.ff_calc.i = hdr.ff_calc.i ^ IRRED_POLY;

		if (high_bit_f_5 != 0)
			hdr.ff_calc.k = hdr.ff_calc.k ^ IRRED_POLY;

		if (high_bit_f_6 != 0)
			hdr.ff_calc.m = hdr.ff_calc.m ^ IRRED_POLY;

		if (high_bit_f_7 != 0)
			hdr.ff_calc.o = hdr.ff_calc.o ^ IRRED_POLY;


        // PASS 4
        low_bit_f_0 = hdr.ff_calc.b & 0x1;
        low_bit_f_1 = hdr.ff_calc.d & 0x1;
        low_bit_f_2 = hdr.ff_calc.f & 0x1;
        low_bit_f_3 = hdr.ff_calc.h & 0x1;
        low_bit_f_4 = hdr.ff_calc.j & 0x1;
        low_bit_f_5 = hdr.ff_calc.l & 0x1;
        low_bit_f_6 = hdr.ff_calc.n & 0x1;
        low_bit_f_7 = hdr.ff_calc.p & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			hdr.ff_calc.result_0 = hdr.ff_calc.result_0 ^ hdr.ff_calc.a;

		if (low_bit_f_1 != 0)
			hdr.ff_calc.result_1 = hdr.ff_calc.result_1 ^ hdr.ff_calc.c;

		if (low_bit_f_2 != 0)
			hdr.ff_calc.result_2 = hdr.ff_calc.result_2 ^ hdr.ff_calc.e;

		if (low_bit_f_3 != 0)
			hdr.ff_calc.result_3 = hdr.ff_calc.result_3 ^ hdr.ff_calc.g;

		if (low_bit_f_4 != 0)
			hdr.ff_calc.result_4 = hdr.ff_calc.result_4 ^ hdr.ff_calc.i;

		if (low_bit_f_5 != 0)
			hdr.ff_calc.result_5 = hdr.ff_calc.result_5 ^ hdr.ff_calc.k;

		if (low_bit_f_6 != 0)
			hdr.ff_calc.result_6 = hdr.ff_calc.result_6 ^ hdr.ff_calc.m;

		if (low_bit_f_7 != 0)
			hdr.ff_calc.result_7 = hdr.ff_calc.result_7 ^ hdr.ff_calc.o;



        action_ff_mult();

        // If high bit here
		if (high_bit_f_0 != 0)
			hdr.ff_calc.a = hdr.ff_calc.a ^ IRRED_POLY;

		if (high_bit_f_1 != 0)
			hdr.ff_calc.c = hdr.ff_calc.c ^ IRRED_POLY;

		if (high_bit_f_2 != 0)
			hdr.ff_calc.e = hdr.ff_calc.e ^ IRRED_POLY;

		if (high_bit_f_3 != 0)
			hdr.ff_calc.g = hdr.ff_calc.g ^ IRRED_POLY;

		if (high_bit_f_4 != 0)
			hdr.ff_calc.i = hdr.ff_calc.i ^ IRRED_POLY;

		if (high_bit_f_5 != 0)
			hdr.ff_calc.k = hdr.ff_calc.k ^ IRRED_POLY;

		if (high_bit_f_6 != 0)
			hdr.ff_calc.m = hdr.ff_calc.m ^ IRRED_POLY;

		if (high_bit_f_7 != 0)
			hdr.ff_calc.o = hdr.ff_calc.o ^ IRRED_POLY;


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
    ethernet_h  ethernet;
    ff_calc_t   ff_calc;
    port_h      port;
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
        transition parse_ethernet;
    }

    state parse_ethernet {
        pkt.extract(hdr.ethernet);
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

    bit<8> high_bit_f_0;
    bit<8> high_bit_f_1;
    bit<8> high_bit_f_2;
    bit<8> high_bit_f_3;
    bit<8> high_bit_f_4;
    bit<8> high_bit_f_5;
    bit<8> high_bit_f_6;
    bit<8> high_bit_f_7;

    bit<8> low_bit_f_0;
    bit<8> low_bit_f_1;
    bit<8> low_bit_f_2;
    bit<8> low_bit_f_3;
    bit<8> low_bit_f_4;
    bit<8> low_bit_f_5;
    bit<8> low_bit_f_6;
    bit<8> low_bit_f_7;

    action action_ff_mult() {
        
        // ELEMENT 0
		high_bit_f_0 = hdr.ff_calc.a & HIGH_BIT_MASK;

		hdr.ff_calc.a = hdr.ff_calc.a << 1;
		hdr.ff_calc.b = hdr.ff_calc.b >> 1;

        // ELEMENT 1
		high_bit_f_1 = hdr.ff_calc.c & HIGH_BIT_MASK;

		hdr.ff_calc.c = hdr.ff_calc.c << 1;
		hdr.ff_calc.d = hdr.ff_calc.d >> 1;

        // ELEMENT 2
		high_bit_f_2 = hdr.ff_calc.e & HIGH_BIT_MASK;

		hdr.ff_calc.e = hdr.ff_calc.e << 1;
		hdr.ff_calc.f = hdr.ff_calc.f >> 1;

        // ELEMENT 3
		high_bit_f_3 = hdr.ff_calc.g & HIGH_BIT_MASK;

		hdr.ff_calc.g = hdr.ff_calc.g << 1;
		hdr.ff_calc.h = hdr.ff_calc.h >> 1;

        // ELEMENT 4
		high_bit_f_4 = hdr.ff_calc.i & HIGH_BIT_MASK;

		hdr.ff_calc.i = hdr.ff_calc.i << 1;
		hdr.ff_calc.j = hdr.ff_calc.j >> 1;

        // ELEMENT 5
		high_bit_f_5 = hdr.ff_calc.k & HIGH_BIT_MASK;

		hdr.ff_calc.k = hdr.ff_calc.k << 1;
		hdr.ff_calc.l = hdr.ff_calc.l >> 1;

        // ELEMENT 6
		high_bit_f_6 = hdr.ff_calc.m & HIGH_BIT_MASK;

		hdr.ff_calc.m = hdr.ff_calc.m << 1;
		hdr.ff_calc.n = hdr.ff_calc.n >> 1;

        // ELEMENT 7
		high_bit_f_7 = hdr.ff_calc.o & HIGH_BIT_MASK;

		hdr.ff_calc.o = hdr.ff_calc.o << 1;
		hdr.ff_calc.p = hdr.ff_calc.p >> 1;



    }

    action set_ethertype() {
        hdr.ethernet.ether_type = TYPE_RESPONSE;
    }

    action action_ff_mult_end() {
        action_ff_mult();
        set_ethertype();
    }

    apply {
        


        // PASS 5
        low_bit_f_0 = hdr.ff_calc.b & 0x1;
        low_bit_f_1 = hdr.ff_calc.d & 0x1;
        low_bit_f_2 = hdr.ff_calc.f & 0x1;
        low_bit_f_3 = hdr.ff_calc.h & 0x1;
        low_bit_f_4 = hdr.ff_calc.j & 0x1;
        low_bit_f_5 = hdr.ff_calc.l & 0x1;
        low_bit_f_6 = hdr.ff_calc.n & 0x1;
        low_bit_f_7 = hdr.ff_calc.p & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			hdr.ff_calc.result_0 = hdr.ff_calc.result_0 ^ hdr.ff_calc.a;

		if (low_bit_f_1 != 0)
			hdr.ff_calc.result_1 = hdr.ff_calc.result_1 ^ hdr.ff_calc.c;

		if (low_bit_f_2 != 0)
			hdr.ff_calc.result_2 = hdr.ff_calc.result_2 ^ hdr.ff_calc.e;

		if (low_bit_f_3 != 0)
			hdr.ff_calc.result_3 = hdr.ff_calc.result_3 ^ hdr.ff_calc.g;

		if (low_bit_f_4 != 0)
			hdr.ff_calc.result_4 = hdr.ff_calc.result_4 ^ hdr.ff_calc.i;

		if (low_bit_f_5 != 0)
			hdr.ff_calc.result_5 = hdr.ff_calc.result_5 ^ hdr.ff_calc.k;

		if (low_bit_f_6 != 0)
			hdr.ff_calc.result_6 = hdr.ff_calc.result_6 ^ hdr.ff_calc.m;

		if (low_bit_f_7 != 0)
			hdr.ff_calc.result_7 = hdr.ff_calc.result_7 ^ hdr.ff_calc.o;



        action_ff_mult();

        // If high bit here
		if (high_bit_f_0 != 0)
			hdr.ff_calc.a = hdr.ff_calc.a ^ IRRED_POLY;

		if (high_bit_f_1 != 0)
			hdr.ff_calc.c = hdr.ff_calc.c ^ IRRED_POLY;

		if (high_bit_f_2 != 0)
			hdr.ff_calc.e = hdr.ff_calc.e ^ IRRED_POLY;

		if (high_bit_f_3 != 0)
			hdr.ff_calc.g = hdr.ff_calc.g ^ IRRED_POLY;

		if (high_bit_f_4 != 0)
			hdr.ff_calc.i = hdr.ff_calc.i ^ IRRED_POLY;

		if (high_bit_f_5 != 0)
			hdr.ff_calc.k = hdr.ff_calc.k ^ IRRED_POLY;

		if (high_bit_f_6 != 0)
			hdr.ff_calc.m = hdr.ff_calc.m ^ IRRED_POLY;

		if (high_bit_f_7 != 0)
			hdr.ff_calc.o = hdr.ff_calc.o ^ IRRED_POLY;

        

        // PASS 6
        low_bit_f_0 = hdr.ff_calc.b & 0x1;
        low_bit_f_1 = hdr.ff_calc.d & 0x1;
        low_bit_f_2 = hdr.ff_calc.f & 0x1;
        low_bit_f_3 = hdr.ff_calc.h & 0x1;
        low_bit_f_4 = hdr.ff_calc.j & 0x1;
        low_bit_f_5 = hdr.ff_calc.l & 0x1;
        low_bit_f_6 = hdr.ff_calc.n & 0x1;
        low_bit_f_7 = hdr.ff_calc.p & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			hdr.ff_calc.result_0 = hdr.ff_calc.result_0 ^ hdr.ff_calc.a;

		if (low_bit_f_1 != 0)
			hdr.ff_calc.result_1 = hdr.ff_calc.result_1 ^ hdr.ff_calc.c;

		if (low_bit_f_2 != 0)
			hdr.ff_calc.result_2 = hdr.ff_calc.result_2 ^ hdr.ff_calc.e;

		if (low_bit_f_3 != 0)
			hdr.ff_calc.result_3 = hdr.ff_calc.result_3 ^ hdr.ff_calc.g;

		if (low_bit_f_4 != 0)
			hdr.ff_calc.result_4 = hdr.ff_calc.result_4 ^ hdr.ff_calc.i;

		if (low_bit_f_5 != 0)
			hdr.ff_calc.result_5 = hdr.ff_calc.result_5 ^ hdr.ff_calc.k;

		if (low_bit_f_6 != 0)
			hdr.ff_calc.result_6 = hdr.ff_calc.result_6 ^ hdr.ff_calc.m;

		if (low_bit_f_7 != 0)
			hdr.ff_calc.result_7 = hdr.ff_calc.result_7 ^ hdr.ff_calc.o;



        action_ff_mult();

        // If high bit here
		if (high_bit_f_0 != 0)
			hdr.ff_calc.a = hdr.ff_calc.a ^ IRRED_POLY;

		if (high_bit_f_1 != 0)
			hdr.ff_calc.c = hdr.ff_calc.c ^ IRRED_POLY;

		if (high_bit_f_2 != 0)
			hdr.ff_calc.e = hdr.ff_calc.e ^ IRRED_POLY;

		if (high_bit_f_3 != 0)
			hdr.ff_calc.g = hdr.ff_calc.g ^ IRRED_POLY;

		if (high_bit_f_4 != 0)
			hdr.ff_calc.i = hdr.ff_calc.i ^ IRRED_POLY;

		if (high_bit_f_5 != 0)
			hdr.ff_calc.k = hdr.ff_calc.k ^ IRRED_POLY;

		if (high_bit_f_6 != 0)
			hdr.ff_calc.m = hdr.ff_calc.m ^ IRRED_POLY;

		if (high_bit_f_7 != 0)
			hdr.ff_calc.o = hdr.ff_calc.o ^ IRRED_POLY;

    


        // PASS 7
        low_bit_f_0 = hdr.ff_calc.b & 0x1;
        low_bit_f_1 = hdr.ff_calc.d & 0x1;
        low_bit_f_2 = hdr.ff_calc.f & 0x1;
        low_bit_f_3 = hdr.ff_calc.h & 0x1;
        low_bit_f_4 = hdr.ff_calc.j & 0x1;
        low_bit_f_5 = hdr.ff_calc.l & 0x1;
        low_bit_f_6 = hdr.ff_calc.n & 0x1;
        low_bit_f_7 = hdr.ff_calc.p & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			hdr.ff_calc.result_0 = hdr.ff_calc.result_0 ^ hdr.ff_calc.a;

		if (low_bit_f_1 != 0)
			hdr.ff_calc.result_1 = hdr.ff_calc.result_1 ^ hdr.ff_calc.c;

		if (low_bit_f_2 != 0)
			hdr.ff_calc.result_2 = hdr.ff_calc.result_2 ^ hdr.ff_calc.e;

		if (low_bit_f_3 != 0)
			hdr.ff_calc.result_3 = hdr.ff_calc.result_3 ^ hdr.ff_calc.g;

		if (low_bit_f_4 != 0)
			hdr.ff_calc.result_4 = hdr.ff_calc.result_4 ^ hdr.ff_calc.i;

		if (low_bit_f_5 != 0)
			hdr.ff_calc.result_5 = hdr.ff_calc.result_5 ^ hdr.ff_calc.k;

		if (low_bit_f_6 != 0)
			hdr.ff_calc.result_6 = hdr.ff_calc.result_6 ^ hdr.ff_calc.m;

		if (low_bit_f_7 != 0)
			hdr.ff_calc.result_7 = hdr.ff_calc.result_7 ^ hdr.ff_calc.o;



        action_ff_mult();

        // If high bit here
		if (high_bit_f_0 != 0)
			hdr.ff_calc.a = hdr.ff_calc.a ^ IRRED_POLY;

		if (high_bit_f_1 != 0)
			hdr.ff_calc.c = hdr.ff_calc.c ^ IRRED_POLY;

		if (high_bit_f_2 != 0)
			hdr.ff_calc.e = hdr.ff_calc.e ^ IRRED_POLY;

		if (high_bit_f_3 != 0)
			hdr.ff_calc.g = hdr.ff_calc.g ^ IRRED_POLY;

		if (high_bit_f_4 != 0)
			hdr.ff_calc.i = hdr.ff_calc.i ^ IRRED_POLY;

		if (high_bit_f_5 != 0)
			hdr.ff_calc.k = hdr.ff_calc.k ^ IRRED_POLY;

		if (high_bit_f_6 != 0)
			hdr.ff_calc.m = hdr.ff_calc.m ^ IRRED_POLY;

		if (high_bit_f_7 != 0)
			hdr.ff_calc.o = hdr.ff_calc.o ^ IRRED_POLY;

        

        // PASS 8
        low_bit_f_0 = hdr.ff_calc.b & 0x1;
        low_bit_f_1 = hdr.ff_calc.d & 0x1;
        low_bit_f_2 = hdr.ff_calc.f & 0x1;
        low_bit_f_3 = hdr.ff_calc.h & 0x1;
        low_bit_f_4 = hdr.ff_calc.j & 0x1;
        low_bit_f_5 = hdr.ff_calc.l & 0x1;
        low_bit_f_6 = hdr.ff_calc.n & 0x1;
        low_bit_f_7 = hdr.ff_calc.p & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			hdr.ff_calc.result_0 = hdr.ff_calc.result_0 ^ hdr.ff_calc.a;

		if (low_bit_f_1 != 0)
			hdr.ff_calc.result_1 = hdr.ff_calc.result_1 ^ hdr.ff_calc.c;

		if (low_bit_f_2 != 0)
			hdr.ff_calc.result_2 = hdr.ff_calc.result_2 ^ hdr.ff_calc.e;

		if (low_bit_f_3 != 0)
			hdr.ff_calc.result_3 = hdr.ff_calc.result_3 ^ hdr.ff_calc.g;

		if (low_bit_f_4 != 0)
			hdr.ff_calc.result_4 = hdr.ff_calc.result_4 ^ hdr.ff_calc.i;

		if (low_bit_f_5 != 0)
			hdr.ff_calc.result_5 = hdr.ff_calc.result_5 ^ hdr.ff_calc.k;

		if (low_bit_f_6 != 0)
			hdr.ff_calc.result_6 = hdr.ff_calc.result_6 ^ hdr.ff_calc.m;

		if (low_bit_f_7 != 0)
			hdr.ff_calc.result_7 = hdr.ff_calc.result_7 ^ hdr.ff_calc.o;



        action_ff_mult_end();

        // If high bit here
		if (high_bit_f_0 != 0)
			hdr.ff_calc.a = hdr.ff_calc.a ^ IRRED_POLY;

		if (high_bit_f_1 != 0)
			hdr.ff_calc.c = hdr.ff_calc.c ^ IRRED_POLY;

		if (high_bit_f_2 != 0)
			hdr.ff_calc.e = hdr.ff_calc.e ^ IRRED_POLY;

		if (high_bit_f_3 != 0)
			hdr.ff_calc.g = hdr.ff_calc.g ^ IRRED_POLY;

		if (high_bit_f_4 != 0)
			hdr.ff_calc.i = hdr.ff_calc.i ^ IRRED_POLY;

		if (high_bit_f_5 != 0)
			hdr.ff_calc.k = hdr.ff_calc.k ^ IRRED_POLY;

		if (high_bit_f_6 != 0)
			hdr.ff_calc.m = hdr.ff_calc.m ^ IRRED_POLY;

		if (high_bit_f_7 != 0)
			hdr.ff_calc.o = hdr.ff_calc.o ^ IRRED_POLY;

        
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
