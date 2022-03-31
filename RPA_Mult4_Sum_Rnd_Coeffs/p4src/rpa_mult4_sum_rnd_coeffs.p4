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

header seed_h {
    bit<8> seed;
}

header ff_calc_t {
    bit<8> x1;
    bit<8> x2;
    bit<8> x3;
    bit<8> x4;
}

header exit_ff_calc_h{
    bit<8>  coef1;
    bit<8>  coef2;
    bit<8>  coef3;
    bit<8>  coef4;
    bit<8>  result;
}

header port_h {
    bit<16> port_num;
}

header bridge_h {
    bit<8> x1_part;
    bit<8> coef1_part;
    bit<8> result1_part;
    bit<8> x2_part;
    bit<8> coef2_part;
    bit<8> result2_part;
    bit<8> x3_part;
    bit<8> coef3_part;
    bit<8> result3_part;
    bit<8> x4_part;
    bit<8> coef4_part;
    bit<8> result4_part;
    bit<8> coef1;
    bit<8> coef2;
    bit<8> coef3;
    bit<8> coef4;
}



/*************************************************************************
 **************  I N G R E S S   P R O C E S S I N G   *******************
 *************************************************************************/
 
/***********************  H E A D E R S  ************************/

struct my_ingress_headers_t {
    bridge_h        bridge;
    ethernet_h      ethernet;
    //seed_h        seed;
    ff_calc_t       ff_calc;
    port_h          port;
}

/******  G L O B A L   I N G R E S S   M E T A D A T A  *********/

struct mul_metadata_t {
    bit<8>      x;
    bit<8>      part_result;
    bit<8>      high_bit_f;
}

