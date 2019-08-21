package com.daodecode.scalax.collection.extensions

import org.scalatest.{FlatSpec, Matchers}

class SeqOpsExtensionSpec extends FlatSpec with Matchers {

  case class Person(fName: String, lName: String, age: Int)

  val eugeneP = Person("Eugene", "Platonov", 27)
  val xeniya = Person("Xeniya", "Skrypnyk", 27)
  val eugeneM = Person("Eugene", "Medvediev", 31)
  val vasiliy = Person("Vasiliy", "Platonov", 2)

  val alwaysFirst: (Person, Person) => Boolean = (_, _) => true

  "distinctByUsing" should "return empty seq if source is empty" in {
    val empty = Seq.empty[Person]
    assert(empty.distinctByUsing(_.lName, alwaysFirst) == empty)
  }

  it should "evaluate transforming function only once per each element" in {

    def assertEvaluateOnlyOnce(emptySeq: Seq[Person]): Unit = {
      val people = emptySeq ++ Iterable(eugeneP, xeniya, eugeneM, vasiliy)
      var x = 0
      people.distinctByUsing({ p => x += 1; p.fName}, alwaysFirst)
        .toList should be(List(eugeneP, xeniya, vasiliy))
      assert(x == 4, "Looks like function has been evaluated wrong number of times")
    }

    assertEvaluateOnlyOnce(Seq.empty[Person])
    assertEvaluateOnlyOnce(Vector.empty[Person])
  }

  it should "respect selector" in {
    val people = Seq(eugeneM, xeniya, eugeneP, vasiliy)

    people.distinctByUsing(_.fName, alwaysFirst) should be(Seq(eugeneM, xeniya, vasiliy))

    people.distinctByUsing(_.fName, (p1, p2) => p1.lName == "Platonov") should be(Seq(eugeneP, xeniya, vasiliy))
  }
}
