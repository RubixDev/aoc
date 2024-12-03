with open('input.txt', 'r') as input_file:
    input_list = [
        [
            [
                int(n) for n in i.split(' ')[0].split('-')
            ],
            i.split(' ')[1][0], i.split(' ')[2]
        ] for i in input_file.read().split('\n')
    ]


def is_valid(pwd_list: list) -> bool:
    if (pwd_list[2].count(pwd_list[1]) < pwd_list[0][0]) \
            or (pwd_list[2].count(pwd_list[1]) > pwd_list[0][1]):
        return False
    return True


def is_valid_new(pwd_list: list) -> bool:
    if (pwd_list[2][pwd_list[0][0] - 1] == pwd_list[1]) \
            ^ (pwd_list[2][pwd_list[0][1] - 1] == pwd_list[1]):
        return True
    return False


count = 0
for password in input_list:
    if is_valid(password):
        count += 1

print(count)

count = 0
for password in input_list:
    if is_valid_new(password):
        count += 1

print(count)
