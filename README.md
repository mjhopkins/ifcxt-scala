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
scala> import ifcxt._; import v1._
```
or
```
scala> import ifcxt._; import v2._
```
or
```
scala> import ifcxt._; import v3._
```



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

More demos in `ifcxt/Demos.scala`.