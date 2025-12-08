package de.rubixdev.aoc

enum class Tile {
    WALL,
    BOX,
    FREE,
    ROBOT,
}

enum class WideTile {
    WALL,
    BOX_LEFT,
    BOX_RIGHT,
    FREE,
    ROBOT,
}

fun day15(input: String): Day = sequence {
    val example = """
##########
#..O..O.O#
#......O.#
#.OO..O.O#
#..O@..O.#
#O#..O...#
#O..O..O.#
#.OO.O.OO#
#....O...#
##########

<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
    """.trimIndent().trim()
    // val (rawMap, rawMovements) = example.split("\n\n")
    val (rawMap, rawMovements) = input.split("\n\n")
    val map = rawMap.lines().map { line ->
        line.map { tile ->
            when (tile) {
                '#' -> Tile.WALL
                'O' -> Tile.BOX
                '.' -> Tile.FREE
                '@' -> Tile.ROBOT
                else -> throw RuntimeException("malformed input")
            }
        }
    }
    val movements = rawMovements.mapNotNull { it.toDirection() }

    yield(null)
    yield(part1(map, movements))
    yield(part2(map, movements))
}

private fun tryMove(pos: Vec2, direction: Direction, map: List<MutableList<Tile>>): Boolean {
    val movePos = pos + direction.vec
    if (!movePos.isInBounds(map)) return false
    return when (map[movePos]) {
        Tile.WALL -> false
        Tile.FREE -> true
        Tile.ROBOT -> throw IllegalStateException("trying to move onto robot")
        Tile.BOX -> tryMove(movePos, direction, map)
    }.also {
        if (it) {
            map[movePos] = map[pos]
            map[pos] = Tile.FREE
        }
    }
}

private fun part1(map: List<List<Tile>>, movements: List<Direction>): Long {
    val mapCopy = map.map { it.toMutableList() }
    var robotPos = mapCopy.findPos { it == Tile.ROBOT }
    for (direction in movements) {
        if (tryMove(robotPos, direction, mapCopy)) {
            robotPos += direction.vec
        }
    }
    return mapCopy.withIndex().sumOf { (y, line) ->
        line.withIndex().sumOf { (x, tile) ->
            if (tile == Tile.BOX) {
                x + 100L * y
            } else {
                0L
            }
        }
    }
}

private fun expandMap(map: List<List<Tile>>): List<List<WideTile>> = map.map { line ->
    line.flatMap { tile ->
        when (tile) {
            Tile.WALL -> listOf(WideTile.WALL, WideTile.WALL)
            Tile.BOX -> listOf(WideTile.BOX_LEFT, WideTile.BOX_RIGHT)
            Tile.FREE -> listOf(WideTile.FREE, WideTile.FREE)
            Tile.ROBOT -> listOf(WideTile.ROBOT, WideTile.FREE)
        }
    }
}

private fun canMove(pos: Vec2, direction: Direction, map: List<MutableList<WideTile>>): Boolean {
    val movePos = pos + direction.vec
    if (!movePos.isInBounds(map)) return false
    return when (map[movePos]) {
        WideTile.WALL -> false

        WideTile.FREE -> true

        WideTile.ROBOT -> throw IllegalStateException("trying to move onto robot")

        WideTile.BOX_LEFT -> canMove(movePos, direction, map) &&
            (direction.isHorizontal || canMove(movePos + Direction.RIGHT.vec, direction, map))

        WideTile.BOX_RIGHT -> canMove(movePos, direction, map) &&
            (direction.isHorizontal || canMove(movePos + Direction.LEFT.vec, direction, map))
    }
}

private fun move(pos: Vec2, direction: Direction, map: List<MutableList<WideTile>>) {
    val movePos = pos + direction.vec
    when (map[movePos]) {
        WideTile.ROBOT -> throw IllegalStateException("trying to move onto robot")

        WideTile.BOX_LEFT -> {
            move(movePos, direction, map)
            if (!direction.isHorizontal) move(movePos + Direction.RIGHT.vec, direction, map)
        }

        WideTile.BOX_RIGHT -> {
            move(movePos, direction, map)
            if (!direction.isHorizontal) move(movePos + Direction.LEFT.vec, direction, map)
        }

        else -> {}
    }
    map[movePos] = map[pos]
    map[pos] = WideTile.FREE
}

private fun part2(map: List<List<Tile>>, movements: List<Direction>): Long {
    val mapCopy = expandMap(map).map { it.toMutableList() }
    var robotPos = mapCopy.findPos { it == WideTile.ROBOT }
    for (direction in movements) {
        if (canMove(robotPos, direction, mapCopy)) {
            move(robotPos, direction, mapCopy)
            robotPos += direction.vec
        }
    }
    return mapCopy.withIndex().sumOf { (y, line) ->
        line.withIndex().sumOf { (x, tile) ->
            if (tile == WideTile.BOX_LEFT) {
                x + 100L * y
            } else {
                0L
            }
        }
    }
}
