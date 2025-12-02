package de.rubixdev.aoc

fun main(){val(i,j)=java.io.File("i").readLines().map{it.split("   ").map{it.toInt()}.let{it[0]to it[1]}}.unzip()
print("${i.sorted().zip(j.sorted()).sumOf{(a,b)->kotlin.math.abs(a-b)}} "+i.sumOf{n->n*j.count{it==n}})}
