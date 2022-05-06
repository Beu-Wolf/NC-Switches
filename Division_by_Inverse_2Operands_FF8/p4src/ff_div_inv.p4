/* -*- P4_16 -*- */

#include <core.p4>
#include <tna.p4>

/*************************************************************************
 ************* C O N S T A N T S    A N D   T Y P E S  *******************
**************************************************************************/

#define IRRED_POLY 0xb
#define MSB 8
#define HIGH_BIT_MASK 4

const bit<16> TYPE_CODING = 0x1234;

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
    bit<48>   dst_addr;
    bit<48>   src_addr;
    bit<16>   ether_type;
}


header ff_calc_h {
    bit<8> a;
    bit<8> b;
    bit<8> result;
}

header port_h {
    bit<16> port_num;
}

header bridge_h {
    bit<8> b;
    bit<8> v;
    bit<8> u;
    bit<8> s;
    int<32> delta;
}


/*************************************************************************
 **************  I N G R E S S   P R O C E S S I N G   *******************
 *************************************************************************/
 
    /***********************  H E A D E R S  ************************/

struct my_ingress_headers_t {
    bridge_h    bridge;
    ethernet_h  ethernet;
    ff_calc_h   ff_calc;
    port_h      port;
}


struct first_operand_t {
    bit<8>  a;
}

struct second_operand_t {
    bit<8>  b;
    bit<8>  v;
    bit<8>  u;
    bit<8>  s;
}

    /******  G L O B A L   I N G R E S S   M E T A D A T A  *********/
    
