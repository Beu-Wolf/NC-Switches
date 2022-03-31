# NC-Switches

For now, this folder holds 4 different programs, all with the aim of performing Network Coding operations, specifically the enconding in the P4 switches. All these solutions are based on the Russian Peasant Algorithm, a more compute intensive way of performing finite field multiplication, not bound by the size of the log and anti-log tables.

## 1 - Simple multiplication

[RPA_2Operands_No_Sum](./RPA_2Operands_No_Sum/rpa_nc.p4RPA) - This version just performs a simple finite field multiplication, in GF(256), of 2 operands, `a` and `b`, which are set in the packet. It outputs the result

## 2 - Multiplication of 4 Symbols at the same time

[RPA_Mult4_No_Sum](./RPA_Mult4_No_Sum/p4src/rpa_mult4_no_sum.p4) - This version multiplies 4 symbols by some coefficients you define and outputs the result of the multiplications. So for example it can be used to perform finite field multiplication on a packet with 4 bytes, if you select the 4 coefficients to be the same

## 3 - Multiplication of 4 Symbols and summation

[RPA_Mult4_Sum_Rnd_Coeffs](./RPA_Mult4_Sum_Rnd_Coeffs/p4src/rpa_mult4_sum_rnd_coeffs.p4) - This version multiplies 4 symbols by 4 random coefficients that the switch generates and outputs the sum of the multiplications and the coefficients used. So basically it performs 1 full NC encoding operation of 4, 1 byte sized, packets.

**NOTE:** As of right now, the RPA_P4_Mult4_Sum_Rnd_Coeffs.p4 is selecting random coefficients for each packet that arrives. If we want to use the same for more than 1 packet, we need to move the coefficients to registers

## 4 - Multiplication of 4 Symbols and summation using Taurus

[RPA_Taurus_Mult4_Sum](./RPA_Taurus_Switch/RPA_Taurus_Mul4_Sum/FFMult.scala) - This is Spatial code for an FPGA, that follows the architecture of Taurus, the paper about Map Reduce operations in the data plane at line-rate. We are in the process of talking to one of the authors to get more help and fully flesh out a solution.

## Some Considerations

There are some limitations of course. Due to the operations necessary for the Russian Peasant Algorithm and the stages used, there is no way to do a finite field bigger than GF(256) and multiplying more than 4 symbols at the same time.
