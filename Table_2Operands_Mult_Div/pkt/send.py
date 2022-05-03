from scapy.all import *
import sys
import os

if os.getuid() !=0:
    print("""
ERROR: This script requires root privileges. 
       Use 'sudo' to run it.
""")
    quit()

if sys.argv[1] == 'm':
    op = 0
elif sys.argv[1] == 'd':
    op = 1
else:
    print("You can only (m)ultiply or (d)ivide")
    quit()

a = int(sys.argv[2])
b = int(sys.argv[3])

class RPANC(Packet):
    name = "P4 RPA NC"
    fields_desc=[
            ByteField('Op', 0),
            ByteField('A', 0),
            ByteField('B', 0),
            IntField('Result', 0)
    ]

class Port(Packet):
    name = "Port Header"
    fields_desc=[
            ShortField("port_h", 0)
    ]

iface = "veth6"

bind_layers(Ether, RPANC, type=0x1234)

p = (Ether(dst="00:11:22:33:44:55", src="00:aa:bb:cc:dd:ee")/
     RPANC(Op=op, A=a, B=b)/
     Port(port_h=3))
    
print(p.show())

sendp(p, iface=iface) 