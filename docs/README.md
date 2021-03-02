scalax-collection [![Build Status](https://travis-ci.org/jozic/scalax-collection.svg?branch=master)](https://travis-ci.org/jozic/scalax-collection) [![Coverage Status](https://coveralls.io/repos/jozic/scalax-collection/badge.svg)](https://coveralls.io/r/jozic/scalax-collection)
=================

A small library of extension methods for standard scala collections library. 
Published to maven central.

| 2.10 | 2.11 | 2.12 | 2.13 |
|------|------|------|------|
|[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.10/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.10) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.11) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.12) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.13) |


## Collection Examples

An import needed for examples to work:
```scala mdoc
import com.daodecode.scalax.collection.extensions._
```

### distinctBy

preserving first duplicate
```scala mdoc
val xs = List(1 -> "one", 1 -> "ten", 2 -> "two", 2 -> "twenty").distinctBy(_._1)
```

*NOTE: Since Scala 2.13 this method is available in standard library*

### distinctByUsing

or preserving any duplicate you want
```scala mdoc:nest
val xs = List(1 -> "one", 1 -> "ten", 2 -> "two", 2 -> "twenty").distinctByUsing(_._1, takeFirst = _._2.length > _._2.length)
```

*NOTE: Before 0.3.0 this method was named `distinctBy`*

### foldLeftWhile/foldRightWhile

```scala mdoc:nest
val xs = Iterable(List(1,2,3), List(4,5), List(6,7,8,9)).foldLeftWhile(List.empty[Int])(_.size < 4){ case (acc, l) => acc ++ l }
```

### toCompleteMap

*since Scala 2.13 can be seen as equivalent to `groupMap(_._1)(_._2)`*

```scala mdoc:nest
val cm = List(1 -> "1", 2 -> "2", 1 -> "11").toCompleteMap
```

### mapToMap

*can be seen as more efficient replacement for `map().toMap` combination*

```scala mdoc:nest
val m = List("1" -> "one", "2" -> "two").mapToMap { case (i, s) => i.toInt -> s }
```

### toMapWithKey

*since Scala 2.13 can be seen as equivalent to `groupMapReduce(f)(identity)((b,_) => b)`*

```scala mdoc:nest
val m = List("1", "2", "1").toMapWithKey(_.toInt)
```

### toMapWithValue

*since Scala 2.13 can be seen as equivalent to `groupMapReduce(identity)(f)((b,_) => b)`*

```scala mdoc:nest
val m = List("1", "2", "1").toMapWithValue(_.toInt)
```

### withFrequency

*since Scala 2.13 can be seen as equivalent to `groupMapReduce(identity)(_ => 1)(_ + _)`*

```scala mdoc:nest
val fm = List("a", "b", "c", "a", "b", "d").withFrequency
```

### withFrequencyBy

*since Scala 2.13 can be seen as equivalent to `groupMapReduce(f)(_ => 1)(_ + _)`*

```scala mdoc:nest
val fm = List("ab", "bc", "cd", "ab", "bc", "de").withFrequencyBy(_.head)
```

### mergedWith

Merges two maps using provided function to merge values for duplicate keys
```scala mdoc:nest
val merged = Map("1" -> 1, "2" -> 2).mergedWith(Map("1" -> 1, "2" -> 2))(_ + _)
```

### minOption/minOptionBy

Finds the smallest element wrapped in `Option` or `None` if iterable is empty
```scala mdoc:nest
val m1 = List.empty[Int].minOption

val m2 = List(1,2,1).minOptionBy(_ * -1)
```

*NOTE: Since Scala 2.13 this is available in standard library*

### maxOption/maxOptionBy

Finds the largest element wrapped in `Option` or `None` if iterable is empty
```scala mdoc:nest
val m1 = List.empty[Int].maxOption

val m2 = List(1,2,1).maxOptionBy(_ * -1)
```

*NOTE: Since Scala 2.13 this is available in standard library*

## Strings Examples

An import needed for examples to work:
```scala mdoc:nest
import com.daodecode.scalax._
```
### NonEmptyString

```scala mdoc
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

```scala mdoc
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

```scala mdoc
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
libraryDependencies += "com.daodecode" %% "scalax-collection" % "0.3.0"
```
### maven

set `<scala.binary.version>` property to scala version you need, like

```xml
<properties>
    <scala.binary.version>2.13</scala.binary.version>
</properties>

```
 and then in `dependencies` add
 
```xml
<dependency>
    <groupId>com.daodecode</groupId>
    <artifactId>scalax-collection_${scala.binary.version}</artifactId>
    <version>0.3.0</version>
</dependency>
```