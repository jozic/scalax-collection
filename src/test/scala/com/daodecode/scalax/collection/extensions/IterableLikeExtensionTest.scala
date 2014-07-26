package com.daodecode.scalax.collection.extensions

import org.scalatest.Matchers

class IterableLikeExtensionTest extends org.scalatest.FlatSpec with Matchers {

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


}
