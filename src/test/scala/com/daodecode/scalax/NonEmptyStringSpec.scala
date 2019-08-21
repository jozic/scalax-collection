package com.daodecode.scalax

import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class NonEmptyStringSpec extends FlatSpec with Matchers {

  "NonEmptyString" should "return Some for non empty string" in {
    def randomWord =
      Seq.fill(Random.nextInt(10))(Random.nextPrintableChar()).mkString("")

    val nonEmptyWords = Seq.fill(100)(randomWord).filter(_.nonEmpty)
    nonEmptyWords should not be empty
    nonEmptyWords.foreach { word =>
      NonEmptyString(word) should be(Some(word))
    }
  }

  it should "return Some for blank strings" in {
    NonEmptyString("   ") should be(Some("   "))
    NonEmptyString("\n") should be(Some("\n"))
    NonEmptyString("\r\n") should be(Some("\r\n"))
    NonEmptyString("\t") should be(Some("\t"))
  }

  it should "return None for null" in {
    NonEmptyString(null) should be(None)
  }

  it should "return None for empty string" in {
    NonEmptyString("") should be(None)
  }

  it should "have proper extractor" in {
    "a" should matchPattern {
      case NonEmptyString("a") =>
    }

    "" should not matchPattern {
      case NonEmptyString(_) =>
    }

    (null: String) should not matchPattern {
      case NonEmptyString(_) =>
    }

    Option.empty[String] should not matchPattern {
      case Some(NonEmptyString(_)) =>
    }

    Option("") should not matchPattern {
      case Some(NonEmptyString(_)) =>
    }

    Option("here") should matchPattern {
      case Some(NonEmptyString("here")) =>
    }

    Option("here") match {
      case Some(NonEmptyString(s)) => s should be("here")
    }
  }

  it should "work as function" in {

    Seq("a", "", "b", "", "c").flatMap(NonEmptyString(_)) should be(Seq("a", "b", "c"))

    Seq("a", "", "b", "", "c").flatMap(NonEmptyString.apply) should be(Seq("a", "b", "c"))

    Seq(Option("a"), None, Option("b"), Option(""), Option("c"))
      .flatMap(_.flatMap(NonEmptyString)) should be(Seq("a", "b", "c"))
  }

}
