package de.rubixdev;

fun main(){val i=java.io.File("3").readText()
println(Regex("""mul\((\d{1,3}),(\d{1,3})\)""").findAll(i).sumOf{val(a,b)=it.destructured
a.toInt()*b.toInt()})
var o=true
var s=0
Regex("""mul\((\d{1,3}),(\d{1,3})\)|do(n't)?\(\)""").findAll(i).forEach{when(it.value){"do()"->o=true"don't()"->o=false else->if(o){val(a,b)=it.destructured
s+=a.toInt()*b.toInt()}}}
println(s)}
