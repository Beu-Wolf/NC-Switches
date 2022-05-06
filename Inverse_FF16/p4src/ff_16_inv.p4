/* -*- P4_16 -*- */

#include <core.p4>
#include <tna.p4>

/*************************************************************************
 ************* C O N S T A N T S    A N D   T Y P E S  *******************
**************************************************************************/

#define IRRED_POLY 0x13
#define MSB 16

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
    bit<8> result;
}

header port_h {
    bit<16> port_num;
}

header bridge_h {
    bit<8> a;
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


struct inv_help_vars_t {
    bit<8>  a;
    bit<8>  v;
    bit<8>  u;
    bit<8>  s;
}

    /******  G L O B A L   I N G R E S S   M E T A D A T A  *********/
    
struct my_ingress_metadata_t {
    inv_help_vars_t inverse;
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
        meta.inverse.a = 0;
        meta.inverse.v = 0;
        meta.inverse.u = 1;
        meta.inverse.s = IRRED_POLY;
        meta.delta = 0;
        transition select(hdr.ethernet.ether_type) {
            TYPE_CODING: parse_ff_calc;
            default: accept;
        }
    }

    state parse_ff_calc {
        pkt.extract(hdr.ff_calc);
        meta.inverse.a = hdr.ff_calc.a;
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
    bit<8> aux_a;
    bit<8> aux_u;

    action action_shift_a_u(inout bit<8> a, inout bit<8> u ) {
        a = a << 1;
        u = u << 1;
        meta.delta = meta.delta + 1;
    }

    action action_xor_s_v(inout bit<8> s, inout bit<8> v, bit<8> a, bit<8> u) {
        s = s ^ a;
        v = v ^ u;
    }

    action action_swap_a_s(inout bit<8> a, inout bit<8> s) {
        a = s;
        s = aux_a;
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
        bit<8> a_msb_flag;
        bit<8> s_msb_flag;

        table_forward.apply();
        
        // PASS 1
        action_shift_a_u(meta.inverse.a, meta.inverse.u);

        // PASS 2
        a_msb_flag = meta.inverse.a & MSB;
        s_msb_flag = meta.inverse.s & MSB;
        if(a_msb_flag == 0) {
            action_shift_a_u(meta.inverse.a, meta.inverse.u);
        } else {
            if(s_msb_flag != 0) {
                action_xor_s_v(meta.inverse.s, meta.inverse.v, meta.inverse.a, meta.inverse.u);
            }
            meta.inverse.s = meta.inverse.s << 1;
            if (meta.delta == 0) {
                aux_a = meta.inverse.a;
                aux_u = meta.inverse.u;
                action_swap_a_s(meta.inverse.a, meta.inverse.s);
                action_swap_shift_u(meta.inverse.u, meta.inverse.v);
                meta.delta = 1;
            } else {
                meta.inverse.u = meta.inverse.u >> 1;
                meta.delta = meta.delta - 1;
            }
        }

        // PASS 3
        a_msb_flag = meta.inverse.a & MSB;
        s_msb_flag = meta.inverse.s & MSB;
        if(a_msb_flag == 0) {
            action_shift_a_u(meta.inverse.a, meta.inverse.u);
        } else {
            if(s_msb_flag != 0) {
                action_xor_s_v(meta.inverse.s, meta.inverse.v, meta.inverse.a, meta.inverse.u);
            }
            meta.inverse.s = meta.inverse.s << 1;
            if (meta.delta == 0) {
                aux_a = meta.inverse.a;
                aux_u = meta.inverse.u;
                action_swap_a_s(meta.inverse.a, meta.inverse.s);
                action_swap_shift_u(meta.inverse.u, meta.inverse.v);
                meta.delta = 1;
            } else {
                meta.inverse.u = meta.inverse.u >> 1;
                meta.delta = meta.delta - 1;
            }
        }

        //PASS 4
        a_msb_flag = meta.inverse.a & MSB;
        s_msb_flag = meta.inverse.s & MSB;
        if(a_msb_flag == 0) {
            action_shift_a_u(meta.inverse.a, meta.inverse.u);
        } else {
            if(s_msb_flag != 0) {
                action_xor_s_v(meta.inverse.s, meta.inverse.v, meta.inverse.a, meta.inverse.u);
            }
            meta.inverse.s = meta.inverse.s << 1;
            if (meta.delta == 0) {
                aux_a = meta.inverse.a;
                aux_u = meta.inverse.u;
                action_swap_a_s(meta.inverse.a, meta.inverse.s);
                action_swap_shift_u(meta.inverse.u, meta.inverse.v);
                meta.delta = 1;
            } else {
                meta.inverse.u = meta.inverse.u >> 1;
                meta.delta = meta.delta - 1;
            }
        }

        
        hdr.bridge.setValid();

        hdr.bridge.a = meta.inverse.a;
        hdr.bridge.v = meta.inverse.v;
        hdr.bridge.u = meta.inverse.u;
        hdr.bridge.s = meta.inverse.s;
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
    inv_help_vars_t inverse;
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
        meta.inverse.a = bridge.a;
        meta.inverse.v = bridge.v;
        meta.inverse.u = bridge.u;
        meta.inverse.s = bridge.s;
        meta.delta = bridge.delta;
        transition parse_ethernet;
    }

    state parse_ethernet {
        pkt.extract(hdr.ethernet);
        transition parse_ff_calc;
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
   
    bit<8> aux_a;
    bit<8> aux_u;

    action action_shift_a_u(inout bit<8> a, inout bit<8> u ) {
        a = a << 1;
        u = u << 1;
        meta.delta = meta.delta + 1;
    }

    action action_xor_s_v(inout bit<8> s, inout bit<8> v, bit<8> a, bit<8> u) {
        s = s ^ a;
        v = v ^ u;
    }

    action action_swap_a_s(inout bit<8> a, inout bit<8> s) {
        a = s;
        s = aux_a;
    }

    action action_swap_shift_u(inout bit<8> u, inout bit<8> v) {
        u = v << 1;
        v = aux_u;
    }


    apply {
        bit<8> a_msb_flag;
        bit<8> s_msb_flag;

        // PASS 5
        a_msb_flag = meta.inverse.a & MSB;
        s_msb_flag = meta.inverse.s & MSB;
        if(a_msb_flag == 0) {
            action_shift_a_u(meta.inverse.a, meta.inverse.u);
        } else {
            if(s_msb_flag != 0) {
                action_xor_s_v(meta.inverse.s, meta.inverse.v, meta.inverse.a, meta.inverse.u);
            }
            meta.inverse.s = meta.inverse.s << 1;
            if (meta.delta == 0) {
                aux_a = meta.inverse.a;
                aux_u = meta.inverse.u;
                action_swap_a_s(meta.inverse.a, meta.inverse.s);
                action_swap_shift_u(meta.inverse.u, meta.inverse.v);
                meta.delta = 1;
            } else {
                meta.inverse.u = meta.inverse.u >> 1;
                meta.delta = meta.delta - 1;
            }
        }

        // PASS 6
        a_msb_flag = meta.inverse.a & MSB;
        s_msb_flag = meta.inverse.s & MSB;
        if(a_msb_flag == 0) {
            action_shift_a_u(meta.inverse.a, meta.inverse.u);
        } else {
            if(s_msb_flag != 0) {
                action_xor_s_v(meta.inverse.s, meta.inverse.v, meta.inverse.a, meta.inverse.u);
            }
            meta.inverse.s = meta.inverse.s << 1;
            if (meta.delta == 0) {
                aux_a = meta.inverse.a;
                aux_u = meta.inverse.u;
                action_swap_a_s(meta.inverse.a, meta.inverse.s);
                action_swap_shift_u(meta.inverse.u, meta.inverse.v);
                meta.delta = 1;
            } else {
                meta.inverse.u = meta.inverse.u >> 1;
                meta.delta = meta.delta - 1;
            }
        }

        //PASS 7
        a_msb_flag = meta.inverse.a & MSB;
        s_msb_flag = meta.inverse.s & MSB;
        if(a_msb_flag == 0) {
            action_shift_a_u(meta.inverse.a, meta.inverse.u);
        } else {
            if(s_msb_flag != 0) {
                action_xor_s_v(meta.inverse.s, meta.inverse.v, meta.inverse.a, meta.inverse.u);
            }
            meta.inverse.s = meta.inverse.s << 1;
            if (meta.delta == 0) {
                aux_a = meta.inverse.a;
                aux_u = meta.inverse.u;
                action_swap_a_s(meta.inverse.a, meta.inverse.s);
                action_swap_shift_u(meta.inverse.u, meta.inverse.v);
                meta.delta = 1;
            } else {
                meta.inverse.u = meta.inverse.u >> 1;
                meta.delta = meta.delta - 1;
            }
        }

        // PASS 8
        a_msb_flag = meta.inverse.a & MSB;
        s_msb_flag = meta.inverse.s & MSB;
        
        if(a_msb_flag == 0) {
            meta.inverse.u = meta.inverse.u << 1;
        } else {
            if(s_msb_flag != 0) {
                meta.inverse.v = meta.inverse.v ^ meta.inverse.u;
            }
            if (meta.delta == 0) {
                meta.inverse.u = meta.inverse.v << 1;
            }  else {
                meta.inverse.u = meta.inverse.u >> 1;
            }
        }

        hdr.ff_calc.result = meta.inverse.u;
        
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
