name := "scalax-collection"

organization := "com.daodecode"

scalaVersion := "2.12.1"

crossScalaVersions := Seq("2.10.6", "2.11.8", scalaVersion.value)

def scalacOptionsVersion(scalaBinVersion: String) = {
  Seq(
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
    "-encoding", "UTF-8"
  ) ++ {
    scalaBinVersion match {
      case "2.11" | "2.12" => Seq("-Ywarn-unused", "-Ywarn-unused-import")
      case _ => Nil
    }
  }
}

scalacOptions := scalacOptionsVersion(scalaBinaryVersion.value)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"

scalacOptions in (Compile, console) := scalacOptions.value.filterNot(_ == "-Ywarn-unused-import")

initialCommands in console := "import com.daodecode.scalax.collection.extensions._"
