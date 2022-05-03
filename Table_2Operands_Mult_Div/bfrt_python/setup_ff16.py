from netaddr import IPAddress

p4 = bfrt.ff_mult_table.pipe

# This function can clear all the tables and later on other fixed objects
# once bfrt support is added.
def clear_all(verbose=True, batching=True):
    global p4
    global bfrt
    
    # The order is important. We do want to clear from the top, i.e.
    # delete objects that use other objects, e.g. table entries use
    # selector groups and selector groups use action profile members

    for table_types in (['MATCH_DIRECT', 'MATCH_INDIRECT_SELECTOR'],
                        ['SELECTOR'],
                        ['ACTION_PROFILE']):
        for table in p4.info(return_info=True, print_info=False):
            if table['type'] in table_types:
                if verbose:
                    print("Clearing table {:<40} ... ".
                          format(table['full_name']), end='', flush=True)
                table['node'].clear(batch=batching)
                if verbose:
                    print('Done')
                    
#clear_all(verbose=True)

table_forward = p4.Ingress.table_forward
table_forward.add_with_send(port_num=3,   port=3)

# TODO: Add GF(2^16) tables

bfrt.complete_operations()

# Final programming
print("""
******************* PROGAMMING RESULTS *****************
""")
print ("Table forward")
table_forward.dump(table=True)
# print ("Table log_a:")
# table_log_a.dump(table=True)
# print ("Table log_b:")
# table_log_b.dump(table=True)
# print ("Table antilog:")
# table_antilog.dump(table=True)

                       
