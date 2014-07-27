scalax-collection [![Build Status](https://travis-ci.org/jozic/scalax-collection.png?branch=master)](https://travis-ci.org/jozic/scalax-collection) [![Coverage Status](https://coveralls.io/repos/jozic/scalax-collection/badge.png)](https://coveralls.io/r/jozic/scalax-collection)
=================

A small library of extension methods for standard scala collections library

Released for scala 2.10 and scala 2.11

##Latest stable release

### sbt
```
libraryDependencies += "com.daodecode" %% "scalax-collection" % "0.1.0"
```
### maven
```
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_2.10</artifactId>
    <version>0.1.0</version>
</dependency>
```
or
```
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_2.11</artifactId>
    <version>0.1.0</version>
</dependency>
```

##Latest snapshot

First add sonatype snapshots repository to your settings

### sbt

`resolvers += Resolver.sonatypeRepo("snapshots")`

### maven

```
<repository>
    <id>snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
</repository>
```

then add snapshot as a dependency

### sbt
```
libraryDependencies += "com.daodecode" %% "scalax-collection" % "0.1.1-SNAPSHOT"
```
### maven
```
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_2.10</artifactId>
    <version>0.1.1-SNAPSHOT</version>
</dependency>
```
or
```
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_2.11</artifactId>
    <version>0.1.1-SNAPSHOT</version>
</dependency>
```
