package com.daodecode.scalax.collection.concurrent

import java.util.concurrent.ConcurrentHashMap

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.JavaConverters._

class ConcurrentSpec extends FlatSpec with Matchers {

  "ConcurrentMap.empty" should "create an empty mutable concurrent Map" in {
    val emptyConcurrentMap = ConcurrentMap.empty[Int, String]
    emptyConcurrentMap should be('empty)

    emptyConcurrentMap shouldBe a[ConcurrentMap[_, _]]
    emptyConcurrentMap.asJava shouldBe a[ConcurrentHashMap[_, _]]

    emptyConcurrentMap.putIfAbsent(1, "1") should be(None)

    emptyConcurrentMap should contain theSameElementsAs Map(1 -> "1")
  }

  "ConcurrentSet.empty" should "create an empty mutable concurrent Set" in {
    val emptyConcurrentSet = ConcurrentSet.empty[Int]
    emptyConcurrentSet should be('empty)

    emptyConcurrentSet shouldBe a[collection.mutable.Set[_]]

    emptyConcurrentSet.add(1) should be(true)

    emptyConcurrentSet should contain theSameElementsAs Set(1)
  }

}
