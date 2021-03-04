name := "scalax-collection"

organization := "com.daodecode"

scalaVersion := "2.13.5"

crossScalaVersions := Seq("2.12.13", scalaVersion.value)

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

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.9" % "test"
)

def filterOutUnusedImports(options: Seq[String]): Seq[String] =
  options.filterNot(Set("-Ywarn-unused", "-Ywarn-unused-import", "-Xlint")) ++
    Seq("-Xlint:-unused", "-Ywarn-unused:-imports,_")

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

