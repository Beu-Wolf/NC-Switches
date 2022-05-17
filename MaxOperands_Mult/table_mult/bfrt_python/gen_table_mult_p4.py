import sys
import string 

num_mults = int(sys.argv[1])
alphabet = list(string.ascii_lowercase) + [letter1+letter2 for letter1 in string.ascii_lowercase for letter2 in string.ascii_lowercase]

constant_table_lines = ["\t\t#if defined(FF_8)\n", "\t\tsize = 256;\n", "\t\t#elif defined(FF_16)\n", "\t\tsize = 65536;\n", "\t\t#endif\n"]

def replicate_line_alphabet(f, line, times):
    for i in range(times):
        tmpline = line.replace('X', alphabet[i])
        f.write(tmpline)

def replicate_line_number(f, line, times):
    for i in range(times):
        tmpline = line.replace('X', str(i))
        f.write(tmpline)

def write_log_actions(f, times):
    for i in range(times):
        f.write("\taction get_log_" + alphabet[i] + "(bit<32> log_value) {\n")
        f.write("\t\tlog_" + alphabet[i] + " = log_value;\n")
        f.write("\t}\n\n")

def write_antilog_actions(f, times):
    for i in range(times):
        f.write("\taction get_antilog_" + str(i) + "(bit<32> antilog_value) {\n")
        f.write("\t\thdr.ff_calc.result_" + str(i) + " = antilog_value;\n")
        f.write("\t}\n\n")

def write_log_tables(f, times):
    for i in range(times):
        f.write("\ttable table_log_" + alphabet[i] + " {\n")
        f.write("\t\t key = {\n")
        f.write("\t\t\thdr.ff_calc." + alphabet[i] + ": exact;\n")
        f.write("\t\t}\n")
        f.write("\t\tactions = {\n")
        f.write("\t\t\t get_log_" + alphabet[i] + ";\n")
        f.write("\t\t}\n")
        f.writelines(constant_table_lines)
        f.write("\t}\n\n")

def write_antilog_tables(f, times):
    for i in range(times):
        f.write("\ttable table_antilog_" + str(i) + " {\n")
        f.write("\t\tkey = {\n")
        f.write("\t\t\tmeta.tmp_sum_value_" + str(i) + ": exact;\n")
        f.write("\t\t}\n")
        f.write("\t\tactions = {\n")
        f.write("\t\t\t get_antilog_" + str(i) + ";\n")
        f.write("\t\t}\n")
        f.writelines(constant_table_lines) 
        f.write("\t}\n\n")   
 

def write_sum_vals(f, line, times):
    for i in range(times):
        tmpline = line.replace('X', str(i))
        tmpline2 = tmpline.replace('Y', alphabet[i*2])
        tmpline3 = tmpline2.replace('Z', alphabet[i*2+1])
        f.write(tmpline3)

def write_sum_if(f, times):
    for i in range(times):
        f.write("\t\tif(meta.tmp_sum_value_" + str(i) + " == MAX_SIZE || meta.tmp_sum_value_" + str(i) + " & BIT_MASK != 0) {\n")
        f.write("\t\t\tsub_max(meta.tmp_sum_value_" + str(i) + ");\n")
        f.write("\t\t}\n\n")


with open('template_table_mult.p4', 'r') as template:
    with open('../p4src/ff_mult_table_max_ops.p4', 'w') as f:

        for line in template:

            if "field_size_t X" in line:
                replicate_line_alphabet(f, line, num_mults*2)
            
            elif "result_X" in line:
                replicate_line_number(f, line, num_mults)
            
            elif "tmp_sum_value_X;" in line:
                replicate_line_number(f, line, num_mults)
            
            elif "meta.tmp_sum_value_X = 0" in line:
                replicate_line_number(f, line, num_mults)

            elif "bit<32> log_X" in line:
                replicate_line_alphabet(f, line, num_mults*2)
            
            elif "Action log here" in line:
                f.write(line)
                write_log_actions(f, num_mults*2)
            
            elif "Action antilog here" in line:
                f.write(line)
                write_antilog_actions(f, num_mults)

            elif "Tables log here" in line:
                f.write(line)
                write_log_tables(f, num_mults*2)

            elif "Tables antilog here" in line:
                f.write(line)
                write_antilog_tables(f, num_mults)

            elif "table_log_X.apply" in line:
                replicate_line_alphabet(f, line, num_mults*2)
            
            elif "sum_vals(meta.tmp" in line:
                write_sum_vals(f, line, num_mults)
            
            elif "MAX SIZE if" in line:
                write_sum_if(f, num_mults)
            
            elif "table_antilog_X.apply" in line:
                replicate_line_number(f, line, num_mults)
            
            else:
                f.write(line)

