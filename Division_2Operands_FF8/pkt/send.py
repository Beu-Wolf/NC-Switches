from scapy.all import *
import sys
import os

if os.getuid() !=0:
    print("""
ERROR: This script requires root privileges. 
       Use 'sudo' to run it.
""")
    quit()

a = int(sys.argv[1])
b = int(sys.argv[2])

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

iface = "veth6"

bind_layers(Ether, RPANC, type=0x1234)

p = (Ether(dst="00:11:22:33:44:55", src="00:aa:bb:cc:dd:ee")/
     RPANC(A=a, B=b)/
     Port(port_h=3))
    
print(p.show())

sendp(p, iface=iface) 