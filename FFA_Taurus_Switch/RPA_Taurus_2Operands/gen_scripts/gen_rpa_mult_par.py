import sys
import string

num_mults = int(sys.argv[1])
alphabet = list(string.ascii_lowercase) + [letter1+letter2 for letter1 in string.ascii_lowercase for letter2 in string.ascii_lowercase]
iteration = 0

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

def replicate_result_line(f, line, times):
    for i in range(times):
        tmpline1 = line.replace('X', str(i))
        tmpline2 = tmpline1.replace('Y', str(iteration))
        tmpline3 = tmpline2.replace('Z', alphabet[2*i+1])
        tmpline4 = tmpline3.replace('W', alphabet[2*i])
        f.write(tmpline4)

def replicate_flag_line(f, line, times):
    for i in range(times):
        tmpline = line.replace('X', alphabet[2*i])
        f.write(tmpline)


def replicate_first_fifo_line(f, line, times):
    for i in range(times):
        tmpline1 = line.replace('X', alphabet[2*i])
        tmpline2 = tmpline1.replace('Y', str(iteration))
        f.write(tmpline2)

def replicate_second_fifo_line(f, line, times):
    for i in range(times):
        tmpline1 = line.replace('X', alphabet[i])
        tmpline2 = tmpline1.replace('Y', str(iteration))
        f.write(tmpline2)

def replicate_queue_fifo_line(f, line, times):
    for i in range(times):
        tmpline1 = line.replace('X', alphabet[2*i+1])
        tmpline2 = tmpline1.replace('Y', str(iteration))
        f.write(tmpline2)

def stream_out_line(f, line, times):
    f.write(line)
    new_line = "\t\t\t\tstream_out := Tup2((result_0_stage7.deq()"
    for i in range(1, times):
        new_line += " + result_" + str(i) + "_stage7.deq()"
    new_line += ").to[I32], p == (N-1))\n"
    f.write(new_line)

with open('template_rpa_mult_par.scala', 'r') as template:
    with open('../FFMult_It_Opt.scala', 'w') as f:

        for line in template:
            
            if "val infile_X" in line:
                replicate_line_alphabet(f, line, num_mults*2)
            
            elif "writeCSVNow(" in line:
                replicate_line_alphabet(f, line, num_mults*2)
            
            elif "val stream_X_in" in line:
                replicate_line_alphabet(f, line, num_mults*2)
            
            elif "// MULT X FIFOS" in line:
                operands_fifos(f, line, num_mults)

            elif "val X = stream_X_in.value" in line:
                replicate_line_alphabet(f, line, num_mults*2)
            
            elif "val result_X = 0" in line:
                replicate_line_number(f, line, num_mults)
            
            elif "val X = X_stageY.deq()" in line:
                replicate_second_fifo_line(f, line, num_mults*2)
            
            elif "val result_X = result_X_stageY.deq()" in line:
                replicate_result_line(f, line, num_mults)
                iteration += 1

            elif "result_X_stageY.enq(mux" in line:
                replicate_result_line(f, line, num_mults)
            
            elif "val flag_X = (X" in line:
                replicate_flag_line(f, line, num_mults)
            
            elif "X_stageY.enq(mux" in line:
                replicate_first_fifo_line(f, line, num_mults)
            
            elif "X_stageY.enq(X >> 1)" in line:
                replicate_queue_fifo_line(f, line, num_mults)

            elif "// STREAM OUT" in line:
                stream_out_line(f, line, num_mults)

            else:
                f.write(line)