package com.daodecode.scalax.collection

import scala.collection._
import scala.collection.generic.CanBuildFrom

package object extensions {

  /**
   * @define coll collection
   */
  implicit class SeqLikeExtension[A, Repr](val seqLike: SeqLike[A, Repr]) extends AnyVal {

    /** Builds a new $coll from this $coll without any duplicate elements (as
      * determined by `==` after applying transforming function `f`).
      *
      * @return  A new $coll which contains the first occurrence of every element of this $coll.
      * @since 0.1.0
      */
    def distinctBy[B](f: A => B)(implicit cbf: CanBuildFrom[Repr, A, Repr]): Repr = {
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
  implicit class IterableLikeExtension[A, Repr <: IterableLike[A, Repr]](val iterableLike: IterableLike[A, Repr]) extends AnyVal {

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

    /**
     * Converts this $coll to a map. This method is unavailable unless
     * the elements are members of Tuple2[K, V].
     * The method is different from `toMap` in the way that values for
     * duplicate keys will be collected into $coll,
     * so all the values will be in the result map.
     *
     * Example:
     * {{{
     * scala> val cm = List(1 -> "1", 2 -> "2", 1 -> "11").toCompleteMap
     * cm: scala.collection.immutable.Map[Int,List[String]] = Map(2 -> List(2), 1 -> List(1, 11))
     * }}}
     *
     * @return a map of type `immutable.Map[K, That]` where `That` is a $coll of `V`
     *
     * @since 0.1.1
     **/
    def toCompleteMap[K, V, That](implicit ev: A <:< (K, V), cbf: CanBuildFrom[Repr, V, That]): immutable.Map[K, That] = {
      val m = mutable.Map.empty[K, mutable.Builder[V, That]]
      for (keyValue <- iterableLike) {
        val builder = m.getOrElseUpdate(keyValue._1, cbf(iterableLike.repr))
        builder += keyValue._2
      }
      val b = immutable.Map.newBuilder[K, That]
      for ((k, vBuilder) <- m)
        b += ((k, vBuilder.result()))
      b.result()
    }

  }

}

