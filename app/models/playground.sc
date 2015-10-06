import models.{Vector2, Wall}

def isEnemyVisible (w: Wall, me: Vector2, enemy: Vector2) : Boolean = {
  !((math.signum((w.b - w.a) x (me - w.a)) != math.signum((w.b - w.a) x (enemy - w.a))) &&
    (math.signum((enemy - me) x (w.a - me)) != math.signum((enemy - me) x (w.b - me))))
}

val w = Wall(Vector2(-1, 0), Vector2(-1, 0))
val me = Vector2(0, -1)
val enemy = Vector2(0, 1)

math.signum((w.b - w.a) x (me - w.a))
math.signum((w.b - w.a) x (enemy - w.a))

isEnemyVisible(w, me, enemy)