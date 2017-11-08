package ifcxt

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.language.higherKinds

abstract class IfCxtTests[IfCxt[T] <: AbstractIfCxt[T]] {
  def ifCxt[T: IfCxt, A](y: T => A, n: A) = implicitly[IfCxt[T]].ifCxt(y, n)

  def show[A](a: A)(implicit i: IfCxt[Show[A]]) =
    i.ifCxt(_.show(a), "Can't show")

  def printSecurely[A](a: A)(implicit i: IfCxt[Secure[A]]) =
    i.ifCxt(
      _.mask(a),
      a.toString
    )

  def nub[N](ns: List[N])(implicit i: IfCxt[Ordering[N]]) =
    i.ifCxt(
      implicit ord => fastNub(ns.sorted),
      ns.distinct
    )

  // probably not actually faster, since every object in Scala has a hash
  // and `distinct` is able to build up a hash set of seen elements
  @tailrec
  final def fastNub[N](list: List[N], acc: ListBuffer[N] = ListBuffer.empty[N]): List[N] = list match {
    case x1 :: x2 :: rest if x1 == x2 => fastNub(x2 :: rest, acc)
    case x1 :: x2 :: rest             => fastNub(x2 :: rest, acc += x1)
    case List(x)                      => (acc += x).toList
    case Nil                          => acc.toList
  }

  def smartSum[N: Numeric](ns: List[N])(implicit i: IfCxt[Fractional[N]]) =
    i.ifCxt(
      implicit frac => sumKahan(ns),
      ns.sum
    )

  // https://en.wikipedia.org/wiki/Kahan_summation_algorithm
  def sumKahan[N](ns: List[N])(implicit N: Fractional[N]) = {
    import N._

    println("Using Kahan summation")

    def go(p: (N, N), i: N): (N, N) = {
      val (c, t) = p
      val y = i - c
      val t1 = t + y
      ((t1 - t) - y, t1)
    }

    ns.foldLeft((N.zero, N.zero))(go)._2
  }
}
