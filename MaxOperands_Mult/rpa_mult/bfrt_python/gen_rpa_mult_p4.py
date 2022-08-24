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

def action_ff_mult_fill(f, line, times):
    for i in range(times):
        element_line = line.replace('X', str(i))
        f.write(element_line)
        f.write("\t\thigh_bit_f_" + str(i) + " = hdr.ff_calc." + alphabet[2*i] + " & HIGH_BIT_MASK;\n\n")
        f.write("\t\thdr.ff_calc." + alphabet[2*i] + " = hdr.ff_calc." + alphabet[2*i] + " << 1;\n")
        f.write("\t\thdr.ff_calc." + alphabet[2*i+1] + " = hdr.ff_calc." + alphabet[2*i+1] + " >> 1;\n\n")

def low_bit_iteration_flag(f, line, times):
    for i in range(times):
        tmpline = line.replace('X', str(i))
        tmpline2 = tmpline.replace('Y', alphabet[2*i+1])
        f.write(tmpline2)

def low_bit_if(f, times):
    for i in range(times):
        f.write("\t\tif (low_bit_f_" + str(i) + " != 0)\n")
        f.write("\t\t\thdr.ff_calc.result_" + str(i) + " = hdr.ff_calc.result_" + str(i) + " ^ hdr.ff_calc." + alphabet[2*i] + ";\n\n")

def high_bit_if(f, times):
    for i in range(times):
        f.write("\t\tif (high_bit_f_" + str(i) + " != 0)\n")
        f.write("\t\t\thdr.ff_calc." + alphabet[2*i] + " = hdr.ff_calc." + alphabet[2*i] + " ^ IRRED_POLY;\n\n")

with open('template_rpa_mult.p4', 'r') as template:
    with open('../p4src/rpa_mult_max_ops.p4', 'w') as f:

        for line in template:

            if "bit<8> X;" in line:
                replicate_line_alphabet(f, line, num_mults*2)

            elif "bit<8> result_X;" in line:
                replicate_line_number(f, line, num_mults)
            
            elif "bit<8> high_bit_f_X" in line:
                replicate_line_number(f, line, num_mults)

            elif "bit<8> low_bit_f_X;" in line:
                replicate_line_number(f, line, num_mults)
            
            elif "// ELEMENT X" in line:
                action_ff_mult_fill(f, line, num_mults)

            elif "low_bit_f_X = hdr.ff_calc.Y" in line:
                low_bit_iteration_flag(f, line, num_mults)

            elif "// If low bit here" in line:
                f.write(line)
                low_bit_if(f, num_mults)

            elif "// If high bit here" in line:
                f.write(line)
                high_bit_if(f, num_mults)

            else:
                f.write(line)

