scalax-collection [![Build Status](https://travis-ci.org/jozic/scalax-collection.svg?branch=master)](https://travis-ci.org/jozic/scalax-collection) [![Coverage Status](https://coveralls.io/repos/jozic/scalax-collection/badge.svg)](https://coveralls.io/r/jozic/scalax-collection)
=================

A small library of extension methods for standard scala collections library

Released for scala 2.10, 2.11 and 2.12

## Examples

An import needed for examples to work:
```tut
import com.daodecode.scalax.collection.extensions._
```

### distinctBy

preserving first duplicate
```tut
val xs = List(1 -> "one", 1 -> "ten", 2 -> "two", 2 -> "twenty").distinctBy(_._1)
```

or preserving any duplicate you want
```tut
val xs = List(1 -> "one", 1 -> "ten", 2 -> "two", 2 -> "twenty").distinctBy(_._1, takeFirst = _._2.length > _._2.length)
```

### foldLeftWhile/foldRightWhile

```tut
val xs = Iterable(List(1,2,3), List(4,5), List(6,7,8,9)).foldLeftWhile(List.empty[Int])(_.size < 4){ case (acc, l) => acc ++ l }
```

### toCompleteMap

```tut
val cm = List(1 -> "1", 2 -> "2", 1 -> "11").toCompleteMap
```

### mapToMap

can be seen as more efficient replacement for `map().toMap` combination

```tut
val m = List("1" -> "one", "2" -> "two").mapToMap { case (i, s) => i.toInt -> s }
```

### toMapWithKey

```tut
val m = List("1", "2", "1").toMapWithKey(_.toInt)
```

### toMapWithValue

```tut
val m = List("1", "2", "1").toMapWithValue(_.toInt)
```

### withFrequency

```tut
val fm = List("a", "b", "c", "a", "b", "d").withFrequency
```

### mergedWith

Merges two maps using provided function to merge values for duplicate keys
```tut
val merged = Map("1" -> 1, "2" -> 2).mergedWith(Map("1" -> 1, "2" -> 2))(_ + _)
```

### minOption/minOptionBy

Finds the smallest element wrapped in `Option` or `None` if iterable is empty
```tut
val m = List.empty[Int].minOption

val m = List(1,2,1).minOptionBy(_ * -1)
```

### maxOption/maxOptionBy

Finds the largest element wrapped in `Option` or `None` if iterable is empty
```tut
val m = List.empty[Int].maxOption

val m = List(1,2,1).maxOptionBy(_ * -1)
```

##Latest stable release

### sbt
```
libraryDependencies += "com.daodecode" %% "scalax-collection" % "0.1.2"
```
### maven
#### scala 2.10 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.10/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.10)
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_2.10</artifactId>
    <version>0.1.2</version>
</dependency>
```
or
#### scala 2.11 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.11)

``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_2.11</artifactId>
    <version>0.1.2</version>
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
libraryDependencies += "com.daodecode" %% "scalax-collection" % "0.1.3-SNAPSHOT"
```
### maven
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_2.10</artifactId>
    <version>0.1.3-SNAPSHOT</version>
</dependency>
```
or
``` xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_2.11</artifactId>
    <version>0.1.3-SNAPSHOT</version>
</dependency>
```
