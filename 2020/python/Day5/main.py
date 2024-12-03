with open('input.txt', 'r') as input_file:
    seat_codes = input_file.read()\
        .replace('F', '0')\
        .replace('B', '1')\
        .replace('L', '0')\
        .replace('R', '1')\
        .split('\n')

seat_ids = []
for seat_code in seat_codes:
    row = int(seat_code[:7], 2)
    column = int(seat_code[7:], 2)
    seat_ids.append(row * 8 + column)
print(max(seat_ids))

for seat_id in seat_ids:
    if (seat_id + 1 not in seat_ids) and (seat_id + 2 in seat_ids):
        print(seat_id + 1)
        break
