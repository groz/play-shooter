package models

case class Vector2(x: Double, y: Double) {
  def scale(mult : Double) : Vector2 = Vector2(x * mult, y * mult)

  def rotate(angle: Double) : Vector2 = ???

  def add(other: Vector2) : Vector2 = Vector2(x + other.x, y + other.y)

  def length : Double = math.sqrt(x * x + y * y)

  def angleTo (other: Vector2) : Double = ???

  def * (other: Vector2) : Double = x * other.x + y * other.y
}
