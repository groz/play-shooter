package models

import akka.actor._
import models.protocol._

import scala.util.Random

class Game extends Actor {

  val rng = new Random()

  override def receive: Receive = process(Set.empty, Seq.empty)

  def process(players: Set[ActorRef], walls: Seq[Wall]): Receive = {

    case JoinGame =>
      val playerState = genPlayerState
      sender ! InitPlayer(playerState)
      println(s"Player $playerState joined...")
      context become process(players + sender, walls)

    case LeaveGame =>
      println(s"Player $sender left.")
      context become process(players - sender, walls)

    case SetWalls(w: Seq[Wall]) =>
      println(s"New walls has been set!")
      context become process (players, w)
  }

  def genPlayerState = PlayerState(
    ObjectId(java.util.UUID.randomUUID().toString),
    Vector2(rng.nextDouble, rng.nextDouble),
    Vector2(1, 0),
    100)

}








