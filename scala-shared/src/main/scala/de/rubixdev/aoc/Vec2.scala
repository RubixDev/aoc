package de.rubixdev.aoc

case class Vec2(x: Long, y: Long) {
  def this(v: Long) = this(v, v)

  def +(other: Vec2): Vec2 = (x + other.x) by (y + other.y)
  def -(other: Vec2): Vec2 = (x - other.x) by (y - other.y)
  def *(scalar: Long): Vec2 = (x * scalar) by (y * scalar)
  def *(scalar: Int): Vec2 = (x * scalar) by (y * scalar)
  def /(scalar: Long): Vec2 = (x / scalar) by (y / scalar)
  def /(scalar: Int): Vec2 = (x / scalar) by (y / scalar)

  def move(direction: Direction): Vec2 = this + direction.vec
}

extension (x: Int) {
  infix def by(y: Int) = Vec2(x.toLong, y.toLong)
  def *(vec: Vec2): Vec2 = vec * x
}

extension (x: Long) {
  infix def by(y: Long) = Vec2(x, y)
  def *(vec: Vec2): Vec2 = vec * x
}

enum Direction:
  case Up, Down, Left, Right
end Direction

extension (dir: Direction) {
  def vec: Vec2 = dir match {
    case Direction.Up    => 0 by -1
    case Direction.Down  => 0 by 1
    case Direction.Left  => -1 by 0
    case Direction.Right => 1 by 0
  }

  def isHorizontal: Boolean = dir match {
    case Direction.Up | Direction.Down    => false
    case Direction.Left | Direction.Right => true
  }

  def rotateLeft: Direction = dir match {
    case Direction.Up    => Direction.Left
    case Direction.Down  => Direction.Right
    case Direction.Left  => Direction.Down
    case Direction.Right => Direction.Up
  }

  def rotateRight: Direction = dir match {
    case Direction.Up    => Direction.Right
    case Direction.Down  => Direction.Left
    case Direction.Left  => Direction.Up
    case Direction.Right => Direction.Down
  }
}

val ADJACENT = Direction.values.map(_.vec) ++ List(
  Direction.Up.vec + Direction.Left.vec,
  Direction.Up.vec + Direction.Right.vec,
  Direction.Down.vec + Direction.Left.vec,
  Direction.Down.vec + Direction.Right.vec,
)

extension [T](map: Seq[Seq[T]]) {
  def apply(index: Vec2): T = map(index.y.toInt)(index.x.toInt)
  def lift2d(index: Vec2): Option[T] =
    map.lift(index.y.toInt).flatMap(_.lift(index.x.toInt))
}
