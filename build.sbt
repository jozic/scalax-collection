name := "scalax-collection"

organization := "com.daodecode"

scalaVersion := "2.13.0"

crossScalaVersions := Seq("2.12.9", scalaVersion.value)

scalacOptions := Seq(
  "-Xlint",
  "-unchecked",
  "-deprecation",
//  "-Xfatal-warnings",
  "-Ywarn-dead-code",
  "-feature",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-Ywarn-unused",
  "-encoding", "UTF-8"
) ++ (if (priorTo2_13(scalaVersion.value))
  Seq(
    "-Ywarn-inaccessible",
    "-Yno-adapted-args",
    "-Ywarn-nullary-unit",
    "-Ywarn-unused-import"
  ) else Seq.empty)

val silencerVersion ="1.4.3"

libraryDependencies ++= Seq(
  compilerPlugin("com.github.ghik" % "silencer-plugin" % silencerVersion cross CrossVersion.full),
  "com.github.ghik" % "silencer-lib" % silencerVersion % Provided cross CrossVersion.full,
  "org.scalatest" %% "scalatest" % "3.0.8" % "test"
)

def filterOutUnusedImports(options: Seq[String]) =
  options.filterNot(Set("-Ywarn-unused", "-Ywarn-unused-import", "-Xlint")) ++
    Seq("-Xlint:-unused", "-Ywarn-unused:-imports,_")

scalacOptions in Tut := filterOutUnusedImports(scalacOptions.value)

scalacOptions in(Compile, console) := filterOutUnusedImports(scalacOptions.value)

def priorTo2_13(scalaVersion: String): Boolean =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, minor)) if minor < 13 => true
    case _                              => false
  }

initialCommands in console :=
  """
    |import com.daodecode.scalax.collection.extensions._
    |import com.daodecode.scalax._
    |""".stripMargin

