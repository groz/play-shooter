package models

import akka.actor._
import models.protocol._
import play.api.libs.json.JsValue

class Player(game: ActorRef, out: ActorRef) extends Actor {

  game ! JoinGame

  override def postStop() = game ! LeaveGame

  override def receive: Receive = {

    case InitPlayer(state) =>
      context become process(state)
  }

  def process(state: PlayerState): Receive = {
    case clientMessage: JsValue =>
      println(clientMessage)
      out ! clientMessage
  }
}
