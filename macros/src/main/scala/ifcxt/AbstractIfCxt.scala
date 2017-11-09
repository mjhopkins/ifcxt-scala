package ifcxt

trait AbstractIfCxt[C] {
  def ifCxt[A](y: C => A, n: A): A = maybeCxt map y getOrElse n
  def maybeCxt: Option[C]
}
