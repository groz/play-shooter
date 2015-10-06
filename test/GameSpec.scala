import models._
import org.scalatest._

class GameSpec extends FlatSpec with Matchers {

  "isEnemyVisible" should "return false for simple case" in {
    val wall = Wall(Vector2(0, -1), Vector2(0, 1))
    val me = Vector2(-1, 0)
    val enemy = Vector2(1, 0)
    Game.isEnemyVisible(wall, me, enemy) should be(false)
  }

  it should "return true for simple case" in {
    val wall = Wall(Vector2(0, -1), Vector2(0, 1))
    val me = Vector2(0.5, 0)
    val enemy = Vector2(1, 0)
    Game.isEnemyVisible(wall, me, enemy) should be(true)
  }

  it should "return true for another simple case" in {
    val wall = Wall(Vector2(0, -1), Vector2(0, 1))
    val me = Vector2(-1, 1000)
    val enemy = Vector2(1, 0)
    Game.isEnemyVisible(wall, me, enemy) should be(true)
  }

  //   def distanceToWall(w: Wall, p: Vector2) = ((p - w.a).length + (p - w.b).length) / 2

  "distanceToWall" should "return 1 for (0, 0) & [(1, -1), (1, 1)]" in {
    val wall = Wall(Vector2(1, -1), Vector2(1, 1))
    Game.distanceToWall(wall, Vector2(0, 0)) should be(1.0)
  }
}
