import xerial.sbt.Sonatype._

sonatypeSettings ++ Seq(

  startYear := Some(2014),

  homepage := Some(url("http://github.com/jozic/scalax-collection")),

  developers := List(
    Developer("jozic", "Eugene Platonov", "jozic@live.com", url("http://github.com/jozic"))
  ),

  scmInfo := homepage.value.map(ScmInfo(_, "scm:git:git@github.com:jozic/scalax-collection.git")),

  licenses := Seq("BSD-style" -> url("http://www.opensource.org/licenses/BSD-3-Clause"))
)

publishTo := sonatypePublishTo.value