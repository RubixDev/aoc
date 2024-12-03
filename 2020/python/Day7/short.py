exec("with open('input.txt', 'r') as input_file:\n    bag_rules = {\n        i.split(' bag contain ')[0]: [\n            (int(n[0]), n[2:]) for n in ''.join(\n                i.split(' bag contain ')[1:]\n            ).split(' bag, ')[:-1] if 'no ' not in n\n        ] for i in input_file.read().replace('bags', 'bag').replace('.', ', ').split('\\n')\n    }\n\nin_bags = {bag: [k for k, v in bag_rules.items() if bag in [i[1] for i in v]] for bag in bag_rules.keys()}\n\n\ndef find_bag(bag: str = 'shiny gold') -> list:\n    return in_bags[bag] + [i for s in [find_bag(b) for b in in_bags[bag]] for i in s]\n\n\nprint(len(set(find_bag())))\n\n\ndef get_num_of_bags(bag: str = 'shiny gold') -> str:\n    return '+'.join([f'{b[0]}+{b[0]}*({get_num_of_bags(b[1])})' for b in bag_rules[bag]]).replace('()', '0')\n\n\nprint(eval(get_num_of_bags()))\n")