struct my_ingress_metadata_t {
    first_operand_t first_operand;
    second_operand_t second_operand;
    int<32>  delta;
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
        meta.first_operand.a = 0;
        meta.second_operand.b = 0;
        meta.second_operand.v = 0;
        meta.second_operand.u = 1;
        meta.second_operand.s = IRRED_POLY;
        meta.delta = 0;
        transition select(hdr.ethernet.ether_type) {
            TYPE_CODING: parse_ff_calc;
            default: accept;
        }
    }

    state parse_ff_calc {
        pkt.extract(hdr.ff_calc);
        meta.first_operand.a = hdr.ff_calc.a;
        meta.second_operand.b = hdr.ff_calc.b;
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
    bit<8> aux_b;
    bit<8> aux_u;

    action action_shift_b_u(inout bit<8> b, inout bit<8> u ) {
        b = b << 1;
        u = u << 1;
        meta.delta = meta.delta + 1;
    }

    action action_xor_s_v(inout bit<8> s, inout bit<8> v, bit<8> b, bit<8> u) {
        s = s ^ b;
        v = v ^ u;
    }

    action action_swap_b_s(inout bit<8> b, inout bit<8> s) {
        b = s;
        s = aux_b;
    }

    action action_swap_shift_u(inout bit<8> u, inout bit<8> v) {
        u = v << 1;
        v = aux_u;
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
        bit<8> b_msb_flag;
        bit<8> s_msb_flag;

        table_forward.apply();
        
        // PASS 1
        action_shift_b_u(meta.second_operand.b, meta.second_operand.u);

        // PASS 2
        b_msb_flag = meta.second_operand.b & MSB;
        s_msb_flag = meta.second_operand.s & MSB;
        if(b_msb_flag == 0) {
            action_shift_b_u(meta.second_operand.b, meta.second_operand.u);
        } else {
            if(s_msb_flag != 0) {
                action_xor_s_v(meta.second_operand.s, meta.second_operand.v, meta.second_operand.b, meta.second_operand.u);
            }
            meta.second_operand.s = meta.second_operand.s << 1;
            if (meta.delta == 0) {
                aux_b = meta.second_operand.b;
                aux_u = meta.second_operand.u;
                action_swap_b_s(meta.second_operand.b, meta.second_operand.s);
                action_swap_shift_u(meta.second_operand.u, meta.second_operand.v);
                meta.delta = 1;
            } else {
                meta.second_operand.u = meta.second_operand.u >> 1;
                meta.delta = meta.delta - 1;
            }
        }

        // PASS 3
        b_msb_flag = meta.second_operand.b & MSB;
        s_msb_flag = meta.second_operand.s & MSB;
        if(b_msb_flag == 0) {
            action_shift_b_u(meta.second_operand.b, meta.second_operand.u);
        } else {
            if(s_msb_flag != 0) {
                action_xor_s_v(meta.second_operand.s, meta.second_operand.v, meta.second_operand.b, meta.second_operand.u);
            }
            meta.second_operand.s = meta.second_operand.s << 1;
            if (meta.delta == 0) {
                aux_b = meta.second_operand.b;
                aux_u = meta.second_operand.u;
                action_swap_b_s(meta.second_operand.b, meta.second_operand.s);
                action_swap_shift_u(meta.second_operand.u, meta.second_operand.v);
                meta.delta = 1;
            } else {
                meta.second_operand.u = meta.second_operand.u >> 1;
                meta.delta = meta.delta - 1;
            }
        }

        //PASS 4
        b_msb_flag = meta.second_operand.b & MSB;
        s_msb_flag = meta.second_operand.s & MSB;
        if(b_msb_flag == 0) {
            action_shift_b_u(meta.second_operand.b, meta.second_operand.u);
        } else {
            if(s_msb_flag != 0) {
                action_xor_s_v(meta.second_operand.s, meta.second_operand.v, meta.second_operand.b, meta.second_operand.u);
            }
            meta.second_operand.s = meta.second_operand.s << 1;
            if (meta.delta == 0) {
                aux_b = meta.second_operand.b;
                aux_u = meta.second_operand.u;
                action_swap_b_s(meta.second_operand.b, meta.second_operand.s);
                action_swap_shift_u(meta.second_operand.u, meta.second_operand.v);
                meta.delta = 1;
            } else {
                meta.second_operand.u = meta.second_operand.u >> 1;
                meta.delta = meta.delta - 1;
            }
        }

        
        hdr.bridge.setValid();

        hdr.bridge.b = meta.second_operand.b;
        hdr.bridge.v = meta.second_operand.v;
        hdr.bridge.u = meta.second_operand.u;
        hdr.bridge.s = meta.second_operand.s;
        hdr.bridge.delta = meta.delta;
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
        pkt.emit(hdr.ff_calc);
        pkt.emit(hdr.port);
    }
}


/*************************************************************************
 ****************  E G R E S S   P R O C E S S I N G   *******************
 *************************************************************************/

    /***********************  H E A D E R S  ************************/

struct my_egress_headers_t {
    ethernet_h  ethernet;
    ff_calc_h   ff_calc;
    port_h      port;
}

    /********  G L O B A L   E G R E S S   M E T A D A T A  *********/

