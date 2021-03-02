scalax-collection [![Build Status](https://travis-ci.org/jozic/scalax-collection.svg?branch=master)](https://travis-ci.org/jozic/scalax-collection) [![Coverage Status](https://coveralls.io/repos/jozic/scalax-collection/badge.svg)](https://coveralls.io/r/jozic/scalax-collection)
=================

A small library of extension methods for standard scala collections library. 
Published to maven central.

| 2.10 | 2.11 | 2.12 | 2.13 |
|------|------|------|------|
|[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.10/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.10) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.11) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.12/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.12) | [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.13/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.daodecode/scalax-collection_2.13) |


## Collection Examples

An import needed for examples to work:
```scala
import com.daodecode.scalax.collection.extensions._
```

### distinctBy

preserving first duplicate
```scala
val xs = List(1 -> "one", 1 -> "ten", 2 -> "two", 2 -> "twenty").distinctBy(_._1)
// xs: List[(Int, String)] = List((1, "one"), (2, "two"))
```

*NOTE: Since Scala 2.13 this method is available in standard library*

### distinctByUsing

or preserving any duplicate you want
```scala
val xs = List(1 -> "one", 1 -> "ten", 2 -> "two", 2 -> "twenty").distinctByUsing(_._1, takeFirst = _._2.length > _._2.length)
// xs: List[(Int, String)] = List((1, "ten"), (2, "twenty"))
```

*NOTE: Before 0.3.0 this method was named `distinctBy`*

### foldLeftWhile/foldRightWhile

```scala
val xs = Iterable(List(1,2,3), List(4,5), List(6,7,8,9)).foldLeftWhile(List.empty[Int])(_.size < 4){ case (acc, l) => acc ++ l }
// xs: List[Int] = List(1, 2, 3, 4, 5)
```

### toCompleteMap

*since Scala 2.13 can be seen as equivalent to `groupMap(_._1)(_._2)`*

```scala
val cm = List(1 -> "1", 2 -> "2", 1 -> "11").toCompleteMap
// cm: Map[Int, List[String]] = Map(1 -> List("1", "11"), 2 -> List("2"))
```

### mapToMap

*can be seen as more efficient replacement for `map().toMap` combination*

```scala
val m = List("1" -> "one", "2" -> "two").mapToMap { case (i, s) => i.toInt -> s }
// m: Map[Int, String] = Map(1 -> "one", 2 -> "two")
```

### toMapWithKey

*since Scala 2.13 can be seen as equivalent to `groupMapReduce(f)(identity)((b,_) => b)`*

```scala
val m = List("1", "2", "1").toMapWithKey(_.toInt)
// m: Map[Int, String] = Map(1 -> "1", 2 -> "2")
```

### toMapWithValue

*since Scala 2.13 can be seen as equivalent to `groupMapReduce(identity)(f)((b,_) => b)`*

```scala
val m = List("1", "2", "1").toMapWithValue(_.toInt)
// m: Map[String, Int] = Map("1" -> 1, "2" -> 2)
```

### withFrequency

*since Scala 2.13 can be seen as equivalent to `groupMapReduce(identity)(_ => 1)(_ + _)`*

```scala
val fm = List("a", "b", "c", "a", "b", "d").withFrequency
// fm: Map[String, Int] = Map("a" -> 2, "b" -> 2, "c" -> 1, "d" -> 1)
```

### withFrequencyBy

*since Scala 2.13 can be seen as equivalent to `groupMapReduce(f)(_ => 1)(_ + _)`*

```scala
val fm = List("ab", "bc", "cd", "ab", "bc", "de").withFrequencyBy(_.head)
// fm: Map[Char, Int] = Map('a' -> 2, 'b' -> 2, 'c' -> 1, 'd' -> 1)
```

### mergedWith

Merges two maps using provided function to merge values for duplicate keys
```scala
val merged = Map("1" -> 1, "2" -> 2).mergedWith(Map("1" -> 1, "2" -> 2))(_ + _)
// merged: Map[String, Int] = Map("1" -> 2, "2" -> 4)
```

### minOption/minOptionBy

Finds the smallest element wrapped in `Option` or `None` if iterable is empty
```scala
val m1 = List.empty[Int].minOption
// m1: Option[Int] = None

val m2 = List(1,2,1).minOptionBy(_ * -1)
// m2: Option[Int] = Some(value = 2)
```

*NOTE: Since Scala 2.13 this is available in standard library*

### maxOption/maxOptionBy

Finds the largest element wrapped in `Option` or `None` if iterable is empty
```scala
val m1 = List.empty[Int].maxOption
// m1: Option[Int] = None

val m2 = List(1,2,1).maxOptionBy(_ * -1)
// m2: Option[Int] = Some(value = 1)
```

*NOTE: Since Scala 2.13 this is available in standard library*

## Strings Examples

An import needed for examples to work:
```scala
import com.daodecode.scalax._
```
### NonEmptyString

```scala
NonEmptyString(null)
// res0: Option[String] = None
NonEmptyString("")
// res1: Option[String] = None
NonEmptyString(" a ")
// res2: Option[String] = Some(value = " a ")

(null: String) match {
  case NonEmptyString(_) => "boo" 
  case _ => "works!"
}
// res3: String = "works!"

"" match {
  case NonEmptyString(_) => "boo" 
  case _ => "works!"
}
// res4: String = "works!"

"works!" match {
  case NonEmptyString(s) => s 
  case _ => "boo"
}
// res5: String = "works!"
```

### NonBlankString

```scala
NonBlankString(null)
// res6: Option[String] = None
NonBlankString("")
// res7: Option[String] = None
NonBlankString(" \n \r \t ")
// res8: Option[String] = None
NonBlankString(" a ")
// res9: Option[String] = Some(value = " a ")

(null: String) match {
  case NonBlankString(_) => "boo" 
  case _ => "works!"
}
// res10: String = "works!"

"" match {
  case NonBlankString(_) => "boo" 
  case _ => "works!"
}
// res11: String = "works!"

"   \t " match {
  case NonBlankString(_) => "boo" 
  case _ => "works!"
}
// res12: String = "works!"

"works!" match {
  case NonBlankString(s) => s 
  case _ => "boo"
}
// res13: String = "works!"
```

### NonBlankTrimmedString

```scala
NonBlankTrimmedString(null)
// res14: Option[String] = None
NonBlankTrimmedString("")
// res15: Option[String] = None
NonBlankTrimmedString(" \n \r \t ")
// res16: Option[String] = None
NonBlankTrimmedString(" a ")
// res17: Option[String] = Some(value = "a")

(null: String) match {
  case NonBlankTrimmedString(_) => "boo" 
  case _ => "works!"
}
// res18: String = "works!"

"" match {
  case NonBlankTrimmedString(_) => "boo" 
  case _ => "works!"
}
// res19: String = "works!"

"   \t " match {
  case NonBlankTrimmedString(_) => "boo" 
  case _ => "works!"
}
// res20: String = "works!"

"  works!\n  " match {
  case NonBlankTrimmedString(s) => s 
  case _ => "boo"
}
// res21: String = "works!"
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