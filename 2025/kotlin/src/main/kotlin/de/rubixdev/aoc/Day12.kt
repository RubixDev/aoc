package de.rubixdev.aoc

fun day12(input: String): Day = sequence {
    val example = """
        0:
        ###
        ##.
        ##.

        1:
        ###
        ##.
        .##

        2:
        .##
        ###
        ##.

        3:
        ##.
        ###
        ##.

        4:
        ###
        #..
        ###

        5:
        ###
        .#.
        ###

        4x4: 0 0 0 0 2 0
        12x5: 1 0 1 0 2 2
        12x5: 1 0 1 0 3 2
    """.trimIndent()
//    val presents = example.split("\n\n")
    val presents = input.split("\n\n")
        .dropLast(1)
        .map { present ->
            present.lines().drop(1).map { line ->
                line.map { it == '#' }
            }
        }
//    val regions = example.split("\n\n")
    val regions = input.split("\n\n")
        .last()
        .lines()
        .map { line -> line.split(": ") }
        .map { (size, counts) ->
            size.split('x').map { it.toInt() }.toVec2() to
                counts.split(' ').map { it.toInt() }
        }

    yield(null)
    yield(part1(presents, regions))
    yield("North Pole decorated :)")
}

private fun part1(presents: List<List<List<Boolean>>>, regions: List<Pair<Vec2, List<Int>>>): Int {
    fun List<Boolean>.bitmask(): ULong = fold(0.toULong()) { bits, bit ->
        (bits shl 1) or bit.toLong().toULong()
    }

    val allPresents = presents.map { present ->
        setOf(
            present.map { it.bitmask() }, // normal
            present.transpose().map { it.asReversed().bitmask() }, // rotated 90 deg
            present.asReversed().map { it.asReversed().bitmask() }, // rotated 180 deg
            present.asReversed().transpose().map { it.bitmask() }, // rotated 270 deg
            present.map { it.asReversed().bitmask() }, // flipped horizontally
            present.asReversed().map { it.bitmask() }, // flipped vertically
            // hopefully that's all
        )
    }

    fun List<ULong>.addPresent(present: List<ULong>, at: Vec2): List<ULong> =
        mapIndexed { y, line ->
            when (y in at.y.toInt()..<at.y.toInt() + present.size) {
                true -> line or (present[y - at.y.toInt()] shl at.x.toInt())
                false -> line
            }
        }

    return regions.count { region ->
        fun List<ULong>.findFitting(presentIdx: Int): Sequence<Pair<Vec2, List<ULong>>> {
            val presentSize = presents[presentIdx]
            val presentVariants = allPresents[presentIdx]
            return presentVariants.asSequence().flatMap { present ->
                (0..(region.first.y - presentSize.size)).asSequence().flatMap { yOffset ->
                    (0..(region.first.x - presentSize.first().size)).asSequence()
                        .mapNotNull { xOffset ->
                            drop(yOffset.toInt())
                                .zip(present)
                                .all { it.first and (it.second shl xOffset.toInt()) == 0.toULong() }
                                .then { xOffset by yOffset to present }
                        }
                }
            }
        }

        fun canFitAll(map: List<ULong>, restPresents: List<Int>): Boolean =
            restPresents.isEmpty() || map.findFitting(restPresents.last()).any { (pos, present) ->
                canFitAll(map.addPresent(present, pos), restPresents.dropLast(1))
            }

        val triviallyFits = region.first.map { it / 3 }.area() >= region.second.sum()
        val totalPresentArea = region.second.withIndex()
            .sumOf { (idx, count) -> presents[idx].flatten().count { it } * count }

        // the puzzle input never actually requires the brute-force search, or
        // else it wouldn't complete quickly enough
        triviallyFits || (
            totalPresentArea <= region.first.area() &&
                canFitAll(
                    // y lines with x 0s ("free spaces") at the end
                    (0..<region.first.y).map { ULong.MAX_VALUE shl region.first.x.toInt() },
                    region.second.flatMapIndexed { idx, count -> listOf(idx).repeat(count) },
                )
            )
    }
}
