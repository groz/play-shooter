import models.{Game, Vector2, Wall}

//val w = Wall(Vector2(0, -1), Vector2(0, 1))
//val me = Vector2(-1, 0)
//val enemy = Vector2(1, 0)

val w = Wall(Vector2(0, -1), Vector2(0, 1))
val me = Vector2(-1, 0)
val enemy = Vector2(1, 0)

math.signum((w.b - w.a) x (me - w.a))
math.signum((w.b - w.a) x (enemy - w.a))

Game.isEnemyVisible(w, me, enemy)