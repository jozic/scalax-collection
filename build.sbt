import SonatypeKeys._

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

def extraPom =
  <url>http://github.com/jozic/scalax-collection</url>
    <licenses>
      <license>
        <name>BSD-style</name>
        <url>http://www.opensource.org/licenses/BSD-3-Clause</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:jozic/scalax-collection.git</url>
      <connection>scm:git:git@github.com:jozic/scalax-collection.git</connection>
    </scm>
    <developers>
      <developer>
        <id>jozic</id>
        <name>Eugene Platonov</name>
        <url>http://github.com/jozic</url>
      </developer>
    </developers>


