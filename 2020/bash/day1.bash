#!/bin/bash

time1=$(date +%s.%N)

readarray -t input < input.txt

part1 () {
  for num1 in "${input[@]}"; do
    for num2 in "${input[@]}"; do
      if [[ $(( num1 + num2 )) -eq 2020 ]]; then
        echo "$num1 + $num2 = $(( num1 + num2 ))"
        echo "$num1 * $num2 = $(( num1 * num2 ))"
        return 0
      fi
    done
  done
}

part2 () {
  for num1 in "${input[@]}"; do
    for num2 in "${input[@]}"; do
      for num3 in "${input[@]}"; do
        if [[ $(( num1 + num2 + num3 )) -eq 2020 ]]; then
          echo "$num1 + $num2 + $num3 = $(( num1 + num2 + num3 ))"
          echo "$num1 * $num2 * $num3 = $(( num1 * num2 * num3 ))"
          return 0
        fi
      done
    done
  done
}

part1
part2

time2=$(date +%s.%N)
runtime="$(echo "($time2 - $time1) * 1000" | bc -l)"
echo "Execution took ${runtime:0:-3}ms"

