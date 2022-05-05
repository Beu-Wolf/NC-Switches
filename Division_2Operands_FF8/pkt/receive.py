#!/usr/bin/python
import os
import sys

if os.getuid() !=0:
    print ("""
ERROR: This script requires root privileges. 
       Use 'sudo' to run it.
""")
    quit()

from scapy.all import *
try:
    iface=sys.argv[1]
except:
    iface="veth7"

class RPANC(Packet):
    name = "P4 RPA NC"
    fields_desc=[
            ByteField('A', 0),
            ByteField('B', 0),
            ByteField('Result', 0)
    ]

class Port(Packet):
    name = "Port Header"
    fields_desc=[
            ShortField("port_h", 0)
    ]


bind_layers(Ether, RPANC, type=0x1234)

print ("Sniffing on ", iface)
print ("Press Ctrl-C to stop...")
sniff(iface=iface, prn=lambda p: p.show())