# Max Simultaneous Multiplications

## Parallel Multiplications

In order to generate the `.scala` program, execute the `.py` script `gen_scripts/gen_rpa_mult_par.py`, providing the number of multiplications you want. Example for 9 simultaneous multiplications:

```bash
python3 gen_rpa_mult_par.py 9
```

Preliminary results show that we cannot do more than 9 parallel multiplications, being hindered by the number of CUs and MUs of Taurus.

## Sequential Multiplications

In order to generate the `.scala` program, execute the `.py` script `gen_scripts/gen_rpa_mult_seq.py`, providing the number of multiplications you want. Example for 9 simultaneous multiplications:

```bash
python3 gen_rpa_mult_seq.py 6
```

Preliminary results show that we cannot do more than 6 parallel multiplications, being hindered by the number of CUs and MUs of Taurus.
