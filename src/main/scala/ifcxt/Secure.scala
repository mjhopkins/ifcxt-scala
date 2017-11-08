package ifcxt

// Tax file numbers
// secure in some contexts, not in others
case class TFN(value             : Int) {
  // NB don't do this in production!
  // Use a (possibly macro-based) smart constructor instead
  if (value.toString.length != 9) throw new IllegalArgumentException("Not a valid tax file number")
}

object TFN {
  implicit val secure: Secure[TFN] =
    Secure {
      tfn => tfn.value.toString.slice(0, 3) + "*" * 6
    }
}

trait Secure[A] {
  def mask(a: A): String
}

object Secure {
  def apply[A](f: A => String) = new Secure[A] {
    def mask(a: A) = f(a)
  }
}
