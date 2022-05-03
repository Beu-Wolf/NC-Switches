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
    bit<8>       op;
    field_size_t a;
    field_size_t b;
    bit<32>      result;
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
    bit<32> tmp_log_value;
    bit<32> tmp_sum_value;
    bit<32> b;
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
        meta.tmp_log_value = 0;
        meta.tmp_sum_value = 0;
        meta.b = 0;
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

    bit<32> log_a;
    bit<32> log_b;


    action send(PortId_t port) {
        ig_tm_md.ucast_egress_port = port;
    }

    action drop() {
        ig_dprsr_md.drop_ctl = 1;
    }


    action get_log(bit<32> log_value) {
        meta.tmp_log_value = log_value;
    }

    action get_inverse(bit<32> inverse_value) {
        meta.b = inverse_value;
    }

    action get_antilog(bit<32> antilog_value) {
        hdr.ff_calc.result = antilog_value;
    }

    action sub_max(bit<32> sum_logs) {
        meta.tmp_sum_value = sum_logs - MAX_SIZE;
    }

    action sum_vals(bit<32> val1, bit<32> val2) {
        meta.tmp_sum_value = val1 + val2;
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

    table table_log_a {
        key = {
            hdr.ff_calc.a: exact;
        }
        actions = {
            get_log;
        }
        #if defined(FF_8)
        size = 256;
        #elif defined(FF_16)
        size = 65536;
        #endif
    }

    table table_log_b {
        key = {
            meta.b: exact;
        }
        actions = {
            get_log;
        }
        #if defined(FF_8)
        size = 256;
        #elif defined(FF_16)
        size = 65536;
        #endif
    }

    table table_inverse {
        key = {
            hdr.ff_calc.b: exact;
        }
        actions = {
            get_inverse;
        }
        #if defined(FF_8)
        size = 256;
        #elif defined(FF_16)
        size = 65536;
        #endif
    }

    table table_antilog {
        key = {
            meta.tmp_sum_value: exact;
        }
        actions = {
            get_antilog;
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
        log_a = meta.tmp_log_value;

        if (hdr.ff_calc.op & 1 != 0) {
            table_inverse.apply();
        } else {
            meta.b = (bit<32>) hdr.ff_calc.b;
        }

        table_log_b.apply();
        log_b = meta.tmp_log_value;
        
        sum_vals(log_a, log_b);

        if (meta.tmp_sum_value == MAX_SIZE || meta.tmp_sum_value & BIT_MASK != 0) {
            sub_max(meta.tmp_sum_value);
        }

        table_antilog.apply();
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
