package models

import akka.actor._
import models.protocol._
import play.api.libs.json._

class Player(game: ActorRef, out: ActorRef) extends Actor {

  implicit val objectIdFmt = Json.format[ObjectId]
  implicit val vector2Fmt = Json.format[Vector2]
  implicit val stateFmt = Json.format[PlayerState]
  implicit val initMessageFmt = Json.format[models.protocol.InitPlayer]

  def toCommand[A](name: String, a: A)(implicit fmt: Format[A]): JsValue =
    JsObject(Map("name" -> JsString(name), "data" -> Json.toJson(a)))

  game ! JoinGame

  override def postStop() = game ! LeaveGame

  override def receive: Receive = {

    case msg @ InitPlayer(state) =>
      out ! toCommand("InitPlayer", msg)
      context become process(state)
  }

  def process(state: PlayerState): Receive = {

    case clientMessage: JsValue =>
      println(clientMessage)
      out ! clientMessage

    case msg @ PlayerJoined =>
      out ! msg
  }
}
