with open('input.txt', 'r') as input_file:
    bag_rules = {
        i.split(' bag contain ')[0]: [
            (int(n[0]), n[2:]) for n in ''.join(
                i.split(' bag contain ')[1:]
            ).split(' bag, ')[:-1] if 'no ' not in n
        ] for i in input_file.read().replace('bags', 'bag').replace('.', ', ').split('\n')
    }

in_bags = {bag: [k for k, v in bag_rules.items() if bag in [i[1] for i in v]] for bag in bag_rules.keys()}


def find_bag(bag: str = 'shiny gold') -> list:
    return in_bags[bag] + [i for s in [find_bag(b) for b in in_bags[bag]] for i in s]


print(len(set(find_bag())))


def get_num_of_bags(bag: str = 'shiny gold') -> str:
    return '+'.join([f'{b[0]}+{b[0]}*({get_num_of_bags(b[1])})' for b in bag_rules[bag]]).replace('()', '0')


print(eval(get_num_of_bags()))
