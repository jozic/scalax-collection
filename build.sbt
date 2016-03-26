name := "scalax-collection"

organization := "com.daodecode"

scalaVersion := "2.10.6"

crossScalaVersions := Seq("2.10.6", "2.11.8")

def scalacOptionsVersion(scalaBinVersion: String) = {
  Seq(
    "-Xlint",
    "-unchecked",
    "-deprecation",
    "-Xfatal-warnings",
    "-Ywarn-inaccessible",
    "-Ywarn-dead-code",
    "-feature",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-encoding", "UTF-8"
  ) ++ {
    scalaBinVersion match {
      case "2.11" => Seq("-Ywarn-unused", "-Ywarn-unused-import")
      case _ => Nil
    }
  }
}


scalacOptions := scalacOptionsVersion(scalaBinaryVersion.value)

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.6" % "test"

initialCommands in console := "import com.daodecode.scalax.collection.extensions._"