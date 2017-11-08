package ifcxt.v3

import scala.language.higherKinds

import ifcxt.AbstractIfCxt

sealed trait IfCxt[T] extends AbstractIfCxt[T]

trait LowPriorityIfCxt {
  implicit def absent[T]: IfCxt[T] = new IfCxt[T] {
    def maybeCxt = None
  }
}

object IfCxt extends LowPriorityIfCxt {
  implicit def present[T](implicit c: T): IfCxt[T] = new IfCxt[T] {
    def maybeCxt = Some(c)
  }
}

