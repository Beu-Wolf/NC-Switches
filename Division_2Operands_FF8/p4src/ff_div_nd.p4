/* -*- P4_16 -*- */

#include <core.p4>
#include <tna.p4>

/*************************************************************************
 ************* C O N S T A N T S    A N D   T Y P E S  *******************
**************************************************************************/

#define IRRED_POLY 0xb

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
    bit<8> a;
    bit<8> b;
    bit<8> v;
    bit<8> P;
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
    bit<8>  v;
}

struct second_operand_t {
    bit<8>  b;
    bit<8>  P;
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
        meta.first_operand.v = 0;
        meta.second_operand.b = 0;
        meta.second_operand.P = IRRED_POLY;
        meta.delta = -1;
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
    bit<8> aux_a;

    action action_divide_val_a(inout bit<8> a, inout bit<8> v) {
        a = a ^ v;
        v = aux_a;
    }

    action action_divide_val_b(inout bit<8> b, inout bit<8> P) {
        b = b ^ P;
        P = aux_b;
    }

    action action_divide_xor_a(inout bit<8> a, bit<8> v) {
        a = a ^ v;
    }

    action action_divide_xor_b(inout bit<8> b, bit<8> P) {
        b = b ^ P;
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
        bit<8> b_pair_flag;
        bit<2> a_aux;
        bit    a_help;
        bit    a_low;
        bit    a_high;

        table_forward.apply();

        // PASS 1
        b_pair_flag = meta.second_operand.b & 0x1;
        if(b_pair_flag != 0) {
            if (meta.delta < 0) {
                aux_a = meta.first_operand.a;
                aux_b = meta.second_operand.b;

                action_divide_val_a(meta.first_operand.a, meta.first_operand.v);
                action_divide_val_b(meta.second_operand.b, meta.second_operand.P);
                meta.delta = -meta.delta;
            } else {
                action_divide_xor_a(meta.first_operand.a, meta.first_operand.v);
                action_divide_xor_b(meta.second_operand.b, meta.second_operand.P);
            }
        }

        meta.delta = meta.delta - 1;
        meta.second_operand.b = meta.second_operand.b >> 1;

        a_low = meta.first_operand.a[0:0];
        a_aux = meta.first_operand.a[1:0];
        a_high = meta.first_operand.a[2:2];
        
        if(a_aux == 2 || a_aux == 1) {
            a_help = 1;
        } else {
            a_help = 0;
        }

        meta.first_operand.a[2:2] = a_low;
        meta.first_operand.a[0:0] = a_help;
        meta.first_operand.a[1:1] = a_high;


        // PASS 2
        b_pair_flag = meta.second_operand.b & 0x1;
        if(b_pair_flag != 0) {
            if (meta.delta < 0) {
                aux_a = meta.first_operand.a;
                aux_b = meta.second_operand.b;

                action_divide_val_a(meta.first_operand.a, meta.first_operand.v);
                action_divide_val_b(meta.second_operand.b, meta.second_operand.P);
                meta.delta = -meta.delta;
            } else {
                action_divide_xor_a(meta.first_operand.a, meta.first_operand.v);
                action_divide_xor_b(meta.second_operand.b, meta.second_operand.P);
            }
        }

        meta.delta = meta.delta - 1;
        meta.second_operand.b = meta.second_operand.b >> 1;

        a_low = meta.first_operand.a[0:0];
        a_aux = meta.first_operand.a[1:0];
        a_high = meta.first_operand.a[2:2];
        
        if(a_aux == 2 || a_aux == 1) {
            a_help = 1;
        } else {
            a_help = 0;
        }

        meta.first_operand.a[2:2] = a_low;
        meta.first_operand.a[0:0] = a_help;
        meta.first_operand.a[1:1] = a_high;

        hdr.bridge.setValid();

        hdr.bridge.a = meta.first_operand.a;
        hdr.bridge.b = meta.second_operand.b;
        hdr.bridge.v = meta.first_operand.v;
        hdr.bridge.P = meta.second_operand.P;
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
        meta.first_operand.a = bridge.a;
        meta.second_operand.b = bridge.b;
        meta.first_operand.v = bridge.v;
        meta.second_operand.P = bridge.P;
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
    bit<8> aux_b;
    bit<8> aux_a;

    action action_divide_val_a(inout bit<8> a, inout bit<8> v) {
        a = a ^ v;
        v = aux_a;
    }

    action action_divide_val_b(inout bit<8> b, inout bit<8> P) {
        b = b ^ P;
        P = aux_b;
    }

    action action_divide_xor_a(inout bit<8> a, bit<8> v) {
        a = a ^ v;
    }

    action action_divide_xor_b(inout bit<8> b, bit<8> P) {
        b = b ^ P;
    }


    apply {
        
        bit<8> b_pair_flag;
        bit<2> a_aux;
        bit    a_help;
        bit    a_low;
        bit    a_high;

        // PASS 3
        b_pair_flag = meta.second_operand.b & 0x1;
        if(b_pair_flag != 0) {
            if (meta.delta < 0) {
                aux_a = meta.first_operand.a;
                aux_b = meta.second_operand.b;

                action_divide_val_a(meta.first_operand.a, meta.first_operand.v);
                action_divide_val_b(meta.second_operand.b, meta.second_operand.P);
                meta.delta = -meta.delta;
            } else {
                action_divide_xor_a(meta.first_operand.a, meta.first_operand.v);
                action_divide_xor_b(meta.second_operand.b, meta.second_operand.P);
            }
        }

        meta.delta = meta.delta - 1;
        meta.second_operand.b = meta.second_operand.b >> 1;

        a_low = meta.first_operand.a[0:0];
        a_aux = meta.first_operand.a[1:0];
        a_high = meta.first_operand.a[2:2];
        
        if(a_aux == 2 || a_aux == 1) {
            a_help = 1;
        } else {
            a_help = 0;
        }

        meta.first_operand.a[2:2] = a_low;
        meta.first_operand.a[0:0] = a_help;
        meta.first_operand.a[1:1] = a_high;


        // PASS 4
        b_pair_flag = meta.second_operand.b & 0x1;
        if(b_pair_flag != 0) {
            if (meta.delta < 0) {
                aux_a = meta.first_operand.a;
                aux_b = meta.second_operand.b;

                action_divide_val_a(meta.first_operand.a, meta.first_operand.v);
                action_divide_val_b(meta.second_operand.b, meta.second_operand.P);
                meta.delta = -meta.delta;
            } else {
                action_divide_xor_a(meta.first_operand.a, meta.first_operand.v);
                action_divide_xor_b(meta.second_operand.b, meta.second_operand.P);
            }
        }

        meta.delta = meta.delta - 1;
        meta.second_operand.b = meta.second_operand.b >> 1;

        a_low = meta.first_operand.a[0:0];
        a_aux = meta.first_operand.a[1:0];
        a_high = meta.first_operand.a[2:2];
        
        if(a_aux == 2 || a_aux == 1) {
            a_help = 1;
        } else {
            a_help = 0;
        }

        meta.first_operand.a[2:2] = a_low;
        meta.first_operand.a[0:0] = a_help;
        meta.first_operand.a[1:1] = a_high;


        // PASS 5
        b_pair_flag = meta.second_operand.b & 0x1;
        if(b_pair_flag != 0 && meta.delta < 0) {
            hdr.ff_calc.result = meta.first_operand.a;
        } else {
            hdr.ff_calc.result = meta.first_operand.v;
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
