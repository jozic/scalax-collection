name := "scalax-collection"

version := "0.1.0-SNAPSHOT"

organization := "com.daodecode"

scalaVersion := "2.10.4"

scalacOptions ++= Seq("-deprecation", "-unchecked", "-Xlint", "-Xfatal-warnings")

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.6" % "test"

instrumentSettings

CoverallsPlugin.coverallsSettings

ScoverageKeys.minimumCoverage := 70

ScoverageKeys.failOnMinimumCoverage := true

ScoverageKeys.highlighting := true

