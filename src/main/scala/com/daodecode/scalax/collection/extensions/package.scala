package com.daodecode.scalax.collection

import scala.collection._
import scala.collection.generic.CanBuildFrom

package object extensions {

  /**
   * @define coll collection
   */
  implicit class SeqLikeExtension[+A, +Repr](val seqLike: SeqLike[A, Repr]) extends AnyVal {

    /** Builds a new $coll from this $coll without any duplicate elements (as
      * determined by `==` after applying transforming function `f`).
      *
      * @return  A new $coll which contains the first occurrence of every element of this $coll.
      * @since 0.1.0
      */
    def distinctBy[B, That](f: A => B)(implicit cbf: CanBuildFrom[Repr, A, That]): That = {
      val b = cbf(seqLike.repr)
      val seen = mutable.HashSet[B]()
      for (x <- seqLike) {
        val fx = f(x)
        if (!seen(fx)) {
          b += x
          seen += fx
        }
      }
      b.result()
    }
  }

  /**
   * @define coll collection
   */
  implicit class GenIterableLikeExtension[+A, +Repr <: GenIterableLike[A, Repr]](val iterableLike: GenIterableLike[A, Repr]) extends AnyVal {

    /** Applies a binary operator to a start value and all elements of this $coll while predicate `p` is satisfied,
      * going left to right.
      *
      * @param   z    the start value.
      * @param   p    the predicate used to test if folding should continue.
      * @param   op   the binary operator.
      * @tparam  B    the result type of the binary operator.
      * @return  the result of inserting `op` between consecutive elements of this $coll,
      *          going left to right with the start value `z` on the left.
      * @since 0.1.0
      */
    def foldLeftWhile[B](z: B)(p: B => Boolean)(op: (B, A) => B): B = {
      var result = z
      val it = iterableLike.iterator
      while (it.hasNext && p(result)) {
        val next = it.next()
        result = op(result, next)
      }
      result
    }

    /** Applies a binary operator to all elements of this $coll and a start value while predicate `p` is satisfied,
      * going right to left.
      *
      * @param   z    the start value.
      * @param   p    the predicate used to test if folding should continue.
      * @param   op   the binary operator.
      * @tparam  B    the result type of the binary operator.
      * @return  the result of inserting `op` between consecutive elements of this $coll,
      *          going right to left with the start value `z` on the right.
      * @since 0.1.0
      */
    def foldRightWhile[B](z: B)(p: B => Boolean)(op: (A, B) => B): B =
      if (iterableLike.isEmpty) z
      else {
        val result = iterableLike.tail.foldRightWhile(z)(p)(op)
        if (p(result)) op(iterableLike.head, result) else result
      }
  }

}

