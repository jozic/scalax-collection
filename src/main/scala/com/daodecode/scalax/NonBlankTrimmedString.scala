package com.daodecode.scalax

object NonBlankTrimmedString extends (String => Option[String]) {

  /**
    * @param s a String to check for non emptiness or trim
    * @return `None` if `s` is `null` or `""`, `Some(s.trim)` otherwise
    * @since 0.2.1
    *
    *        Example
    *        {{{
    *           scala> NonBlankTrimmedString(null)
    *           res0: Option[String] = None
    *           scala> NonBlankTrimmedString("")
    *           res1: Option[String] = None
    *           scala> NonBlankTrimmedString(" boo ")
    *           res2: Option[String] = Some(boo)
    *        }}}
    */
  @inline
  def apply(s: String): Option[String] =
    if (s == null || s.trim.isEmpty) None else Some(s.trim)

  /**
    * Extractor for non-empty trimmed strings
    *
    * @param s String to check for non emptiness or trim
    * @return `None` if `s` is `null` or `""`, `Some(s.trim)` otherwise
    * @since 0.2.1
    *
    *        Example
    *        {{{
    *           scala> null match {
    *                |  case NonBlankTrimmedString(_) => "no way!"
    *                |  case _ => "works!"
    *                |}
    *           res0: String = works!
    *           scala> "" match {
    *                |  case NonBlankTrimmedString(_) => "no way!"
    *                |  case _ => "works!"
    *                |}
    *           res1: String = works!
    *           scala> "  works!  " match {
    *                |  case NonBlankTrimmedString(s) => s
    *                |  case _ => "no way!"
    *                |}
    *           res2: String = works!
    *        }}}
    */
  @inline
  def unapply(s: String): Option[String] = apply(s)
}
