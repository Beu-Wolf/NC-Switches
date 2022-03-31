# NC-Switches

This folder holds 2 P4 programs that perform Finite Field Multiplication using the Russian Peasant Algorithm in the Tofino Switch. Both these versions work only for GF(256). There are 2 versions here:

RPA_P4_Mult4_No_Sum.p4: This version multiplies 4 symbols by some coefficients you define and outputs the result of the multiplications. So for example it can be used to perform finite field multiplication on a packet with 4 bytes, if you select the 4 coefficients to be the same

RPA_P4_Mult4_Sum_Rnd_Coeffs.p4: This version multiplies 4 symbols by 4 random coefficients that the switch generates and outputs the sum of the multiplications and the coefficients used. So basically it performs 1 full NC encoding operation of 4, 1 byte, packets


NOTE: There are some limitations of course. Due to the operations necessary for the Russian Peasant Algorithm and the stages used, there is no way to do a finite field bigger than GF(256) and multiplying more than 4 symbols at the same time.

Also, as of right now, the RPA_P4_Mult4_Sum_Rnd_Coeffs.p4 is selecting random coefficients for each packet that arrives. If we want to use the same for more than 1 packet, we need to move the coefficients to registers
