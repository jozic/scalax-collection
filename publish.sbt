import xerial.sbt.Sonatype._

sonatypeSettings

pomExtra :=
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
