package models

import akka.actor._
import models.protocol._
import play.api.libs.json._

class Player(game: ActorRef, out: ActorRef) extends Actor {

  implicit val objectIdFmt = Json.format[ObjectId]
  implicit val vector2Fmt = Json.format[Vector2]
  implicit val stateFmt = Json.format[PlayerState]
  implicit val initMessageFmt = Json.format[models.protocol.InitPlayer]
  implicit val pjoinedMessageFmt = Json.format[models.protocol.PlayerJoined]
  implicit val pleftMessageFmt = Json.format[models.protocol.PlayerLeft]
  implicit val reposMessageFmt = Json.format[models.protocol.Reposition]
  implicit val reposplayaMessageFmt = Json.format[models.protocol.PlayerReposition]

  def toCommand[A](name: String, a: A)(implicit fmt: Format[A]): JsValue =
    JsObject(Map("name" -> JsString(name), "data" -> Json.toJson(a)))

  game ! JoinGame

  override def postStop() = game ! LeaveGame

  override def receive: Receive = {

    case msg @ InitPlayer(state, playerStates) =>
      out ! toCommand("InitPlayer", msg)
      context become process(state)
  }

  def process(state: PlayerState): Receive = {

    case clientMessage: JsValue =>
      clientMessage.\("name").as[String] match {
        case "reposition" =>
          val vec = (clientMessage\"data").as[Vector2]
          println(s"repositioning from init started $vec")
          game ! Reposition(vec)
        case _ =>
          println(clientMessage)
          out ! clientMessage
      }

    case msg: PlayerReposition =>
      out ! toCommand("PlayerReposition", msg)

    case msg: PlayerJoined =>
      out ! toCommand("PlayerJoined", msg)

    case msg: PlayerLeft =>
      out ! toCommand("PlayerLeft", msg)


  }
}
