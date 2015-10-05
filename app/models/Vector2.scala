package models

case class Vector2(x: Double, y: Double) {
  def scale(mult : Double) : Vector2 = Vector2(x * mult, y * mult)

  def rotate(angle: Double) : Vector2 = Vector2 (this.x*math.cos(angle) - y* math.sin(angle), x*math.sin(angle) + y*math.cos(angle))

  def rotateCw(angle: Double) : Vector2 = rotate(-angle) 

  def add(other: Vector2) : Vector2 = Vector2(x + other.x, y + other.y)

  def length : Double = math.sqrt(x * x + y * y)

  def angleTo (other: Vector2) : Double = {
    val denom = length * other.length
    val sin = (x * other.y - y * other.x) / denom
    val cos = (x * other.x + y * other.y) / denom

    val angle = math.asin(sin)
    if (cos > 0) angle else math.Pi - angle
  }

  def * (other: Vector2) : Double = x * other.x + y * other.y
}
