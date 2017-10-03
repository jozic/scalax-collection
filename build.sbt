name := "scalax-collection"

organization := "com.daodecode"

scalaVersion := "2.12.3"

crossScalaVersions := Seq("2.11.11", scalaVersion.value)

scalacOptions := Seq(
  "-Xlint",
  "-unchecked",
  "-deprecation",
  "-Xfatal-warnings",
  "-Ywarn-inaccessible",
  "-Ywarn-dead-code",
  "-Ywarn-adapted-args",
  "-Ywarn-nullary-unit",
  "-feature",
  "-language:postfixOps",
  "-language:implicitConversions",
  "-Ywarn-unused",
  "-Ywarn-unused-import",
  "-encoding", "UTF-8"
)

libraryDependencies +=
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"

scalacOptions in(Compile, console) := scalacOptions.value
  .filterNot(Set("-Ywarn-unused", "-Ywarn-unused-import", "-Xlint")) ++
  Seq("-Xlint:-unused", "-Ywarn-unused:-imports,_")

initialCommands in console := "import com.daodecode.scalax.collection.extensions._"
