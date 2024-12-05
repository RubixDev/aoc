package de.rubixdev

fun main(){val(i,j)=java.io.File("i").readLines().map{it.split("   ").map{it.toInt()}.let{it[0]to it[1]}}.unzip()
println(i.sorted().zip(j.sorted()).sumOf{(a,b)->kotlin.math.abs(a-b)})
println(i.sumOf{n->n*j.count{it==n}})}
