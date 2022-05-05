# Multiplication of 4 Symbols at the same time using RPA

This `.p4` is capable of performing 4 FF(2^8) multiplications at the same time with the same coefficient for each byte. Effectively, this performs 1 finite field multiplication on a packet with 4 bytes.

## Compilation

In order to select the coefficient you want to use, when compiling the program, use the flag:

- `F_COEF=<your-value>`

So, for multiplying by 10 each byte, you need to compile the program with:

```bash
<path-to-sh>/p4_build.sh <path-this-dir>/p4src/rpa_mult4_no_sum.p4 -DF_COEF=10
```

If you do not select a flag value, you will be multiplying by `1`.

## Sending and receiving packets

In order to actually use the switch and perform calculations, use the scripts in the `pkt` directory.

### Sending

In order to send, you call the script with the values of the 4 bytes (between 0 and 255)

Example:

```bash
sudo python3 send.py 2 3 4 5
```

### Receiving

In order to receive the packets, you just need to run the `receive.py` script:

```bash
sudo python3 receive.py
```
