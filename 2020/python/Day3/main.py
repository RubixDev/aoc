with open('input.txt', 'r') as input_file:
    input_map = [
        [
            field == '#' for field in line
        ] for line in input_file.read().split('\n')
    ]

pos = 0
count = 0
for line in input_map:
    if line[pos % len(line)]:
        count += 1
    pos += 3
print(count)

print('---')

counts = []
for slope in [[1, 1], [3, 1], [5, 1], [7, 1], [1, 2]]:
    pos = 0
    count = 0
    for line_num, line in enumerate(input_map):
        if line_num % slope[1] != 0:
            continue
        if line[pos % len(line)]:
            count += 1
        pos += slope[0]
    counts.append(count)
    print(count)
print(eval('*'.join([str(i) for i in counts])))
