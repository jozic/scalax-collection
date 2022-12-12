package com.daodecode.scalax
package collection.concurrent

import java.util.concurrent.ConcurrentHashMap

import scala.collection.JavaConverters._
import scala.collection.mutable

class ConcurrentSpec extends ScalaxFlatSpec {

  "ConcurrentMap.empty" should "create an empty mutable concurrent Map" in {
    val emptyConcurrentMap = ConcurrentMap.empty[Int, String]
    emptyConcurrentMap shouldBe empty

    emptyConcurrentMap shouldBe a[ConcurrentMap[_, _]]
    emptyConcurrentMap.asJava shouldBe a[ConcurrentHashMap[_, _]]

    emptyConcurrentMap.putIfAbsent(1, "1") should be(None)

    emptyConcurrentMap should contain theSameElementsAs Map(1 -> "1")
  }

  "ConcurrentSet.empty" should "create an empty mutable concurrent Set" in {
    val emptyConcurrentSet = ConcurrentSet.empty[Int]
    emptyConcurrentSet shouldBe empty

    emptyConcurrentSet shouldBe a[mutable.Set[_]]

    emptyConcurrentSet.add(1) should be(true)

    emptyConcurrentSet should contain theSameElementsAs Set(1)
  }

}
