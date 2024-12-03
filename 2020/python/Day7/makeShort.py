with open(input('What file do you want to press in one line?\n'), 'r') as input_file:
    org_code = 'exec("' + input_file.read()\
        .replace(r'\n', '||/n/n||')\
        .replace('\n', r'\n')\
        .replace('||/n/n||', r'\\n') + '")'
print('Writing following code to out.py:\n' + org_code)
with open('out.py', 'w') as new_file:
    new_file.write(org_code)
print('Done')
