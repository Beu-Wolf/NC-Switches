# Table Multiplication and Division in Tofino Switch

This `.p4` is capable of performing finite field multiplication and division in a Tofino Switch. It can currently compute results in GF(256), but can be easily extended to GF(65536).

## Compilation

In order to select the field you want to use, when compiling the program, select one of the flags:

- `FF_8`
- `FF_16`

So, for working with GF(256) you need to compile the program with:

```bash
<path-to-sh>/p4_build.sh <path-this-dir>/p4src/ff_mult_table.p4 -DFF_8
```

## Setup of tables

There is already a script present that loads the log, antilog and inverse tables with the correct values. As of now, it only works for GF(256). In order to load them, after running the `<path-to-sh>/run_tofino_model.sh -p ff_mult_table` and the `<path-to-sh>/run_switchd.sh -p ff_mult_table`, run:

```bash
<path-to-sh>/run_bfshell.sh -b <path-this-dir>/bfrt_python/setup_ff8.py -i
```

## Sending and receiving packets

In order to actually use the switch and perform calculations, use the scripts in the `pkt` directory.

### Sending

In order to send, you can select either multiplication or division by setting the first command line argument to `m` or `d`, respectively. The other 2 arguments are the numbers you want to perform the operation with.

Example for multiplication:

```bash
sudo python3 send.py m 10 5
```

Example for division:

```bash
sudo python3 send.py d 10 5
```

### Receiving

In order to receive the packets, you just need to run the `receive.py` script:

```bash
sudo python3 receive.py
```
