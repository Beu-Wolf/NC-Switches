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
    bit<8> X;
    bit<8> result_X;
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

    bit<8> high_bit_f_X;

    bit<8> low_bit_f_X;

    action action_ff_mult() {
        
        // ELEMENT X


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
        low_bit_f_X = hdr.ff_calc.Y & 0x1;
        
        // If low bit here


        table_forward.apply();

        // If high bit here

        // PASS 2
        low_bit_f_X = hdr.ff_calc.Y & 0x1;
        
        // If low bit here


        action_ff_mult();

        // If high bit here
        

        // PASS 3
        low_bit_f_X = hdr.ff_calc.Y & 0x1;
        
        // If low bit here


        action_ff_mult();

        // If high bit here

        // PASS 4
        low_bit_f_X = hdr.ff_calc.Y & 0x1;
        
        // If low bit here


        action_ff_mult();

        // If high bit here

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

    bit<8> high_bit_f_X;

    bit<8> low_bit_f_X;

    action action_ff_mult() {
        
        // ELEMENT X


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
        low_bit_f_X = hdr.ff_calc.Y & 0x1;
        
        // If low bit here


        action_ff_mult();

        // If high bit here
        

        // PASS 6
        low_bit_f_X = hdr.ff_calc.Y & 0x1;
        
        // If low bit here


        action_ff_mult();

        // If high bit here
    


        // PASS 7
        low_bit_f_X = hdr.ff_calc.Y & 0x1;
        
        // If low bit here


        action_ff_mult();

        // If high bit here
        

        // PASS 8
        low_bit_f_X = hdr.ff_calc.Y & 0x1;
        
        // If low bit here


        action_ff_mult_end();

        // If high bit here
        
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
