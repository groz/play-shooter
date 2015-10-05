package models

case class Vector2(x: Double, y: Double) {
  def scale(x : Double) : Vector2 = ???

  def rotate(angle: Double) : Vector2 = ???

  def add(other: Vector2) : Vector2 = ???

  def length : Double = ???

  def angleTo (other: Vector2) : Double = ???
  
  def * (other: Vector2) : Double = ???
}
