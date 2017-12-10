scalax-collection [![Build Status](https://travis-ci.org/jozic/scalax-collection.svg?branch=master)](https://travis-ci.org/jozic/scalax-collection) [![Coverage Status](https://coveralls.io/repos/jozic/scalax-collection/badge.svg)](https://coveralls.io/r/jozic/scalax-collection)
=================

A small library of extension methods for standard scala collections library. 
Published to maven central.

| 2.10 | 2.11 | 2.12 |
|------|------|------|
|[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.10/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.10) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.11) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.12) |


## Collection Examples

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
val xs = Traversable(List(1,2,3), List(4,5), List(6,7,8,9)).foldLeftWhile(List.empty[Int])(_.size < 4){ case (acc, l) => acc ++ l }
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

### withFrequencyBy

```tut
val fm = List("ab", "bc", "cd", "ab", "bc", "de").withFrequencyBy(_.head)
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

### transform

Transform only those elements where given partial function is defined
```tut
val t = Traversable(1,2,3,4,5).transform { case x if x % 2 == 0 => x + 100 }
```

## Strings Examples

An import needed for examples to work:
```tut
import com.daodecode.scalax._
```
### NonEmptyString

```tut
NonEmptyString(null)
NonEmptyString("")
NonEmptyString(" a ")

(null: String) match {
  case NonEmptyString(_) => "boo" 
  case _ => "works!"
}

"" match {
  case NonEmptyString(_) => "boo" 
  case _ => "works!"
}

"works!" match {
  case NonEmptyString(s) => s 
  case _ => "boo"
}
```

### NonBlankString

```tut
NonBlankString(null)
NonBlankString("")
NonBlankString(" \n \r \t ")
NonBlankString(" a ")

(null: String) match {
  case NonBlankString(_) => "boo" 
  case _ => "works!"
}

"" match {
  case NonBlankString(_) => "boo" 
  case _ => "works!"
}

"   \t " match {
  case NonBlankString(_) => "boo" 
  case _ => "works!"
}

"works!" match {
  case NonBlankString(s) => s 
  case _ => "boo"
}
```

### NonBlankTrimmedString

```tut
NonBlankTrimmedString(null)
NonBlankTrimmedString("")
NonBlankTrimmedString(" \n \r \t ")
NonBlankTrimmedString(" a ")

(null: String) match {
  case NonBlankTrimmedString(_) => "boo" 
  case _ => "works!"
}

"" match {
  case NonBlankTrimmedString(_) => "boo" 
  case _ => "works!"
}

"   \t " match {
  case NonBlankTrimmedString(_) => "boo" 
  case _ => "works!"
}

"  works!\n  " match {
  case NonBlankTrimmedString(s) => s 
  case _ => "boo"
}
```

## Latest stable release

### sbt
```scala
libraryDependencies += "com.daodecode" %% "scalax-collection" % "0.2.0"
```
### maven

set `<scala.binary.version>` property to scala version you need, like

```xml
<properties>
    <scala.binary.version>2.12</scala.binary.version>
</properties>

```
 and then in `dependencies` add
 
```xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_${scala.binary.version}</artifactId>
    <version>0.2.0</version>
</dependency>
```

## Latest snapshot

First add sonatype snapshots repository to your settings

### sbt

```scala
resolvers += Resolver.sonatypeRepo("snapshots")
```

### maven

```xml
<repository>
    <id>snapshots-repo</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
</repository>
```

then add snapshot as a dependency

### sbt
```scala
libraryDependencies += "com.daodecode" %% "scalax-collection" % "0.2.1-SNAPSHOT"
```
### maven
```xml
<properties>
    <scala.binary.version>2.12</scala.binary.version>
</properties>
```
 
```xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_${scala.binary.version}</artifactId>
    <version>0.2.1-SNAPSHOT</version>
</dependency>
```