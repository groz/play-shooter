package models.protocol

import models.{PlayerState, ObjectId}

case class GameState(objectStates: Map[ObjectId, PlayerState])
case class GameUpdates(objectStates: Map[ObjectId, PlayerState])

case class InitPlayer(state: PlayerState)
case class PlayerJoined(state: PlayerState)

case object JoinGame
case object LeaveGame

case class SetWalls(walls: Seq[Wall])
