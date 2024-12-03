with open('input.txt', 'r') as input_file:
    instructions = [[i.split(' ')[0], int(i.split(' ')[1])] for i in input_file.read().split('\n')]


def run_instructions(insts=None):
    if insts is None:
        insts = instructions
    done_pos = []
    pos = 0
    accumulator = 0
    while pos not in done_pos and pos < len(insts):
        inst = insts[pos]
        done_pos.append(pos)

        if inst[0] == 'acc':
            accumulator += inst[1]
            pos += 1
        elif inst[0] == 'jmp':
            pos += inst[1]
        else:
            pos += 1
    return accumulator, pos == len(insts)


print(run_instructions()[0])


poss_insts = [
    [
        (
            i[0] if c != pos else ('nop' if i[0] == 'jmp' else 'jmp'),
            i[1]
        ) for c, i in enumerate(instructions)
    ] for pos, inst in [
        (p, i) for p, i in enumerate(instructions) if i[0] != 'acc'
    ]
]
print([run_instructions(poss_inst)[0] for poss_inst in poss_insts if run_instructions(poss_inst)[1]][0])