struct my_egress_metadata_t {
    first_operand_t first_operand;
    second_operand_t second_operand;
    bit<8>   result;
    int<32>  delta;
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
        meta.second_operand.b = bridge.b;
        meta.second_operand.v = bridge.v;
        meta.second_operand.u = bridge.u;
        meta.second_operand.s = bridge.s;
        meta.result = 0;
        meta.delta = bridge.delta;
        transition parse_ethernet;
    }

    state parse_ethernet {
        pkt.extract(hdr.ethernet);
        transition parse_ff_calc;
    }

    state parse_ff_calc {
        pkt.extract(hdr.ff_calc);
        meta.first_operand.a = hdr.ff_calc.a;
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
   
    bit<8> aux_b;
    bit<8> aux_u;
    bit<8> high_bit_f = 0;

    action action_shift_b_u(inout bit<8> b, inout bit<8> u ) {
        b = b << 1;
        u = u << 1;
        meta.delta = meta.delta + 1;
    }

    action action_xor_s_v(inout bit<8> s, inout bit<8> v, bit<8> b, bit<8> u) {
        s = s ^ b;
        v = v ^ u;
    }

    action action_swap_b_s(inout bit<8> b, inout bit<8> s) {
        b = s;
        s = aux_b;
    }

    action action_swap_shift_u(inout bit<8> u, inout bit<8> v) {
        u = v << 1;
        v = aux_u;
    }

    action action_ff_mult(inout bit<8> a, inout bit<8> b, bool low_bit) {
        
        if (low_bit) {
            meta.result = meta.result ^ a;
        }

        high_bit_f = a & HIGH_BIT_MASK;
        
        a = a << 1;
        b = b >> 1;
        
    }


    apply {
        bit<8> b_msb_flag;
        bit<8> s_msb_flag;
        bit<8> low_bit_f;

        // PASS 5
        b_msb_flag = meta.second_operand.b & MSB;
        s_msb_flag = meta.second_operand.s & MSB;
        if(b_msb_flag == 0) {
            action_shift_b_u(meta.second_operand.b, meta.second_operand.u);
        } else {
            if(s_msb_flag != 0) {
                action_xor_s_v(meta.second_operand.s, meta.second_operand.v, meta.second_operand.b, meta.second_operand.u);
            }
            meta.second_operand.s = meta.second_operand.s << 1;
            if (meta.delta == 0) {
                aux_b = meta.second_operand.b;
                aux_u = meta.second_operand.u;
                action_swap_b_s(meta.second_operand.b, meta.second_operand.s);
                action_swap_shift_u(meta.second_operand.u, meta.second_operand.v);
                meta.delta = 1;
            } else {
                meta.second_operand.u = meta.second_operand.u >> 1;
                meta.delta = meta.delta - 1;
            }
        }

        // PASS 6
        b_msb_flag = meta.second_operand.b & MSB;
        s_msb_flag = meta.second_operand.s & MSB;
        
        if(b_msb_flag == 0) {
            meta.second_operand.u = meta.second_operand.u << 1;
        } else {
            if(s_msb_flag != 0) {
                meta.second_operand.v = meta.second_operand.v ^ meta.second_operand.u;
            }
            if (meta.delta == 0) {
                meta.second_operand.u = meta.second_operand.v << 1;
            }  else {
                meta.second_operand.u = meta.second_operand.u >> 1;
            }
        }

        // After this, you will have the inverse
        hdr.ff_calc.b = meta.second_operand.u;
        meta.second_operand.b = meta.second_operand.u;

        if ((hdr.ff_calc.a == 0) || (hdr.ff_calc.b == 0)) {
            hdr.ff_calc.result = 0;
        } else {
            // PASS 1
            low_bit_f = meta.second_operand.b & 0x1;
            if (low_bit_f != 0)
                action_ff_mult(meta.first_operand.a, meta.second_operand.b, true);
            else {
                action_ff_mult(meta.first_operand.a, meta.second_operand.b, false);
            }
            if (high_bit_f != 0) {
                meta.first_operand.a = meta.first_operand.a ^ IRRED_POLY;
            }

            // PASS 2
            low_bit_f = meta.second_operand.b & 0x1;
            if (low_bit_f != 0)
                action_ff_mult(meta.first_operand.a, meta.second_operand.b, true);
            else {
                action_ff_mult(meta.first_operand.a, meta.second_operand.b, false);
            }
            if (high_bit_f != 0) {
                meta.first_operand.a = meta.first_operand.a ^ IRRED_POLY;
            }

            // PASS 3
            low_bit_f = meta.second_operand.b & 0x1;
            if (low_bit_f != 0) 
                action_ff_mult(meta.first_operand.a, meta.second_operand.b, true);
            else {
                action_ff_mult(meta.first_operand.a, meta.second_operand.b, false);
            }
            // if (high_bit_f != 0) {
            //     meta.first_operand.a = meta.first_operand.a ^ IRRED_POLY;
            // }

            hdr.ff_calc.result = meta.result;
        }
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
