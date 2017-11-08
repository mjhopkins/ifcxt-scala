
import sbt.Keys._
import sbt._

object Settings {
  lazy val basicSettings = Seq[Setting[_]](
    scalaVersion := "2.11.8",
    scalacOptions := Seq("-deprecation", "-encoding", "utf8"),
    organization := "com.github.mjhopkins",
    version := "0.1.0",
    description := "new project",
    resolvers ++=
      Seq(
        Resolver.sonatypeRepo("releases"),
        Resolver.sonatypeRepo("snapshots"),
        "bintray/non" at "http://dl.bintray.com/non/maven",
        "bintray/scalaz" at "http://dl.bintray.com/scalaz/releases"
      )
  )
}

object deps {

  val macroParadise = Seq(compilerPlugin("org.scalamacros" % "paradise_2.10.4" % "2.0.1"))
}

object ProjectBuild extends Build {

  import Settings._

  lazy val macros = Project("macros", file("macros"))
    .settings(basicSettings: _*)
    .settings(
      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-reflect" % _),
      libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _),
      libraryDependencies := {
        CrossVersion.partialVersion(scalaVersion.value) match {
          // if Scala 2.11+ is used, quasiquotes are available in the standard distribution
          case Some((2, x)) if x >= 11 =>
            libraryDependencies.value
          // in Scala 2.10, quasiquotes are provided by macro paradise
          case Some((2, 10)) =>
            libraryDependencies.value ++ Seq(
              compilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full),
              "org.scalamacros" %% "quasiquotes" % "2.1.0-M5" cross CrossVersion.binary)
        }
      }
    )

  lazy val ifcxt = Project("ifcxt", file("."))
    .settings(basicSettings: _*)
    .settings(
      scalacOptions in Test ++= Seq("-Yrangepos")
    )
    .dependsOn(macros)
}
