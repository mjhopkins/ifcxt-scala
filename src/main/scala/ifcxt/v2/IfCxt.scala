package ifcxt.v2

import scala.language.higherKinds

import ifcxt.AbstractIfCxt

sealed trait IfCxt[T] extends AbstractIfCxt[T]

object IfCxt {
  implicit def instance[T](implicit c: T = null): IfCxt[T] =
    new IfCxt[T] {
      def maybeCxt = Option(c)
    }
}
