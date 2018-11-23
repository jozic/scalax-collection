package com.daodecode.scalax

import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class EmptyStringSpec extends FlatSpec with Matchers {

  "EmptyString" should "return false for non empty string" in {
    def randomWord =
      Seq.fill(Random.nextInt(10))(Random.nextPrintableChar()).mkString("")

    val nonEmptyWords = Seq.fill(100)(randomWord).filter(_.nonEmpty)
    nonEmptyWords should not be 'empty
    nonEmptyWords.foreach { word =>
      EmptyString(word) should be(false)
    }
  }

  it should "return false for blank strings" in {
    EmptyString("   ") should be(false)
    EmptyString("\n") should be(false)
    EmptyString("\r\n") should be(false)
    EmptyString("\t") should be(false)
  }

  it should "return true for null" in {
    EmptyString(null) should be(true)
  }

  it should "return true for empty string" in {
    EmptyString("") should be(true)
  }

  it should "have proper extractor" in {
    "a" should not matchPattern {
      case EmptyString() =>
    }

    "" should matchPattern {
      case EmptyString() =>
    }

    (null: String) should matchPattern {
      case EmptyString() =>
    }

    Option.empty[String] should not matchPattern {
      case Some(EmptyString()) =>
    }

    Option("") should matchPattern {
      case Some(EmptyString()) =>
    }

    Option("here") should not matchPattern {
      case Some(EmptyString()) =>
    }
  }

  it should "work as function" in {

    Seq("a", "", "b", "", "c").filterNot(EmptyString) should be(
      Seq("a", "b", "c"))

    Seq(Option("a"), None, Option("b"), Option(""), Option("c"))
      .flatMap(_.filterNot(EmptyString)) should be(Seq("a", "b", "c"))
  }

}
