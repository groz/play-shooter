package models

case class PlayerState(id: ObjectId, position: Vector2, direction: Vector2, health: Int) {

  def alive = health > 0

}
