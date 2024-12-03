import time

time1 = time.time()

def asdf():
    with open('input.txt', 'r') as input_file:
        input_data = [int(i) for i in input_file.read().split('\n')]

    for count1, z1 in enumerate(input_data):
        for z2 in input_data[count1:]:
            if z1 + z2 == 2020:
                print(f'{z1} + {z2} = {z1 + z2}\n'
                      f'{z1} * {z2} = {z1 * z2}\n')

    for count1, z1 in enumerate(input_data):
        for count2, z2 in enumerate(input_data[count1:]):
            for z3 in input_data[count2:]:
                if z1 + z2 + z3 == 2020:
                    print(f'{z1} + {z2} + {z3} = {z1 + z2 + z3}\n'
                          f'{z1} * {z2} * {z3} = {z1 * z2 * z3}\n')

for a in range(1):
    asdf()

print('Execution took ' + str(round((time.time() - time1) * 1000, 6)) + 'ms')

