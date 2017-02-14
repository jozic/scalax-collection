package com.daodecode.scalax.collection.extensions

import org.scalatest.Matchers

class SeqLikeExtensionTest extends org.scalatest.FlatSpec with Matchers {

  case class Person(fName: String, lName: String, age: Int)

  val eugeneP = Person("Eugene", "Platonov", 27)
  val xeniya = Person("Xeniya", "Skrypnyk", 27)
  val eugeneM = Person("Eugene", "Medvediev", 31)
  val vasiliy = Person("Vasiliy", "Platonov", 2)

  "distinctBy" should "keep only first unique element based on transforming function" in {
    val people = Seq(eugeneP, xeniya, eugeneM, vasiliy)

    people.distinctBy(_.fName) should be(Seq(eugeneP, xeniya, vasiliy))
    people.distinctBy(_.lName) should be(Seq(eugeneP, xeniya, eugeneM))
    people.distinctBy(_.age) should be(Seq(eugeneP, eugeneM, vasiliy))
  }

  it should "return empty seq if source is empty" in {
    val empty = Seq.empty[Person]
    assert(empty.distinctBy(_.lName) == empty)
  }

  it should "evaluate transforming function only once per each element" in {

    def assertEvaluateOnlyOnce(emptySeq: Seq[Person]): Unit = {
      val people = emptySeq ++ Iterable(eugeneP, xeniya, eugeneM, vasiliy)
      var x = 0
      people.distinctBy { p => x += 1; p.fName }.toList should be(List(eugeneP, xeniya, vasiliy))
      assert(x == 4, "Looks like function has been evaluated wrong number of times")
    }

    assertEvaluateOnlyOnce(Seq.empty[Person])
    assertEvaluateOnlyOnce(Stream.empty[Person])
  }

  it should "respect selector if one is provided" in {
    val people = Seq(eugeneM, xeniya, eugeneP, vasiliy)

    people.distinctBy(_.fName) should be(Seq(eugeneM, xeniya, vasiliy))
    people.distinctBy(_.fName, (p1, p2) => p1.lName == "Platonov") should be(Seq(eugeneP, xeniya, vasiliy))
  }
}
