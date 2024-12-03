with open('input.txt', 'r') as input_file:
    passports = [
        {
            i.split(':')[0]: i.split(':')[1] for i in passport.replace('\n', ' ').split(' ')
        } for passport in input_file.read().split('\n\n')
    ]

count = 0
required_fields = ('byr', 'iyr', 'eyr', 'hgt', 'hcl', 'ecl', 'pid')
for passport in passports:
    if all([field in passport for field in required_fields]):
        count += 1
print(count)

count = 0
required_fields = ('byr', 'iyr', 'eyr', 'hgt', 'hcl', 'ecl', 'pid')
for passport in passports:
    if not all([field in passport for field in required_fields]):
        continue
    if all([
        len(passport['byr']) == 4 and (1920 <= int(passport['byr']) <= 2002),
        len(passport['iyr']) == 4 and (2010 <= int(passport['iyr']) <= 2020),
        len(passport['eyr']) == 4 and (2020 <= int(passport['eyr']) <= 2030),
        passport['hgt'][-2:] in ('cm', 'in') and (
                (150 <= int(passport['hgt'][:-2]) <= 193) if passport['hgt'][-2:] == 'cm' else (
                        59 <= int(passport['hgt'][:-2]) <= 76)
        ),
        passport['hcl'][0] == '#' and len(passport['hcl']) == 7 and all(
            [i in list('0123456789abcdef') for i in passport['hcl'][1:]]
        ),
        passport['ecl'] in ('amb', 'blu', 'brn', 'gry', 'grn', 'hzl', 'oth'),
        len(passport['pid']) == 9 and all([i in list('0123456789') for i in passport['pid']])
    ]):
        count += 1
print(count)
