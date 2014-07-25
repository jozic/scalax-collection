package com.daodecode.scalax.collection

import scala.collection.generic.CanBuildFrom
import scala.collection.{SeqLike, mutable}

package object extensions {

  implicit class SeqLikeExtension[+A, +Repr](val seqLike: SeqLike[A, Repr]) extends AnyVal {

    /** Builds a new collection from this collection without any duplicate elements (as
      * determined by `==` after applying transforming function `f`).
      *
      * @return  A new collection which contains the first occurrence of every element of this collection.
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

}
