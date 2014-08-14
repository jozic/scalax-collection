scalax-collection [![Build Status](https://travis-ci.org/jozic/scalax-collection.png?branch=master)](https://travis-ci.org/jozic/scalax-collection) [![Coverage Status](https://coveralls.io/repos/jozic/scalax-collection/badge.png)](https://coveralls.io/r/jozic/scalax-collection)
=================

A small library of extension methods for standard scala collections library

Released for scala 2.10 and scala 2.11

## Examples

### distinctBy

preserving first duplicate
``` scala
scala> val xs = List(1 -> "one", 1 -> "ten", 2 -> "two", 2 -> "twenty").
| distinctBy(_._1)
xs: List[(Int, String)] = List((1,one), (2,two))
```

or preserving any duplicate you want
``` scala
scala> val xs = List(1 -> "one", 1 -> "ten", 2 -> "two", 2 -> "twenty").
| distinctBy(_._1, takeFirst = _._2.length > _._2.length)
xs: List[(Int, String)] = List((1,ten), (2,twenty))
```

### foldLeftWhile/foldRightWhile
``` scala
scala> val xs = Iterable(List(1,2,3), List(4,5), List(6,7,8,9)).
| foldLeftWhile(List.empty[Int])(_.size < 4){ case (acc, l) => acc ++ l }
xs: List[Int] = List(1, 2, 3, 4, 5)
```

### toCompleteMap

``` scala
scala> val cm = List(1 -> "1", 2 -> "2", 1 -> "11").toCompleteMap
cm: scala.collection.immutable.Map[Int,List[String]] = Map(2 -> List(2), 1 -> List(1, 11))
```

### withFrequency

``` scala
scala> val fm = List("a", "b", "c", "a", "b", "d").withFrequency
fm: scala.collection.immutable.Map[String,Int] = Map(b -> 2, d -> 1, a -> 2, c -> 1)
```

##Latest stable release

### sbt
```
libraryDependencies += "com.daodecode" %% "scalax-collection" % "0.1.0"
```
### maven
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_2.10</artifactId>
    <version>0.1.0</version>
</dependency>
```
or
``` xml
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

``` xml
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
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_2.10</artifactId>
    <version>0.1.1-SNAPSHOT</version>
</dependency>
```
or
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_2.11</artifactId>
    <version>0.1.1-SNAPSHOT</version>
</dependency>
```
