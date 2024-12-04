with open('input.txt', 'r') as input_file:
    xmas_data = [int(i) for i in input_file.read().split('\n')]

num = 0

for index, num in enumerate(xmas_data[25:]):
    if not any([num == sum(i) for i in set(
            [e for s in [
                x for x in [
                    [
                        (z1, z2) for z2 in xmas_data[index + c:index + 25] if z1 != z2
                    ] for c, z1 in enumerate(xmas_data[index:index + 25])] if x
            ] for e in s]
    )]):
        print(num)
        break

all_sums = set(
    [
        e for s in [
            [
                (
                    sum(xmas_data[ps:ps + pe]),
                    max(xmas_data[ps:ps + pe], default=0) + min(xmas_data[ps:ps + pe], default=0)
                ) for pe in range(len(xmas_data[ps:]))
            ] for ps in range(len(xmas_data))
        ] for e in s
    ]
)
print([i for i in all_sums if num in i])
