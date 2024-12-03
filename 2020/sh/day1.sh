#!/bin/sh

time1=$(date +%s.%N)

part1 () {
  while read num1; do
    while read num2; do
      if [ $(( num1 + num2 )) -eq 2020 ]; then
        echo "$num1 + $num2 = $(( num1 + num2 ))"
        echo "$num1 * $num2 = $(( num1 * num2 ))"
        return 0
      fi
    done < input.txt
  done < input.txt
}

part2 () {
  while read num1; do
    while read num2; do
      while read num3; do
        if [ $(( num1 + num2 + num3 )) -eq 2020 ]; then
          echo "$num1 + $num2 + $num3 = $(( num1 + num2 + num3 ))"
          echo "$num1 * $num2 * $num3 = $(( num1 * num2 * num3 ))"
          return 0
        fi
      done < input.txt
    done < input.txt
  done < input.txt
}

part1
part2

time2=$(date +%s.%N)
runtime="$(echo "($time2 - $time1) * 1000" | bc -l)"
echo "Execution took ${runtime}ms"

