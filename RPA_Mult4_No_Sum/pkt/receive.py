#!/usr/bin/python
from rpa_pkt import *
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

print ("Sniffing on ", iface)
print ("Press Ctrl-C to stop...")
sniff(iface=iface, prn=lambda p: p.show())