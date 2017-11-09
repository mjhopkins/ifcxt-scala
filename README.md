# Conditional contexts for Scala

A Scala port of the [ifcxt](https://hackage.haskell.org/package/ifcxt) Haskell library by [Mike Izbicki](http://github.com/mikeizbicki/).

This library allows you to write generic code, with special cases depending on whether the supplied type parameter has an instance for a selected type class in scope.

It does this through an `IfCxt` helper type class which allows you to test for the presence of an implicit without generating a compile error.


Slides for the accompanying [ScalaSyd](https://meetup.com/ScalaSyd) [talk](https://www.meetup.com/scalasyd/events/244852083/) can be found [here](https://mjhopkins.github.io).

Three implementations are provided:

* `v1` - uses macros
* `v2` - uses default parameters
* `v3` - uses a stacked hierarchy of traits

---

## At the REPL 

`sbt console` then 

```
scala> import ifcxt._, v1._
```
or
```
scala> import ifcxt._, v2._
```
or
```
scala> import ifcxt._, v3._
```

Some examples:

### Eliding sensitive data

```
scala> printSecurely(123456789)
res0: String = 123456789
scala> val tfn = TFN(123456789)
scala> printSecurely(tfn)
res1: String = 123******
scala> case class Password(value: String)
scala> printSecurely(Password("p@55w0rd"))
res2: String = p@55w0rd
scala> implicit val passwordIsSecure: Secure[Password] =
  Secure(_.value.replaceAll(".", "*"))
scala> printSecurely(Password("p@55w0rd"))
res3: String = ********
```

### Creating a set

`mkSet` creates a `TreeSet` if the elements support an ordering (`scala.math.Ordering` type class),
otherwise it falls back to a `HashSet`:


```scala
scala> mkSet(1,2,3)
res0: Set[Int] = TreeSet(1, 2, 3)

scala> mkSet(Array(1), Array(2), Array(3))
res1: Set[Array[Int]] = Set(Array(1), Array(2), Array(3))
```

If you need to keep the two cases separate, use `mkSetEither` instead:

```scala
scala> mkSetEither('a', 'b', 'c')
res0: Either[scala.collection.immutable.TreeSet[Char],Set[Char]] = Left(TreeSet(a, b, c))

scala> mkSetEither('a', 'b', 'c', 3)
res1: Either[scala.collection.immutable.TreeSet[Int],Set[Int]] = Left(TreeSet(3, 97, 98, 99))

scala> mkSetEither('a', 'b', 'c', 3, "hi")
res2: Either[scala.collection.immutable.TreeSet[Any],Set[Any]] = Right(Set(a, b, c, 3, hi))
```

### What else?

More demos in `ifcxt/Demos.scala`.