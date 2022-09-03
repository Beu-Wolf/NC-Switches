import sys
import string

num_mults = int(sys.argv[1])
alphabet = list(string.ascii_lowercase) + [letter1+letter2 for letter1 in string.ascii_lowercase for letter2 in string.ascii_lowercase]


def replicate_line_alphabet(f, line, times):
    for i in range(times):
        tmpline = line.replace('X', alphabet[i])
        f.write(tmpline)

def replicate_line_number(f, line, times):
    for i in range(times):
        tmpline = line.replace('X', str(i))
        f.write(tmpline)

def operands_fifos(f, line, times):
    for i in range(times):
        element_line = line.replace('X', str(i))
        f.write(element_line)
        for j in range(7):
            f.write("\t\t\tval result_" + str(i) + "_stage" + str(j) + " = FIFO[T](4)\n")
            f.write("\t\t\tval " + alphabet[2*i] + "_stage" + str(j) + " = FIFO[T](4)\n")
            f.write("\t\t\tval " + alphabet[2*i+1] + "_stage" + str(j) + " = FIFO[T](4)\n\n")
        f.write("\t\t\tval result_" + str(i) + "_stage7" + " = FIFO[T](4)\n\n")

def write_pipe_0(f, mult_num):
    op_1 = alphabet[2*mult_num]
    op_2 = alphabet[2*mult_num+1]
    f.write("\t\t\t\tPipe {\n\n")
    f.write("\t\t\t\t\t// PASS 0\n")
    f.write("\t\t\t\t\tval " + op_1 + " = stream_" + op_1 + "_in.value\n")
    f.write("\t\t\t\t\tval " + op_2 + " = stream_" + op_2 + "_in.value\n\n")
    f.write("\t\t\t\t\tval result = 0\n\n")
    f.write("\t\t\t\t\tresult_" + str(mult_num) + "_stage0.enq(mux(" + op_2 + ".bit(0) == 1, result ^ " + op_1 + ", result))\n\n")
    f.write("\t\t\t\t\tval flag_" + op_1 + " = (" + op_1 + " & mask_a) == mask_a\n\n")
    f.write("\t\t\t\t\t" + op_1 + "_stage0.enq(mux(flag_" + op_1 + ", (" + op_1 + " << 1) ^ irred, " + op_1 + " << 1))\n")
    f.write("\t\t\t\t\t" + op_2 + "_stage0.enq(" + op_2 + " >> 1)\n")
    f.write("\t\t\t\t}\n\n")

def write_pipe_mid(f, mult_num, iter):
    op_1 = alphabet[2*mult_num]
    op_2 = alphabet[2*mult_num+1]
    f.write("\t\t\t\tPipe {\n\n")
    f.write("\t\t\t\t\t// PASS " + str(iter) + "\n")
    f.write("\t\t\t\t\tval " + op_1 + " = " + op_1 + "_stage" + str(iter-1) + ".deq()\n")
    f.write("\t\t\t\t\tval " + op_2 + " = " + op_2 + "_stage" + str(iter-1) + ".deq()\n\n")
    f.write("\t\t\t\t\tval result = result_" + str(mult_num) + "_stage" + str(iter-1) + ".deq()\n\n")
    f.write("\t\t\t\t\tresult_" + str(mult_num) + "_stage" + str(iter) + ".enq(mux(" + op_2 + ".bit(0) == 1, result ^ " + op_1 + ", result))\n\n")
    f.write("\t\t\t\t\tval flag_" + op_1 + " = (" + op_1 + " & mask_a) == mask_a\n\n")
    f.write("\t\t\t\t\t" + op_1 + "_stage" + str(iter) + ".enq(mux(flag_" + op_1 + ", (" + op_1 + " << 1) ^ irred, " + op_1 + " << 1))\n")
    f.write("\t\t\t\t\t" + op_2 + "_stage" + str(iter) + ".enq(" + op_2 + " >> 1)\n")
    f.write("\t\t\t\t}\n\n")

def write_pipe_final(f, mult_num):
    op_1 = alphabet[2*mult_num]
    op_2 = alphabet[2*mult_num+1]
    f.write("\t\t\t\tPipe {\n\n")
    f.write("\t\t\t\t\t// PASS 7\n")
    f.write("\t\t\t\t\tval " + op_1 + " = " + op_1 + "_stage6.deq()\n")
    f.write("\t\t\t\t\tval " + op_2 + " = " + op_2 + "_stage6.deq()\n\n")
    f.write("\t\t\t\t\tval result = result_" + str(mult_num) + "_stage6.deq()\n\n")
    f.write("\t\t\t\t\tresult_" + str(mult_num) + "_stage7.enq(mux(" + op_2 + ".bit(0) == 1, result ^ " + op_1 + ", result))\n\n")
    f.write("\t\t\t\t}\n\n")


def write_pipe_block(f, line, times):
    for i in range(times):
        element_line = line.replace('X', str(i))
        f.write(element_line)
        
        write_pipe_0(f, i)
        for j in range(1, 7):
            write_pipe_mid(f, i, j)

        write_pipe_final(f, i)


def stream_out_line(f, line, times):
    f.write(line)
    new_line = "\t\t\t\tstream_out := Tup2((result_0_stage7.deq()"
    for i in range(1, times):
        new_line += " + result_" + str(i) + "_stage7.deq()"
    new_line += ").to[I32], p == (N-1))\n"
    f.write(new_line)

with open('template_rpa_mult_seq.scala', 'r') as template:
    with open('../FFMult_It_Opt_Seq.scala', 'w') as f:

        for line in template:
            
            if "val infile_X" in line:
                replicate_line_alphabet(f, line, num_mults*2)
            
            elif "writeCSVNow(" in line:
                replicate_line_alphabet(f, line, num_mults*2)
            
            elif "val stream_X_in" in line:
                replicate_line_alphabet(f, line, num_mults*2)
            
            elif "// MULT X FIFOS" in line:
                operands_fifos(f, line, num_mults)

            elif "// ELEMENT X" in line:
                write_pipe_block(f, line, num_mults)

            elif "// STREAM OUT" in line:
                stream_out_line(f, line, num_mults)

            else:
                f.write(line)