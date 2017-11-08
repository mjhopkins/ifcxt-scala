package ifcxt

object Show {

  def apply[A](f: A => String) = new Show[A] {
    def show(a: A) = f(a)
  }

  implicit val string: Show[String] = new Show[String] {
    def show(a: String) = a
  }

  implicit val int: Show[Int] = new Show[Int] {
    def show(a: Int) = a.toString
  }
}

trait Show[A] {
  def show(a: A): String
}