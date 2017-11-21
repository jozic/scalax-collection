package com.daodecode.scalax

object NonBlankString extends (String => Option[String]){

  /**
    * @param s a String to check for non blankness
    * @return `None` if `s` is `null` or `""` or contains only whitespace chars, `Some(s)` otherwise
    * @since 0.2.1
    *
    *        Example
    *        {{{
    *           scala> NonBlankString(null)
    *           res0: Option[String] = None
    *           scala> NonBlankString("")
    *           res1: Option[String] = None
    *           scala> NonBlankString(" \t ")
    *           res2: Option[String] = None
    *           scala> NonBlankString(" boo ")
    *           res3: Option[String] = Some( boo )
    *        }}}
    */
  @inline
  def apply(s: String): Option[String] =
    if (s == null || s.trim.isEmpty) None else Some(s)

  /**
    * Extractor for non-blank strings
    *
    * @param s String to check for non blankness
    * @return `None` if `s` is `null` or `""` or contains only whitespace chars, `Some(s)` otherwise
    * @since 0.2.1
    *
    *        Example
    *        {{{
    *           scala> null match {
    *                |  case NonBlankString(_) => "no way!"
    *                |  case _ => "works!"
    *                |}
    *           res0: String = works!
    *           scala> "" match {
    *                |  case NonBlankString(_) => "no way!"
    *                |  case _ => "works!"
    *                |}
    *           res1: String = works!
    *           scala> " \n\r\t " match {
    *                |  case NonBlankString(_) => "no way!"
    *                |  case _ => "works!"
    *                |}
    *           res2: String = works!
    *           scala> "works!" match {
    *                |  case NonBlankString(s) => s
    *                |  case _ => "no way!"
    *                |}
    *           res3: String = works!
    *        }}}
    */
  @inline
  def unapply(s: String): Option[String] = apply(s)

}
