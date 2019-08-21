package com.daodecode.scalax

import org.scalatest.{FlatSpec, Matchers}

import scala.util.Random

class NonBlankTrimmedStringSpec extends FlatSpec with Matchers {

  "NonBlankTrimmedString" should "return Some trimmed string for non blank string" in {
    def randomWord =
      Seq.fill(Random.nextInt(100))(Random.nextPrintableChar()).mkString("")

    val nonBlankWords = Seq.fill(1000)(randomWord).filter(_.trim.nonEmpty)
    nonBlankWords should not be empty
    nonBlankWords.foreach { word =>
      NonBlankTrimmedString(s"   $word \n ") should be(Some(word.trim))
    }
  }

  it should "return None for blank strings" in {
    NonBlankTrimmedString("   ") should be(None)
    NonBlankTrimmedString("\n") should be(None)
    NonBlankTrimmedString("\r\n") should be(None)
    NonBlankTrimmedString("\t") should be(None)
  }

  it should "return None for null" in {
    NonBlankTrimmedString(null) should be(None)
  }

  it should "return None for empty string" in {
    NonBlankTrimmedString("") should be(None)
  }


  it should "have proper extractor" in {
    (null: String) should not matchPattern {
      case NonBlankTrimmedString(_) =>
    }

    "" should not matchPattern {
      case NonBlankTrimmedString(_) =>
    }

    "   " should not matchPattern {
      case NonBlankTrimmedString(_) =>
    }

    "  \t \r \n " should not matchPattern {
      case NonBlankTrimmedString(_) =>
    }

    "  ab  \n" should matchPattern {
      case NonBlankTrimmedString("ab") =>
    }

    "  ab ba  " should matchPattern {
      case NonBlankTrimmedString("ab ba") =>
    }
  }
}