struct my_ingress_metadata_t {
    mul_metadata_t  mult_properties_1;
    mul_metadata_t  mult_properties_2;
    mul_metadata_t  mult_properties_3;
    mul_metadata_t  mult_properties_4;
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
        transition meta_init;
    }

    state meta_init {
        meta.mult_properties_1 = {0, 0, 0};
        meta.mult_properties_2 = {0, 0, 0};
        meta.mult_properties_3 = {0, 0, 0};
        meta.mult_properties_4 = {0, 0, 0};
        transition parse_ethernet;
    }

    state parse_ethernet {
        pkt.extract(hdr.ethernet);
        transition select(hdr.ethernet.ether_type) {
            TYPE_CODING: parse_ff_calc;
            default: reject;
        }
    }

    state parse_ff_calc {
        pkt.extract(hdr.ff_calc);
        meta.mult_properties_1.x = hdr.ff_calc.x1;
        meta.mult_properties_2.x = hdr.ff_calc.x2;
        meta.mult_properties_3.x = hdr.ff_calc.x3;
        meta.mult_properties_4.x = hdr.ff_calc.x4;
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
    Random<bit<8>>() rdn;

    action action_ff_mult(inout bit<8> coef_1, inout bit<8> coef_2, inout bit<8> coef_3, inout bit<8> coef_4) {
        
        // FIRST ELEMENT
        meta.mult_properties_1.high_bit_f = meta.mult_properties_1.x & HIGH_BIT_MASK;
        
        meta.mult_properties_1.x = meta.mult_properties_1.x << 1;
        coef_1 = coef_1 >> 1;

        // SECOND ELEMENT

        meta.mult_properties_2.high_bit_f = meta.mult_properties_2.x & HIGH_BIT_MASK;
        
        meta.mult_properties_2.x = meta.mult_properties_2.x << 1;
        coef_2 = coef_2 >> 1;

        // THIRD ELEMENT


        meta.mult_properties_3.high_bit_f = meta.mult_properties_3.x & HIGH_BIT_MASK;
        
        meta.mult_properties_3.x = meta.mult_properties_3.x << 1;
        coef_3 = coef_3 >> 1;

        // FORTH ELEMENT


        meta.mult_properties_4.high_bit_f = meta.mult_properties_4.x & HIGH_BIT_MASK;
        
        meta.mult_properties_4.x = meta.mult_properties_4.x << 1;
        coef_4 = coef_4 >> 1;
        
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
        bit<8> low_bit_1;
        bit<8> low_bit_2;
        bit<8> low_bit_3;
        bit<8> low_bit_4;

        hdr.bridge.setValid();

        hdr.bridge.coef1 = rdn.get(); 
        hdr.bridge.coef2 = rdn.get(); 
        hdr.bridge.coef3 = rdn.get(); 
        hdr.bridge.coef4 = rdn.get(); 

        bit<8> coef_1 = hdr.bridge.coef1;
        bit<8> coef_2 = hdr.bridge.coef2;
        bit<8> coef_3 = hdr.bridge.coef3;
        bit<8> coef_4 = hdr.bridge.coef4;

        // bit<8> coef_1 = 0;
        // bit<8> coef_2 = 8;
        // bit<8> coef_3 = 2;
        // bit<8> coef_4 = 224;

        // Save the used coefficients in the bridge header
        

        table_forward.apply();

        // PASS 1
        low_bit_1 = coef_1 & 0x1;
        low_bit_2 = coef_2 & 0x1;
        low_bit_3 = coef_3 & 0x1;
        low_bit_4 = coef_4 & 0x1;
        
        if (low_bit_1 != 0) {
            meta.mult_properties_1.part_result = meta.mult_properties_1.part_result ^ meta.mult_properties_1.x;
        }

        if (low_bit_2 != 0) {
            meta.mult_properties_2.part_result = meta.mult_properties_2.part_result ^ meta.mult_properties_2.x;
        }

        if (low_bit_3 != 0) {
            meta.mult_properties_3.part_result = meta.mult_properties_3.part_result ^ meta.mult_properties_3.x;
        }

        if (low_bit_4 != 0) {
            meta.mult_properties_4.part_result = meta.mult_properties_4.part_result ^ meta.mult_properties_4.x;
        }

        action_ff_mult(coef_1, coef_2, coef_3, coef_4);

        if (meta.mult_properties_1.high_bit_f != 0) {
            meta.mult_properties_1.x = meta.mult_properties_1.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_2.high_bit_f != 0) {
            meta.mult_properties_2.x = meta.mult_properties_2.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_3.high_bit_f != 0) {
            meta.mult_properties_3.x = meta.mult_properties_3.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_4.high_bit_f != 0) {
            meta.mult_properties_4.x = meta.mult_properties_4.x ^ IRRED_POLY;
        }

        // PASS 2
        low_bit_1 = coef_1 & 0x1;
        low_bit_2 = coef_2 & 0x1;
        low_bit_3 = coef_3 & 0x1;
        low_bit_4 = coef_4 & 0x1;

        if (low_bit_1 != 0) {
            meta.mult_properties_1.part_result = meta.mult_properties_1.part_result ^ meta.mult_properties_1.x;
        }

        if (low_bit_2 != 0) {
            meta.mult_properties_2.part_result = meta.mult_properties_2.part_result ^ meta.mult_properties_2.x;
        }

        if (low_bit_3 != 0) {
            meta.mult_properties_3.part_result = meta.mult_properties_3.part_result ^ meta.mult_properties_3.x;
        }

        if (low_bit_4 != 0) {
            meta.mult_properties_4.part_result = meta.mult_properties_4.part_result ^ meta.mult_properties_4.x;
        }

        action_ff_mult(coef_1, coef_2, coef_3, coef_4);

        if (meta.mult_properties_1.high_bit_f != 0) {
            meta.mult_properties_1.x = meta.mult_properties_1.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_2.high_bit_f != 0) {
            meta.mult_properties_2.x = meta.mult_properties_2.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_3.high_bit_f != 0) {
            meta.mult_properties_3.x = meta.mult_properties_3.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_4.high_bit_f != 0) {
            meta.mult_properties_4.x = meta.mult_properties_4.x ^ IRRED_POLY;
        }

        // PASS 3
        low_bit_1 = coef_1 & 0x1;
        low_bit_2 = coef_2 & 0x1;
        low_bit_3 = coef_3 & 0x1;
        low_bit_4 = coef_4 & 0x1;

        if (low_bit_1 != 0) {
            meta.mult_properties_1.part_result = meta.mult_properties_1.part_result ^ meta.mult_properties_1.x;
        }

        if (low_bit_2 != 0) {
            meta.mult_properties_2.part_result = meta.mult_properties_2.part_result ^ meta.mult_properties_2.x;
        }

        if (low_bit_3 != 0) {
            meta.mult_properties_3.part_result = meta.mult_properties_3.part_result ^ meta.mult_properties_3.x;
        }

        if (low_bit_4 != 0) {
            meta.mult_properties_4.part_result = meta.mult_properties_4.part_result ^ meta.mult_properties_4.x;
        }

        action_ff_mult(coef_1, coef_2, coef_3, coef_4);

        if (meta.mult_properties_1.high_bit_f != 0) {
            meta.mult_properties_1.x = meta.mult_properties_1.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_2.high_bit_f != 0) {
            meta.mult_properties_2.x = meta.mult_properties_2.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_3.high_bit_f != 0) {
            meta.mult_properties_3.x = meta.mult_properties_3.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_4.high_bit_f != 0) {
            meta.mult_properties_4.x = meta.mult_properties_4.x ^ IRRED_POLY;
        }

        // PASS 4
        low_bit_1 = coef_1 & 0x1;
        low_bit_2 = coef_2 & 0x1;
        low_bit_3 = coef_3 & 0x1;
        low_bit_4 = coef_4 & 0x1;

        if (low_bit_1 != 0) {
            meta.mult_properties_1.part_result = meta.mult_properties_1.part_result ^ meta.mult_properties_1.x;
        }

        if (low_bit_2 != 0) {
            meta.mult_properties_2.part_result = meta.mult_properties_2.part_result ^ meta.mult_properties_2.x;
        }

        if (low_bit_3 != 0) {
            meta.mult_properties_3.part_result = meta.mult_properties_3.part_result ^ meta.mult_properties_3.x;
        }

        if (low_bit_4 != 0) {
            meta.mult_properties_4.part_result = meta.mult_properties_4.part_result ^ meta.mult_properties_4.x;
        }

        action_ff_mult(coef_1, coef_2, coef_3, coef_4);
        
        if (meta.mult_properties_1.high_bit_f != 0) {
            meta.mult_properties_1.x = meta.mult_properties_1.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_2.high_bit_f != 0) {
            meta.mult_properties_2.x = meta.mult_properties_2.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_3.high_bit_f != 0) {
            meta.mult_properties_3.x = meta.mult_properties_3.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_4.high_bit_f != 0) {
            meta.mult_properties_4.x = meta.mult_properties_4.x ^ IRRED_POLY;
        }

        

        hdr.bridge.x1_part = meta.mult_properties_1.x;
        hdr.bridge.coef1_part = coef_1;
        hdr.bridge.result1_part = meta.mult_properties_1.part_result;

        hdr.bridge.x2_part = meta.mult_properties_2.x;
        hdr.bridge.coef2_part = coef_2;
        hdr.bridge.result2_part = meta.mult_properties_2.part_result;

        hdr.bridge.x3_part = meta.mult_properties_3.x;
        hdr.bridge.coef3_part = coef_3;
        hdr.bridge.result3_part = meta.mult_properties_3.part_result;

        hdr.bridge.x4_part = meta.mult_properties_4.x;
        hdr.bridge.coef4_part = coef_4;
        hdr.bridge.result4_part = meta.mult_properties_4.part_result;
        
        
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
        pkt.emit(hdr.bridge);
        pkt.emit(hdr.ethernet);
        pkt.emit(hdr.port);
    }
}


