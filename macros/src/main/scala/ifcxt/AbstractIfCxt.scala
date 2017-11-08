package ifcxt

trait AbstractIfCxt[T] {
  def ifCxt[A](y: T => A, n: A): A = maybeCxt map y getOrElse n
  def maybeCxt: Option[T]
}
