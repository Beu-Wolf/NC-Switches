from rpa_pkt import *
import sys
import os

if os.getuid() !=0:
    print("""
ERROR: This script requires root privileges. 
       Use 'sudo' to run it.
""")
    quit()

x1 = int(sys.argv[1])
x2 = int(sys.argv[2])
x3 = int(sys.argv[3])
x4 = int(sys.argv[4])

iface = "veth4"

p = (Ether(dst="00:11:22:33:44:55", src="00:aa:bb:cc:dd:ee")/
     RPANC(X1=x1, X2=x2, X3=x3, X4=x4)/
     Port(port_h=3))
    
print(p.show())

sendp(p, iface=iface) 