/*************************************************************************
 ****************  E G R E S S   P R O C E S S I N G   *******************
 *************************************************************************/

    /***********************  H E A D E R S  ************************/

struct my_egress_headers_t {
    ethernet_h  ethernet;
    exit_ff_calc_h  exit_ff_calc;
    port_h      port;
}

struct coef_t {
    bit<8>  coef_1;
    bit<8>  coef_2;
    bit<8>  coef_3;
    bit<8>  coef_4;
}

    /********  G L O B A L   E G R E S S   M E T A D A T A  *********/

struct my_egress_metadata_t {
    coef_t          coeffs;
    coef_t          original_coeffs;
    mul_metadata_t  mult_properties_1;
    mul_metadata_t  mult_properties_2;
    mul_metadata_t  mult_properties_3;
    mul_metadata_t  mult_properties_4;
    
}

    /***********************  P A R S E R  **************************/

parser EgressParser(packet_in        pkt,
    /* User */
    out my_egress_headers_t          hdr,
    out my_egress_metadata_t         meta,
    /* Intrinsic */
    out egress_intrinsic_metadata_t  eg_intr_md)
{
    bridge_h bridge;

    /* This is a mandatory state, required by Tofino Architecture */
    state start {
        pkt.extract(eg_intr_md);
        transition parse_bridge;
    }

    state parse_bridge {
        pkt.extract(bridge);

        meta.coeffs.coef_1 = bridge.coef1_part;
        meta.coeffs.coef_2 = bridge.coef2_part;
        meta.coeffs.coef_3 = bridge.coef3_part;
        meta.coeffs.coef_4 = bridge.coef4_part;

        meta.original_coeffs.coef_1 = bridge.coef1;
        meta.original_coeffs.coef_2 = bridge.coef2;
        meta.original_coeffs.coef_3 = bridge.coef3;
        meta.original_coeffs.coef_4 = bridge.coef4;

        meta.mult_properties_1.x = bridge.x1_part;
        meta.mult_properties_1.part_result = bridge.result1_part;
        meta.mult_properties_1.high_bit_f = 0;
        
        meta.mult_properties_2.x = bridge.x2_part;
        meta.mult_properties_2.part_result = bridge.result2_part;
        meta.mult_properties_2.high_bit_f = 0;


        meta.mult_properties_3.x = bridge.x3_part;
        meta.mult_properties_3.part_result = bridge.result3_part;
        meta.mult_properties_3.high_bit_f = 0;

        meta.mult_properties_4.x = bridge.x4_part;
        meta.mult_properties_4.part_result = bridge.result4_part;
        meta.mult_properties_4.high_bit_f = 0;

        transition parse_ethernet;
    }

    state parse_ethernet {
        pkt.extract(hdr.ethernet);
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

    action action_ff_mult(inout bit<8> coef_1, inout bit<8> coef_2, inout bit<8> coef_3, inout bit<8> coef_4) {
        
        // FIRST ELEMENT
        meta.mult_properties_1.high_bit_f = meta.mult_properties_1.x & HIGH_BIT_MASK;
        
        meta.mult_properties_1.x = meta.mult_properties_1.x << 1;
        coef_1 = coef_1 >> 1;

        // SECOND ELEMENT
        meta.mult_properties_2.high_bit_f = meta.mult_properties_2.x & HIGH_BIT_MASK;
        
        meta.mult_properties_2.x = meta.mult_properties_2.x << 1;
        coef_2 = coef_2 >> 1;

        // THIRD ELEMENT
        meta.mult_properties_3.high_bit_f = meta.mult_properties_3.x & HIGH_BIT_MASK;
        
        meta.mult_properties_3.x = meta.mult_properties_3.x << 1;
        coef_3 = coef_3 >> 1;

        // FORTH ELEMENT
        meta.mult_properties_4.high_bit_f = meta.mult_properties_4.x & HIGH_BIT_MASK;
        
        meta.mult_properties_4.x = meta.mult_properties_4.x << 1;
        coef_4 = coef_4 >> 1;
        
    }

    action xor_sum(out bit<8> result, bit<8> a, bit<8> b) {
        result = a ^ b;
    }

    apply {
        bit<8> low_bit_1;
        bit<8> low_bit_2;
        bit<8> low_bit_3;
        bit<8> low_bit_4;

        bit<8> coef_1 = meta.coeffs.coef_1;
        bit<8> coef_2 = meta.coeffs.coef_2;
        bit<8> coef_3 = meta.coeffs.coef_3;
        bit<8> coef_4 = meta.coeffs.coef_4;

        // PASS 5
        low_bit_1 = coef_1 & 0x1;
        low_bit_2 = coef_2 & 0x1;
        low_bit_3 = coef_3 & 0x1;
        low_bit_4 = coef_4 & 0x1;

        if (low_bit_1 != 0) {
            meta.mult_properties_1.part_result = meta.mult_properties_1.part_result ^ meta.mult_properties_1.x;
        }

        if (low_bit_2 != 0) {
            meta.mult_properties_2.part_result = meta.mult_properties_2.part_result ^ meta.mult_properties_2.x;
        }

        if (low_bit_3 != 0) {
            meta.mult_properties_3.part_result = meta.mult_properties_3.part_result ^ meta.mult_properties_3.x;
        }

        if (low_bit_4 != 0) {
            meta.mult_properties_4.part_result = meta.mult_properties_4.part_result ^ meta.mult_properties_4.x;
        }

        action_ff_mult(coef_1, coef_2, coef_3, coef_4);

        
        if (meta.mult_properties_1.high_bit_f != 0) {
            meta.mult_properties_1.x = meta.mult_properties_1.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_2.high_bit_f != 0) {
            meta.mult_properties_2.x = meta.mult_properties_2.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_3.high_bit_f != 0) {
            meta.mult_properties_3.x = meta.mult_properties_3.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_4.high_bit_f != 0) {
            meta.mult_properties_4.x = meta.mult_properties_4.x ^ IRRED_POLY;
        }

        // PASS 6
        low_bit_1 = coef_1 & 0x1;
        low_bit_2 = coef_2 & 0x1;
        low_bit_3 = coef_3 & 0x1;
        low_bit_4 = coef_4 & 0x1;

        if (low_bit_1 != 0) {
            meta.mult_properties_1.part_result = meta.mult_properties_1.part_result ^ meta.mult_properties_1.x;
        }

        if (low_bit_2 != 0) {
            meta.mult_properties_2.part_result = meta.mult_properties_2.part_result ^ meta.mult_properties_2.x;
        }

        if (low_bit_3 != 0) {
            meta.mult_properties_3.part_result = meta.mult_properties_3.part_result ^ meta.mult_properties_3.x;
        }

        if (low_bit_4 != 0) {
            meta.mult_properties_4.part_result = meta.mult_properties_4.part_result ^ meta.mult_properties_4.x;
        }

        action_ff_mult(coef_1, coef_2, coef_3, coef_4);


        if (meta.mult_properties_1.high_bit_f != 0) {
            meta.mult_properties_1.x = meta.mult_properties_1.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_2.high_bit_f != 0) {
            meta.mult_properties_2.x = meta.mult_properties_2.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_3.high_bit_f != 0) {
            meta.mult_properties_3.x = meta.mult_properties_3.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_4.high_bit_f != 0) {
            meta.mult_properties_4.x = meta.mult_properties_4.x ^ IRRED_POLY;
        }

        // PASS 7
        low_bit_1 = coef_1 & 0x1;
        low_bit_2 = coef_2 & 0x1;
        low_bit_3 = coef_3 & 0x1;
        low_bit_4 = coef_4 & 0x1;

        if (low_bit_1 != 0) {
            meta.mult_properties_1.part_result = meta.mult_properties_1.part_result ^ meta.mult_properties_1.x;
        }

        if (low_bit_2 != 0) {
            meta.mult_properties_2.part_result = meta.mult_properties_2.part_result ^ meta.mult_properties_2.x;
        }

        if (low_bit_3 != 0) {
            meta.mult_properties_3.part_result = meta.mult_properties_3.part_result ^ meta.mult_properties_3.x;
        }

        if (low_bit_4 != 0) {
            meta.mult_properties_4.part_result = meta.mult_properties_4.part_result ^ meta.mult_properties_4.x;
        }

        action_ff_mult(coef_1, coef_2, coef_3, coef_4);

        if (meta.mult_properties_1.high_bit_f != 0) {
            meta.mult_properties_1.x = meta.mult_properties_1.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_2.high_bit_f != 0) {
            meta.mult_properties_2.x = meta.mult_properties_2.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_3.high_bit_f != 0) {
            meta.mult_properties_3.x = meta.mult_properties_3.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_4.high_bit_f != 0) {
            meta.mult_properties_4.x = meta.mult_properties_4.x ^ IRRED_POLY;
        }

        // PASS 8
        low_bit_1 = coef_1 & 0x1;
        low_bit_2 = coef_2 & 0x1;
        low_bit_3 = coef_3 & 0x1;
        low_bit_4 = coef_4 & 0x1;

        if (low_bit_1 != 0) {
            meta.mult_properties_1.part_result = meta.mult_properties_1.part_result ^ meta.mult_properties_1.x;
        }

        if (low_bit_2 != 0) {
            meta.mult_properties_2.part_result = meta.mult_properties_2.part_result ^ meta.mult_properties_2.x;
        }

        if (low_bit_3 != 0) {
            meta.mult_properties_3.part_result = meta.mult_properties_3.part_result ^ meta.mult_properties_3.x;
        }

        if (low_bit_4 != 0) {
            meta.mult_properties_4.part_result = meta.mult_properties_4.part_result ^ meta.mult_properties_4.x;
        }

        action_ff_mult(coef_1, coef_2, coef_3, coef_4);

        if (meta.mult_properties_1.high_bit_f != 0) {
            meta.mult_properties_1.x = meta.mult_properties_1.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_2.high_bit_f != 0) {
            meta.mult_properties_2.x = meta.mult_properties_2.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_3.high_bit_f != 0) {
            meta.mult_properties_3.x = meta.mult_properties_3.x ^ IRRED_POLY;
        }
        if (meta.mult_properties_4.high_bit_f != 0) {
            meta.mult_properties_4.x = meta.mult_properties_4.x ^ IRRED_POLY;
        }

        hdr.ethernet.ether_type = TYPE_RESPONSE;

        hdr.exit_ff_calc.setValid();
        hdr.exit_ff_calc.coef1 = meta.original_coeffs.coef_1;
        hdr.exit_ff_calc.coef2 = meta.original_coeffs.coef_2;
        hdr.exit_ff_calc.coef3 = meta.original_coeffs.coef_3;
        hdr.exit_ff_calc.coef4 = meta.original_coeffs.coef_4;

        // hdr.exit_ff_calc.coef1 = meta.mult_properties_1.part_result;
        // hdr.exit_ff_calc.coef2 = meta.mult_properties_2.part_result;
        // hdr.exit_ff_calc.coef3 = meta.mult_properties_3.part_result;
        // hdr.exit_ff_calc.coef4 = meta.mult_properties_4.part_result;

        // Uncomment these 5 lines if you want the result of the sum of the 4 multiplications
        // MAP-REDUCE
        bit<8> first;
        bit<8> second;

        xor_sum(first,  meta.mult_properties_1.part_result,  meta.mult_properties_2.part_result);
        xor_sum(second, meta.mult_properties_3.part_result,  meta.mult_properties_4.part_result);
        // hdr.exit_ff_calc.coef1 = first;
        // hdr.exit_ff_calc.coef2 = second;
        xor_sum(hdr.exit_ff_calc.result, first, second); 

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
        pkt.emit(hdr.ethernet);
        pkt.emit(hdr.exit_ff_calc);
        pkt.emit(hdr.port);
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
