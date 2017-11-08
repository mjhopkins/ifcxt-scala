package ifcxt.v1

import scala.reflect.macros.{TypecheckException, blackbox}

object IfCxtMacros {

  def materialize[T: c.WeakTypeTag](c: blackbox.Context): c.Tree = {
    import c.universe._

    val T = weakTypeOf[T]
    val ifCxt = weakTypeOf[IfCxt[_]].typeConstructor
    val ifCxtT = appliedType(ifCxt, T)

    def findImplicit(t: c.Type) =
      try Some(c.inferImplicitValue(t, silent = false, withMacrosDisabled = true))
      catch { case e: TypecheckException => None }

    // is there already an IfCxt[T] in the implicit scope?
    findImplicit(ifCxtT) foreach { v =>
      c.abort(
        c.enclosingPosition,
        s"$T already has an instance of $ifCxt in the implicit scope: $v"
      )
    }

    // is there a T in implicit scope?
    findImplicit(T) match {
      case Some(tInstance) =>
        q""" new $ifCxtT { val maybeCxt = ${ Some(tInstance) } } """
      case None            =>
        q""" new $ifCxtT { val maybeCxt = $None } """
    }

  }
}
