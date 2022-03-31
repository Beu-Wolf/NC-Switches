from scapy.all import *

class RPANC_Receive(Packet):
    name = "RPA NC 4 coeffs and result"
    fields_desc = [
        ByteField('Coef1', 0),
        ByteField('Coef2', 0),
        ByteField('Coef3', 0),
        ByteField('Coef4', 0),
        ByteField('Result', 0)
    ]

class Port(Packet):
    name = "Port Header"
    fields_desc=[
            ShortField("port_h", 3)
    ]

bind_layers(Ether, RPANC_Receive, type=0x2345)