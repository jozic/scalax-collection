package com.daodecode.scalax

object NonEmptyString extends (String => Option[String]) {

  /**
    * @param s a String to check for non emptiness
    * @return `None` if `s` is `null` or `""`, `Some(s)` otherwise
    * @since 0.2.1
    *
    *        Example
    *        {{{
    *           scala> NonEmptyString(null)
    *           res0: Option[String] = None
    *           scala> NonEmptyString("")
    *           res1: Option[String] = None
    *           scala> NonEmptyString(" boo ")
    *           res2: Option[String] = Some( boo )
    *        }}}
    */
  @inline
  def apply(s: String): Option[String] =
    if (s == null || s.isEmpty) None else Some(s)

  /**
    * Extractor for non-empty strings
    *
    * @param s String to check for non emptiness
    * @return `None` if `s` is `null` or `""`, `Some(s)` otherwise
    * @since 0.2.1
    *
    *        Example
    *        {{{
    *           scala> null match {
    *                |  case NonEmptyString(_) => "no way!"
    *                |  case _ => "works!"
    *                |}
    *           res0: String = works!
    *           scala> "" match {
    *                |  case NonEmptyString(_) => "no way!"
    *                |  case _ => "works!"
    *                |}
    *           res1: String = works!
    *           scala> "works!" match {
    *                |  case NonEmptyString(s) => s
    *                |  case _ => "no way!"
    *                |}
    *           res2: String = works!
    *        }}}
    */
  @inline
  def unapply(s: String): Option[String] = apply(s)

}
