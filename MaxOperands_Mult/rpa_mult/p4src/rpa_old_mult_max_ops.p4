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
    bit<8> result_0;
    bit<8> result_1;
    bit<8> result_2;
    bit<8> result_3;
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
    bit<8> a;
    bit<8> b;
    bit<8> c;
    bit<8> d;
    bit<8> e;
    bit<8> f;
    bit<8> g;
    bit<8> h;
    bit<8> result_0;
    bit<8> result_1;
    bit<8> result_2;
    bit<8> result_3;
    bit<8> high_bit_f_0; 
    bit<8> high_bit_f_1; 
    bit<8> high_bit_f_2; 
    bit<8> high_bit_f_3; 
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
        
        meta.a = hdr.ff_calc.a;
        meta.b = hdr.ff_calc.b;
        meta.c = hdr.ff_calc.c;
        meta.d = hdr.ff_calc.d;
        meta.e = hdr.ff_calc.e;
        meta.f = hdr.ff_calc.f;
        meta.g = hdr.ff_calc.g;
        meta.h = hdr.ff_calc.h;
        meta.result_0 = 0;
        meta.result_1 = 0;
        meta.result_2 = 0;
        meta.result_3 = 0;
        meta.high_bit_f_0 = 0;
        meta.high_bit_f_1 = 0;
        meta.high_bit_f_2 = 0;
        meta.high_bit_f_3 = 0;
        
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
    action action_ff_mult() {
        
        // ELEMENT 0
		meta.high_bit_f_0 = meta.a & HIGH_BIT_MASK;

		meta.a = meta.a << 1;
		meta.b = meta.b >> 1;

        // ELEMENT 1
		meta.high_bit_f_1 = meta.c & HIGH_BIT_MASK;

		meta.c = meta.c << 1;
		meta.d = meta.d >> 1;

        // ELEMENT 2
		meta.high_bit_f_2 = meta.e & HIGH_BIT_MASK;

		meta.e = meta.e << 1;
		meta.f = meta.f >> 1;

        // ELEMENT 3
		meta.high_bit_f_3 = meta.g & HIGH_BIT_MASK;

		meta.g = meta.g << 1;
		meta.h = meta.h >> 1;



    }


    action action_forward(PortId_t port) {
        ig_tm_md.ucast_egress_port = port;
    }

    table table_forward {
        key = {
            hdr.port.port_num: exact;
        }
        actions = {
            action_forward;
        }
        size = 65536;
    }

    

    apply {
        bit<8> low_bit_f_0;
        bit<8> low_bit_f_1;
        bit<8> low_bit_f_2;
        bit<8> low_bit_f_3;

        table_forward.apply();

        // PASS 1
        low_bit_f_0 = meta.b & 0x1;
        low_bit_f_1 = meta.d & 0x1;
        low_bit_f_2 = meta.f & 0x1;
        low_bit_f_3 = meta.h & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			meta.result_0 = meta.result_0 ^ meta.a;

		if (low_bit_f_1 != 0)
			meta.result_1 = meta.result_1 ^ meta.c;

		if (low_bit_f_2 != 0)
			meta.result_2 = meta.result_2 ^ meta.e;

		if (low_bit_f_3 != 0)
			meta.result_3 = meta.result_3 ^ meta.g;



        action_ff_mult();

        // If high bit here
		if (meta.high_bit_f_0 != 0)
			meta.a = meta.a ^ IRRED_POLY;

		if (meta.high_bit_f_1 != 0)
			meta.c = meta.c ^ IRRED_POLY;

		if (meta.high_bit_f_2 != 0)
			meta.e = meta.e ^ IRRED_POLY;

		if (meta.high_bit_f_3 != 0)
			meta.g = meta.g ^ IRRED_POLY;


        

        // PASS 2
        low_bit_f_0 = meta.b & 0x1;
        low_bit_f_1 = meta.d & 0x1;
        low_bit_f_2 = meta.f & 0x1;
        low_bit_f_3 = meta.h & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			meta.result_0 = meta.result_0 ^ meta.a;

		if (low_bit_f_1 != 0)
			meta.result_1 = meta.result_1 ^ meta.c;

		if (low_bit_f_2 != 0)
			meta.result_2 = meta.result_2 ^ meta.e;

		if (low_bit_f_3 != 0)
			meta.result_3 = meta.result_3 ^ meta.g;



        action_ff_mult();

        // If high bit here
		if (meta.high_bit_f_0 != 0)
			meta.a = meta.a ^ IRRED_POLY;

		if (meta.high_bit_f_1 != 0)
			meta.c = meta.c ^ IRRED_POLY;

		if (meta.high_bit_f_2 != 0)
			meta.e = meta.e ^ IRRED_POLY;

		if (meta.high_bit_f_3 != 0)
			meta.g = meta.g ^ IRRED_POLY;

        

        // PASS 3
        low_bit_f_0 = meta.b & 0x1;
        low_bit_f_1 = meta.d & 0x1;
        low_bit_f_2 = meta.f & 0x1;
        low_bit_f_3 = meta.h & 0x1;

        // If low bit here
		if (low_bit_f_0 != 0)
			meta.result_0 = meta.result_0 ^ meta.a;

		if (low_bit_f_1 != 0)
			meta.result_1 = meta.result_1 ^ meta.c;

		if (low_bit_f_2 != 0)
			meta.result_2 = meta.result_2 ^ meta.e;

		if (low_bit_f_3 != 0)
			meta.result_3 = meta.result_3 ^ meta.g;



        action_ff_mult();

        // If high bit here
		if (meta.high_bit_f_0 != 0)
			meta.a = meta.a ^ IRRED_POLY;

		if (meta.high_bit_f_1 != 0)
			meta.c = meta.c ^ IRRED_POLY;

		if (meta.high_bit_f_2 != 0)
			meta.e = meta.e ^ IRRED_POLY;

		if (meta.high_bit_f_3 != 0)
			meta.g = meta.g ^ IRRED_POLY;


        // PASS 4
        low_bit_f_0 = meta.b & 0x1;
        low_bit_f_1 = meta.d & 0x1;
        low_bit_f_2 = meta.f & 0x1;
        low_bit_f_3 = meta.h & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			meta.result_0 = meta.result_0 ^ meta.a;

		if (low_bit_f_1 != 0)
			meta.result_1 = meta.result_1 ^ meta.c;

		if (low_bit_f_2 != 0)
			meta.result_2 = meta.result_2 ^ meta.e;

		if (low_bit_f_3 != 0)
			meta.result_3 = meta.result_3 ^ meta.g;



        action_ff_mult();

        // If high bit here
		if (meta.high_bit_f_0 != 0)
			meta.a = meta.a ^ IRRED_POLY;

		if (meta.high_bit_f_1 != 0)
			meta.c = meta.c ^ IRRED_POLY;

		if (meta.high_bit_f_2 != 0)
			meta.e = meta.e ^ IRRED_POLY;

		if (meta.high_bit_f_3 != 0)
			meta.g = meta.g ^ IRRED_POLY;


        // Write to packet header

        hdr.ff_calc.a = meta.a;
        hdr.ff_calc.b = meta.b;
        hdr.ff_calc.c = meta.c;
        hdr.ff_calc.d = meta.d;
        hdr.ff_calc.e = meta.e;
        hdr.ff_calc.f = meta.f;
        hdr.ff_calc.g = meta.g;
        hdr.ff_calc.h = meta.h;
        
        hdr.ff_calc.result_0 = meta.result_0;
        hdr.ff_calc.result_1 = meta.result_1;
        hdr.ff_calc.result_2 = meta.result_2;
        hdr.ff_calc.result_3 = meta.result_3;
        
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
    bit<8> a;
    bit<8> b;
    bit<8> c;
    bit<8> d;
    bit<8> e;
    bit<8> f;
    bit<8> g;
    bit<8> h;
    bit<8> result_0;
    bit<8> result_1;
    bit<8> result_2;
    bit<8> result_3;
    bit<8> high_bit_f_0;
    bit<8> high_bit_f_1;
    bit<8> high_bit_f_2;
    bit<8> high_bit_f_3;
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
        
        meta.a = hdr.ff_calc.a;
        meta.b = hdr.ff_calc.b;
        meta.c = hdr.ff_calc.c;
        meta.d = hdr.ff_calc.d;
        meta.e = hdr.ff_calc.e;
        meta.f = hdr.ff_calc.f;
        meta.g = hdr.ff_calc.g;
        meta.h = hdr.ff_calc.h;
        meta.result_0 = hdr.ff_calc.result_0;
        meta.result_1 = hdr.ff_calc.result_1;
        meta.result_2 = hdr.ff_calc.result_2;
        meta.result_3 = hdr.ff_calc.result_3;
        meta.high_bit_f_0 = 0;
        meta.high_bit_f_1 = 0;
        meta.high_bit_f_2 = 0;
        meta.high_bit_f_3 = 0;

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

    action action_ff_mult() {
        
        // ELEMENT 0
		meta.high_bit_f_0 = meta.a & HIGH_BIT_MASK;

		meta.a = meta.a << 1;
		meta.b = meta.b >> 1;

        // ELEMENT 1
		meta.high_bit_f_1 = meta.c & HIGH_BIT_MASK;

		meta.c = meta.c << 1;
		meta.d = meta.d >> 1;

        // ELEMENT 2
		meta.high_bit_f_2 = meta.e & HIGH_BIT_MASK;

		meta.e = meta.e << 1;
		meta.f = meta.f >> 1;

        // ELEMENT 3
		meta.high_bit_f_3 = meta.g & HIGH_BIT_MASK;

		meta.g = meta.g << 1;
		meta.h = meta.h >> 1;




    }

    apply {
        
        bit<8> low_bit_f_0;
        bit<8> low_bit_f_1;
        bit<8> low_bit_f_2;
        bit<8> low_bit_f_3;


        // PASS 5
        low_bit_f_0 = meta.b & 0x1;
        low_bit_f_1 = meta.d & 0x1;
        low_bit_f_2 = meta.f & 0x1;
        low_bit_f_3 = meta.h & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			meta.result_0 = meta.result_0 ^ meta.a;

		if (low_bit_f_1 != 0)
			meta.result_1 = meta.result_1 ^ meta.c;

		if (low_bit_f_2 != 0)
			meta.result_2 = meta.result_2 ^ meta.e;

		if (low_bit_f_3 != 0)
			meta.result_3 = meta.result_3 ^ meta.g;



        action_ff_mult();

        // If high bit here
		if (meta.high_bit_f_0 != 0)
			meta.a = meta.a ^ IRRED_POLY;

		if (meta.high_bit_f_1 != 0)
			meta.c = meta.c ^ IRRED_POLY;

		if (meta.high_bit_f_2 != 0)
			meta.e = meta.e ^ IRRED_POLY;

		if (meta.high_bit_f_3 != 0)
			meta.g = meta.g ^ IRRED_POLY;

        

        // PASS 6
        low_bit_f_0 = meta.b & 0x1;
        low_bit_f_1 = meta.d & 0x1;
        low_bit_f_2 = meta.f & 0x1;
        low_bit_f_3 = meta.h & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			meta.result_0 = meta.result_0 ^ meta.a;

		if (low_bit_f_1 != 0)
			meta.result_1 = meta.result_1 ^ meta.c;

		if (low_bit_f_2 != 0)
			meta.result_2 = meta.result_2 ^ meta.e;

		if (low_bit_f_3 != 0)
			meta.result_3 = meta.result_3 ^ meta.g;



        action_ff_mult();

        // If high bit here
		if (meta.high_bit_f_0 != 0)
			meta.a = meta.a ^ IRRED_POLY;

		if (meta.high_bit_f_1 != 0)
			meta.c = meta.c ^ IRRED_POLY;

		if (meta.high_bit_f_2 != 0)
			meta.e = meta.e ^ IRRED_POLY;

		if (meta.high_bit_f_3 != 0)
			meta.g = meta.g ^ IRRED_POLY;

    


        // PASS 7
        low_bit_f_0 = meta.b & 0x1;
        low_bit_f_1 = meta.d & 0x1;
        low_bit_f_2 = meta.f & 0x1;
        low_bit_f_3 = meta.h & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			meta.result_0 = meta.result_0 ^ meta.a;

		if (low_bit_f_1 != 0)
			meta.result_1 = meta.result_1 ^ meta.c;

		if (low_bit_f_2 != 0)
			meta.result_2 = meta.result_2 ^ meta.e;

		if (low_bit_f_3 != 0)
			meta.result_3 = meta.result_3 ^ meta.g;



        action_ff_mult();

        // If high bit here
		if (meta.high_bit_f_0 != 0)
			meta.a = meta.a ^ IRRED_POLY;

		if (meta.high_bit_f_1 != 0)
			meta.c = meta.c ^ IRRED_POLY;

		if (meta.high_bit_f_2 != 0)
			meta.e = meta.e ^ IRRED_POLY;

		if (meta.high_bit_f_3 != 0)
			meta.g = meta.g ^ IRRED_POLY;

        

        // PASS 8
        low_bit_f_0 = meta.b & 0x1;
        low_bit_f_1 = meta.d & 0x1;
        low_bit_f_2 = meta.f & 0x1;
        low_bit_f_3 = meta.h & 0x1;
        
        // If low bit here
		if (low_bit_f_0 != 0)
			meta.result_0 = meta.result_0 ^ meta.a;

		if (low_bit_f_1 != 0)
			meta.result_1 = meta.result_1 ^ meta.c;

		if (low_bit_f_2 != 0)
			meta.result_2 = meta.result_2 ^ meta.e;

		if (low_bit_f_3 != 0)
			meta.result_3 = meta.result_3 ^ meta.g;



        action_ff_mult();

        // If high bit here
		if (meta.high_bit_f_0 != 0)
			meta.a = meta.a ^ IRRED_POLY;

		if (meta.high_bit_f_1 != 0)
			meta.c = meta.c ^ IRRED_POLY;

		if (meta.high_bit_f_2 != 0)
			meta.e = meta.e ^ IRRED_POLY;

		if (meta.high_bit_f_3 != 0)
			meta.g = meta.g ^ IRRED_POLY;

        


        hdr.ethernet.ether_type = TYPE_RESPONSE;
        hdr.ff_calc.a = meta.a;
        hdr.ff_calc.b = meta.b;
        hdr.ff_calc.c = meta.c;
        hdr.ff_calc.d = meta.d;
        hdr.ff_calc.e = meta.e;
        hdr.ff_calc.f = meta.f;
        hdr.ff_calc.g = meta.g;
        hdr.ff_calc.h = meta.h;
        hdr.ff_calc.result_0 = meta.result_0;
        hdr.ff_calc.result_1 = meta.result_1;
        hdr.ff_calc.result_2 = meta.result_2;
        hdr.ff_calc.result_3 = meta.result_3;
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
