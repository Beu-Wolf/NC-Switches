# Max Simultaneous Multiplications

## Table Multiplication

In order to generate the `.p4` program, navigate to the `table_mult/bfrt_python` folder and execute the `.py` script, providing the number of multiplications you want. Example for 10 simultaneous multiplications:

```bash
python3 gen_table_mult_p4.py 10
```

Preliminary results show that we cannot do more than 15 parallel multiplications, being hindered by the PHV allocations. In GF(2^16), the Ingress can only do 10 multiplications, but we suspect we can do the remaining 5 in the Egress pipe.

## RPA Multiplication

TODO: Create new template and change the README to reflect the 8 maximum multiplications

In order to generate the `.p4` program, navigate to the `rpa_mult/bfrt_python` folder and execute the `.py` script just like previously mentioned, providing the number of wanted multiplications

Preliminary results also show that we cannot do more than 8 parallel multiplications due to the resource limitations of the switch

**Note:** We leave an older version that uses `@pragmas` and that was able to compile a `.p4` program capable of doing 5 parallel multiplications. However, there does not seem to be a concrete logic to allocate be able to allocate even more. We leave the version in [rpa_mult/p4src/rpa_mult_5_ops_pragmas.p4](./rpa_mult/p4src/rpa_mult_5_ops_pragmas.p4).
