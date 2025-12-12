package de.rubixdev.aoc

fun <T> Boolean.then(f: () -> T): T? = when (this) {
    true -> f()
    false -> null
}
