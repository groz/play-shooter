package models

import akka.actor._
import models.protocol._

import scala.util.Random

class Game extends Actor {
  import Game._

  val rng = new Random()

  override def receive: Receive = process(Map.empty, Seq.empty)

  def process(players: Map[ActorRef, PlayerState], walls: Seq[Wall]): Receive = {

    case Reposition(v: Vector2) =>
      val state: PlayerState = players(sender)
      val newState = state.copy(position = v)
      println(s"Reposition $state to $v ...")
      for ((p, _) <- (players - sender)) {
        p ! PlayerReposition(newState)
      }
      context become process(players.updated(sender, newState), walls)

    case JoinGame =>
      val playerState = genPlayerState
      sender ! InitPlayer(playerState, players.values.toSeq)
      println(s"Player $playerState joined...")

      for ((p, state) <- players) {
        p ! PlayerJoined(playerState)
      }

      context become process(players + (sender -> playerState), walls)

    case LeaveGame =>
      println(s"Player $sender left.")
      val leftPlayerState = players(sender)

      for ((p, state) <- players - sender) {
        p ! PlayerLeft(leftPlayerState)
      }

      context become process(players - sender, walls)

    case SetWalls(w: Seq[Wall]) =>
      println(s"New walls has been set!")
      context become process(players, w)

    case GetVisibleEnemies =>
      val myPosition = players(sender).position
      val enemies = players - sender

      val visibleEnemies =
        for {
          (enemy, state) <- enemies
          if walls.forall(w => isEnemyVisible(w, myPosition, state.position))
        } yield (enemy, state)
      sender ! VisibleEnemies(visibleEnemies)
  }

  def genPlayerState = PlayerState(
    ObjectId(java.util.UUID.randomUUID().toString),
    Vector2(rng.nextDouble, rng.nextDouble),
    Vector2(1, 0),
    100)
}

// https://en.wikipedia.org/wiki/K-d_tree

object Game {

  //Если два отрезка пересекаются, то есть концы одного отрезка находятся по разные стороны
  //от прямой, образованной другим отрезком и наоборот.
  def isEnemyVisible(w: Wall, me: Vector2, enemy: Vector2): Boolean = {
    !((math.signum((w.b - w.a) x (me - w.a)) != math.signum((w.b - w.a) x (enemy - w.a))) &&
      (math.signum((enemy - me) x (w.a - me)) != math.signum((enemy - me) x (w.b - me))))
  }

  def distanceToWall(w: Wall, p: Vector2) = ((p - w.a).length + (p - w.b).length) / 2

}



