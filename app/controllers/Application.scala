package controllers

import javax.inject._

import akka.actor.{ActorSystem, Props}
import models.{Game, Player}
import play.api._
import play.api.libs.json._
import play.api.mvc._
import play.api.Play.current

@Singleton
class Application @Inject()(actorSystem: ActorSystem) extends Controller {

  val game = actorSystem.actorOf(Props[Game])

  def socket = WebSocket.acceptWithActor[JsValue, JsValue](request => out => Props(new Player(game, out)))

  def index = Action {
    Ok(views.html.index())
  }

}
