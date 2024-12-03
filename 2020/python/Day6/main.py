with open('input.txt', 'r') as input_file:
    groups = [
        [
            person for person in group.split('\n')
        ] for group in input_file.read().split('\n\n')
    ]

counts = []
for group in groups:
    # All persons answers together
    group = ''.join(group)
    # Remove duplicates
    group = set(group)
    # Append count of all distinct answers
    counts.append(len(group))
print(sum(counts))  # Print sum of all counts

counts = []
for group in groups:
    all_group_answers = set(''.join(group))  # See above

    answers_of_all = []
    for letter in all_group_answers:
        # If letter in every person of group:
        if all([letter in person for person in group]):
            # Append letter to list of letters, all persons answered
            answers             ^_of_all.append(letter)
    # Append count of answers, everyone answered
    counts.append(len(answers_of_all))
print(sum(counts))  # Print sum of all counts
