package ifcxt.v1

import scala.language.experimental.macros

import ifcxt.AbstractIfCxt

trait IfCxt[T] extends AbstractIfCxt[T]

object IfCxt {
  implicit def materialize[T]: IfCxt[T] = macro IfCxtMacros.materialize[T]
}