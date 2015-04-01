name := "scalax-collection"

organization := "com.daodecode"

scalaVersion := "2.10.5"

crossScalaVersions := Seq("2.10.5", "2.11.6")

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint", "-Xfatal-warnings")

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"

initialCommands in console := "import com.daodecode.scalax.collection.extensions._"