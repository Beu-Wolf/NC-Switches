from scapy.all import *

class RPANC(Packet):
    name = "RPA NC 4 coeffs packet"
    fields_desc = [
        ByteField('X1', 0),
        ByteField('X2', 0),
        ByteField('X3', 0),
        ByteField('X4', 0),
        ByteField('Result1', 0),
        ByteField('Result2', 0),
        ByteField('Result3', 0),
        ByteField('Result4', 0),
        ByteField('Result', 0)
    ]

class Port(Packet):
    name = "Port Header"
    fields_desc=[
            ShortField("port_h", 3)
    ]

bind_layers(Ether, RPANC, type=0x1234)