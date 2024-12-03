with open('input.txt', 'r') as input_file: instructions = [[i.split(' ')[0], int(i.split(' ')[1])] for i in input_file.read().split('\n')]
def run_instructions(insts):
    done_pos, pos, accumulator = ([], 0, 0)
    while pos not in done_pos and pos < len(insts):
        done_pos.append(pos); pos += 1
        if insts[pos][0] == 'acc':
            accumulator += insts[pos][1]
            pos += 1
        elif insts[pos][0] == 'jmp':
            pos += insts[pos][1] - 1
    return accumulator, pos == len(insts)
print(run_instructions(instructions)[0], [run_instructions(poss_inst)[0] for poss_inst in [[(i[0] if c != pos else ('nop' if i[0] == 'jmp' else 'jmp'), i[1]) for c, i in enumerate(instructions)] for pos, inst in [(p, i) for p, i in enumerate(instructions) if i[0] != 'acc']] if run_instructions(poss_inst)[1]][0], sep='\n')
