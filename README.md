# NC-Switches

This folder holds different programs, all with the aim of performing Finite Field Operations in Programmable Switches. The P4 program are compiled for the Tofino architecture, and the Spatial program is for the Taurus architecture, where we are trying to perform finite field operations with bigger fields.
The main aim of this repo is to perform Network Coding, specially encoding. As such, some versions perform several multiplications in parallel, and then sum them together.

Any question feel free to reach me @<daniel.g.seara@tecnico.ulisboa.pt>

## 0 - Table multiplication and division

[Table_2Operands_Mult_Div](./Table_2Operands_Mult_Div/p4src/ff_mult_table.p4) - This version is a base version showing how to perform finite field multiplication and division using log, antilog and inverse tables. It currently supports GF(256) but it can be extended to GF(65536) if the tables are added.

## 1 - Simple multiplication

[RPA_2Operands_No_Sum](./RPA_2Operands_No_Sum/rpa_nc.p4RPA) - This version just performs a simple finite field multiplication, in GF(256), of 2 operands, `a` and `b`, which are set in the packet. It outputs the result.

## 2.1 - Simple division using direct algorithm

[Division_2Operands_FF8](./Division_2Operands_FF8/p4src/ff_div_nd.p4) - This version performs a simple finite field division, in GF(8), of 2 operands, `a` and `b`, which are set in the packet. The algorithm used is a direct division algorithm from [here](https://ieeexplore.ieee.org/stamp/stamp.jsp?tp=&arnumber=922162). It outputs the result.

## 2.2 - Simple division by finding the inverse and multiplying

[Division_by_Inverse_2Operands_FF8](./Division_by_Inverse_2Operands_FF8/p4src/ff_div_inv.p4) - This version performs a simple finite field division, in GF(8), of 2 operands `a` and `b`, which are set in the packet. The operation is performed by first finding the inverse of operand `b` and then multiplying `a` by that value. This is because `a/b = a*b`<sup>`-1`</sup>. It outputs the result.

## 3 - Simple inversion

[Inverse_FF16](./Inverse_FF16/p4src/ff_16_inv.p4) - This version performs a simple inversion algorithm, in GF(16), meaning, for a value `a` set in the packet, it finds `a`<sup>`-1`</sup>. The algorithm used can be found [here](https://www.lirmm.fr/arith18/papers/kobayashi-AlgorithmInversionUsingPolynomialMultiplyInstruction.pdf). It outputs the result.

## 4 - Multiplication of 4 Symbols at the same time

[RPA_Mult4_No_Sum](./RPA_Mult4_No_Sum/p4src/rpa_mult4_no_sum.p4) - This version multiplies 4 symbols by the same coefficient you define and outputs the result of the multiplications. So for example it can be used to perform finite field multiplication on a packet with 4 bytes, since we are using the same coefficient for all the bytes.

## 5 - Multiplication of 4 Symbols and summation

[RPA_Mult4_Sum_Rnd_Coeffs](./RPA_Mult4_Sum_Rnd_Coeffs/p4src/rpa_mult4_sum_rnd_coeffs.p4) - This version multiplies 4 symbols by 4 random coefficients that the switch generates and outputs the sum of the multiplications and the coefficients used. So basically it performs 1 full NC encoding operation of 4, 1 byte sized, packets.

**NOTE:** As of right now, the RPA_P4_Mult4_Sum_Rnd_Coeffs.p4 is selecting random coefficients for each packet that arrives. If we want to use the same for more than 1 packet, we need to move the coefficients to registers

## 6 - Simple multiplication using Taurus

[RPA_Taurus_Mult2_No_Sum](./FFA_Taurus_Switch/RPA_Taurus_2Operands/FFMult_It_Pipe.scala) - This is Spatial code for an FPGA, that follows the architecture of Taurus. This PoC performs a normal finite field multiplication between 2 operands, using RPA, in field GF(256). The program compiles but, as of yet, we haven't tested it in a Taurus simulation to extract any meaningfull results.

## 7 - Multiplication of 4 Symbols and summation using Taurus

[RPA_Taurus_Mult4_Sum](./FFA_Taurus_Switch/RPA_Taurus_Mul4_Sum/FFMult.scala) - This is Spatial code for an FPGA, that follows the architecture of Taurus, where we do 4 multiplications and sum them together, like a common Network Coding encoding operation. Although it compiles, we haven't actually tested it in a Taurus simulation to extract meaningfull results.

## 8 - Simple division using Taurus

[EBd_Taurus_Div2_No_Sum](./FFA_Taurus_Switch/EBd_Taurus_2Operands/FFDiv_It_Pipe.scala) - This is Spatial code for an FPGA, that follows the architecture of Taurus.
This PoC performs normal finite field division betwenn 2 operands, using the EBd algorithm, in the field GF(256). The program compiles but has not been tested yet
to extract any meaningfull metrics.

## 9 - Simple inversion using Taurus

[Inv_Taurus_Div2_No_Sum](./FFA_Taurus_Switch/Inv_Taurus_2Operands/FFInv_It_Pipe.scala) - This is Spatial code for an FPGA, that follows the architecture of Taurus. This PoC performs a normal finite field inversion of an operand, using the inversion algorithm, in the field GF(256). The program compiles but has not been tested yet to extract any meaningfull metrics.

## Bonus - Maximum simultaneous multiplications

[MaxOperands_Mult](./MaxOperands_Mult/) - This folder contains `.py` generators capable of producing a valid `.p4` program to run in Tofino that performs several multiplications in parallel. With these generators, we can easily see the maximum number of simultaneous operations that Tofino can execute in parallel. For the Table multiplication, go [here](./MaxOperands_Mult/table_mult/bfrt_python/gen_table_mult_p4.py) and for the RPA multiplication, go [here](./MaxOperands_Mult/rpa_mult/bfrt_python/gen_rpa_mult_p4.py).

## Some Considerations

There are some limitations of course. Due to the operations necessary for the Russian Peasant Algorithm and the stages used, there is no way to do a finite field bigger than GF(256) and multiplying more than 4 symbols at the same time.
Divisions are worse, as they need more iterations in each finite field, so there seems to be no way to perform division in a finite field bigger than GF(8). The inverse is the exception, capable of working in GF(16).
