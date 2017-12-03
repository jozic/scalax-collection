package com.daodecode.scalax.collection.extensions

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.ListBuffer

class IterableLikeExtensionSpec extends FlatSpec with Matchers {

  "foldLeftWhile" should "behave as regular foldLeft if while condition is true" in {

    List.empty[Int].foldLeftWhile(0)(_ => true)(_ + _) should be(List.empty[Int].foldLeft(0)(_ + _))
    (1 to 10).foldLeftWhile(0)(_ => true)(_ + _) should be((1 to 10).foldLeft(0)(_ + _))
    (1 to 10).foldLeftWhile(0)(_ > -1)(_ + _) should be((1 to 10).foldLeft(0)(_ + _))
  }

  it should "respect while condition and return as soon as it is false" in {
    (1 to 10).foldLeftWhile(0)(_ < 12)(_ + _) should be(15)

    var x = 0
    (1 to 10).foldLeftWhile(0)(_ < 12)((s, i) => {
      x += 1
      s + i
    })
    x should be(5)
  }

  "foldRightWhile" should "behave as regular foldRight if while condition is true" in {

    List.empty[Int].foldRightWhile(0)(_ => true)(_ + _) should be(List.empty[Int].foldRight(0)(_ + _))
    (1 to 10).foldRightWhile(0)(_ => true)(_ + _) should be((1 to 10).foldRight(0)(_ + _))
    (1 to 10).foldRightWhile(0)(_ > -1)(_ + _) should be((1 to 10).foldRight(0)(_ + _))
  }

  it should "respect while condition and return as soon as it is false" in {
    (1 to 10).foldRightWhile(0)(_ < 12)(_ + _) should be(19)

    var x = 0
    (1 to 10).foldRightWhile(0)(_ < 12)((s, i) => {
      x += 1
      s + i
    })
    x should be(2)
  }

  "toCompleteMap" should "group all values by keys" in {
    List("1" -> "10", "2" -> "20", "1" -> "11").toCompleteMap should be(Map("1" -> List("10", "11"), "2" -> List("20")))
    ListBuffer("1" -> "10", "2" -> "20", "1" -> "11").toCompleteMap should be(Map("1" -> ListBuffer("10", "11"), "2" -> ListBuffer("20")))
  }

  it should "return empty map if source is empty" in {
    List.empty[(String, String)].toCompleteMap should be(Map[String, List[String]]())
  }

  "mapToMap" should "create a map" in {
    List("1", "2", "1").mapToMap(si => si.toInt -> si) should be(Map(1 -> "1", 2 -> "2"))
    List("1" -> "one", "2" -> "two")
      .mapToMap { case (i, s) => i.toInt -> s } should be(Map(1 -> "one", 2 -> "two"))
  }

  it should "return empty map if source is empty" in {
    List.empty[String].mapToMap(si => si.toInt -> si) should be(Map[Int, String]())
    List.empty[(String, String)]
      .mapToMap { case (i, s) => i.toInt -> s } should be(Map[Int, String]())
  }

  "toMapWithKey" should "create a map" in {
    List("1", "2", "1").toMapWithKey(_.toInt) should be(Map(1 -> "1", 2 -> "2"))
  }

  it should "return empty map if source is empty" in {
    List.empty[String].toMapWithKey(_.toInt) should be(Map[Int, String]())
  }

  "toMapWithValue" should "create a map" in {
    List("1", "2", "1").toMapWithValue(_.toInt) should be(Map("1" -> 1, "2" -> 2))
  }

  it should "return empty map if source is empty" in {
    List.empty[String].toMapWithValue(_.toInt) should be(Map[String, Int]())
  }

  "withFrequency" should "calculate frequency of each element in the source" in {
    Iterable("a", "b", "c", "a", "b", "d").withFrequency should be(Map("a" -> 2, "b" -> 2, "c" -> 1, "d" -> 1))
  }

  it should "return empty map if source is empty" in {
    List.empty[String].withFrequency should be(Map[String, Int]())
  }

  "withFrequencyBy" should "calculate frequency of each element in the source after applying function f" in {
    Iterable("ab", "bc", "cd", "ae", "bk", "dd").withFrequencyBy(_.head) should be(
      Map('a' -> 2, 'b' -> 2, 'c' -> 1, 'd' -> 1))
  }

  it should "return empty map if source is empty" in {
    List.empty[String].withFrequencyBy(_.head) should be(Map[String, Int]())
  }

  "minOption" should "return None if iterable is empty" in {
    Iterable.empty[Int].minOption should be(None)
  }

  it should "return smallest element wrapped in Option if iterable is not empty" in {
    Iterable(2, 1, 3).minOption should be(Some(1))
  }

  "minOptionBy" should "return None if iterable is empty" in {
    Iterable.empty[Int].minOptionBy(_ * -1) should be(None)
  }

  it should "return smallest element wrapped in Option if iterable is not empty" in {
    Iterable(2, 1, 3).minOptionBy(_ * -1) should be(Some(3))
    case class A(i: Int)
    Iterable(A(2), A(1), A(3)).minOptionBy(_.i) should be(Some(A(1)))
  }

  "maxOption" should "return None if iterable is empty" in {
    Iterable.empty[Int].maxOption should be(None)
  }

  it should "return largest element wrapped in Option if iterable is not empty" in {
    Iterable(2, 1, 3).maxOption should be(Some(3))
  }

  "maxOptionBy" should "return None if iterable is empty" in {
    Iterable.empty[Int].maxOptionBy(_ * -1) should be(None)
  }

  it should "return largest element wrapped in Option if iterable is not empty" in {
    Iterable(2, 1, 3).maxOptionBy(_ * -1) should be(Some(1))
    case class A(i: Int)
    Iterable(A(2), A(1), A(3)).maxOptionBy(_.i) should be(Some(A(3)))
  }

}
