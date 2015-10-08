package models.protocol

import akka.actor.ActorRef
import models._

case class GameState(objectStates: Map[ObjectId, PlayerState])
case class GameUpdates(objectStates: Map[ObjectId, PlayerState])

case class InitPlayer(state: PlayerState, players: Seq[PlayerState])
case class PlayerJoined(state: PlayerState)
case class PlayerLeft(state: PlayerState)

case object JoinGame
case object LeaveGame

case class SetWalls(walls: Seq[Wall])

case object GetVisibleEnemies

case class VisibleEnemies(enemies: Map[ActorRef, PlayerState])
