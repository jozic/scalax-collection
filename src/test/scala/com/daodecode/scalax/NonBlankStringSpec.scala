package com.daodecode.scalax

import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class NonBlankStringSpec extends FlatSpec with Matchers {

  "NonBlankString" should "return Some for non blank string" in {
    def randomWord =
      Seq.fill(Random.nextInt(10))(Random.nextPrintableChar()).mkString("")

    val nonBlankWords = Seq.fill(100)(randomWord).filter(_.trim.nonEmpty)
    nonBlankWords should not be empty
    nonBlankWords.foreach { word =>
      NonBlankString(word) should be(Some(word))
    }
  }

  it should "return None for blank strings" in {
    NonBlankString("   ") should be(None)
    NonBlankString("\n") should be(None)
    NonBlankString("\r\n") should be(None)
    NonBlankString("\t") should be(None)
  }

  it should "return None for null" in {
    NonBlankString(null) should be(None)
  }

  it should "return None for empty string" in {
    NonBlankString("") should be(None)
  }

  it should "have proper extractor" in {
    "a" should matchPattern {
      case NonBlankString("a") =>
    }

    "" should not matchPattern {
      case NonBlankString(_) =>
    }

    " \t " should not matchPattern {
      case NonBlankString(_) =>
    }

    (null: String) should not matchPattern {
      case NonBlankString(_) =>
    }
  }
}
