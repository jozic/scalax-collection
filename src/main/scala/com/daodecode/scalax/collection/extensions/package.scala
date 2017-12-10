package com.daodecode.scalax.collection

import scala.collection._
import scala.collection.generic.CanBuildFrom
import scala.util.control.Breaks

package object extensions {

  /**
   * @define coll sequence
   */
  implicit class SeqLikeExtension[A, Repr](val seqLike: SeqLike[A, Repr]) extends AnyVal {

    /** Builds a new $coll from this $coll without any duplicate elements (as
      * determined by `==` after applying transforming function `f`).
      * First of all the duplicates is preserved
      *
      * Example:
      * {{{
      *   scala> List(1 -> "one", 1 -> "ten", 2 -> "two", 2 -> "twenty").distinctBy(_._1)
      *   res1: List[(Int, String)] = List((1,one), (2,two))
      * }}}
      *
      * @return  A new $coll which contains the first occurrence of every element of this $coll.
      * @since 0.1.0
      */
    def distinctBy[B](f: A => B)(implicit cbf: CanBuildFrom[Repr, A, Repr]): Repr =
      distinctBy(f, (_, _) => true)(cbf)

    /** Builds a new $coll from this $coll without any duplicate elements (as
      * determined by `==` after applying transforming function `f`).
      * Function `takeFirst` defines which of duplicates will be preserved.
      * If it returns `true` first of compared duplicates will be kept, second one otherwise.
      *
      * Example:
      * {{{
      *   scala> List(1 -> "one", 1 -> "ten", 2 -> "two", 2 -> "twenty").
      *        | distinctBy(_._1, takeFirst = _._2.length > _._2.length)
      *   res1: List[(Int, String)] = List((1,ten), (2,twenty))
      * }}}
      *
      * @return  A new $coll which contains selected occurrence of every element of this $coll.
      * @since 0.1.1
      */
    def distinctBy[B](f: A => B, takeFirst: (A, A) => Boolean)(implicit cbf: CanBuildFrom[Repr, A, Repr]): Repr = {
      val seen = mutable.LinkedHashMap.empty[B, A]
      for (x <- seqLike) {
        val fx = f(x)
        seen.get(fx) match {
          case Some(a) => seen += fx -> (if (takeFirst(a, x)) a else x)
          case _ => seen += fx -> x
        }
      }
      (cbf(seqLike.repr) ++= seen.values).result()
    }
  }

  /**
   * @define coll traversable
   */
  implicit class TraversableLikeExtension[A, Repr <: TraversableLike[A, Repr]](val traversableLike: TraversableLike[A, Repr]) extends AnyVal {

    /** Applies a binary operator to a start value and all elements of this $coll while predicate `p` is satisfied,
      * going left to right.
      *
      * Example:
      * {{{
      *   scala> Iterable(List(1,2,3), List(4,5), List(6,7,8,9)).
      *        | foldLeftWhile(List.empty[Int])(_.size < 4){ case (acc, l) => acc ++ l }
      *   res1: List[Int] = List(1, 2, 3, 4, 5)
      * }}}
      *
      * @param   z    the start value.
      * @param   p    the predicate used to test if folding should continue.
      * @param   op   the binary operator.
      * @tparam  B    the result type of the binary operator.
      * @return  the result of inserting `op` between consecutive elements of this $coll while predicate `p` is satisfied,
      *          going left to right with the start value `z` on the left.
      * @since 0.1.0
      */
    def foldLeftWhile[B](z: B)(p: B => Boolean)(op: (B, A) => B): B = {
      import Breaks._
      var result = z
      breakable {
        for (a <- traversableLike) {
          if (!p(result)) break
          result = op(result, a)
        }
      }
      result
    }

    /** Applies a binary operator to all elements of this $coll and a start value while predicate `p` is satisfied,
      * going right to left.
      *
      * Example:
      * {{{
      *   scala> Iterable(List(1,2,3), List(4,5), List(6,7,8,9)).
      *        | foldRightWhile(List.empty[Int])(_.size < 5){ case (l, acc) =>  l ::: acc }
      *   res1: List[Int] = List(4, 5, 6, 7, 8, 9)
      * }}}
      *
      * @param   z    the start value.
      * @param   p    the predicate used to test if folding should continue.
      * @param   op   the binary operator.
      * @tparam  B    the result type of the binary operator.
      * @return  the result of inserting `op` between consecutive elements of this $coll while predicate `p` is satisfied,
      *          going right to left with the start value `z` on the right.
      * @since 0.1.0
      */
    def foldRightWhile[B](z: B)(p: B => Boolean)(op: (A, B) => B): B =
      if (traversableLike.isEmpty) z
      else {
        val result = traversableLike.tail.foldRightWhile(z)(p)(op)
        if (p(result)) op(traversableLike.head, result) else result
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
     *   scala> List(1 -> "1", 2 -> "2", 1 -> "11").toCompleteMap
     *   res1: scala.collection.immutable.Map[Int,List[String]] = Map(2 -> List(2), 1 -> List(1, 11))
     * }}}
     *
     * @return a map of type `immutable.Map[K, That]` where `That` is a $coll of `V`
     * @since 0.1.1
     **/
    def toCompleteMap[K, V, That](implicit ev: A <:< (K, V), cbf: CanBuildFrom[Repr, V, That]): immutable.Map[K, That] = {
      val m = mutable.Map.empty[K, mutable.Builder[V, That]]
      for (keyValue <- traversableLike) {
        val builder = m.getOrElseUpdate(keyValue._1, cbf(traversableLike.repr))
        builder += keyValue._2
      }
      m.mapToMap { case (k, b) => k -> b.result() }
    }

    /**
      * Converts this $coll to an immutable map using provided function `f`
      * to generate Map keys and values from elements of this $coll.
      * It can be seen as more efficient combination of `map() and toMap`
      *
      * @param f Function to generate key and value from element of this $coll
      * @tparam K Key type of the result Map
      * @tparam V Value type of the result Map
      * @return Immutable Map. It's possible to loose some of the original elements
      *         if function `f` returns same key for more than one element
      * @since 0.2.0
      */
    def mapToMap[K, V](f: A => (K, V)): immutable.Map[K, V] = {
      val b = immutable.Map.newBuilder[K, V]
      for (a <- traversableLike)
        b += f(a)
      b.result()
    }

    /**
      * Converts this $coll to a map where key is derived from each item using function `f`
      * @param f Function to build a key for the result Map
      * @tparam K Key type of the result Map
      * @return Immutable Map with original elements as values.
      *         It's possible to loose some of the original elements
      *         if function `f` returns same key for more than one element
      * @since 0.2.0
      */
    def toMapWithKey[K](f: A => K): immutable.Map[K, A] = mapToMap(a => f(a) -> a)

    /**
      * Converts this $coll to a map where value is derived from each item using function `f`
      * @param f Function to build a value for the result Map
      * @tparam V Value type of the result Map
      * @return Immutable Map with original elements as keys.
      *         It's possible to loose some of the original elements, as Maps don't allow duplicate keys
      * @since 0.2.0
      */
    def toMapWithValue[V](f: A => V): immutable.Map[A, V] = mapToMap(a => a -> f(a))

    /**
     * Creates a new immutable `Map` with unique elements of this $coll as keys and
     * count of duplicates ((as determined by `==`) (frequency) as values.
     *
     * Example:
     * {{{
     *   scala> List("a", "b", "c", "a", "b", "d").withFrequency
     *   res1: scala.collection.immutable.Map[String,Int] = Map(b -> 2, d -> 1, a -> 2, c -> 1)
     * }}}
     *
     * @return a map of type immutable.Map[A, Int] where Int represents a frequency of key A in original $coll
     * @since 0.1.1
     */
    def withFrequency: immutable.Map[A, Int] = withFrequencyBy(identity)

    /**
     * Creates a new immutable `Map` with unique elements resulting from applying function `f`
     * to elements of this $coll as keys and
     * count of duplicates ((as determined by `==`) (frequency) as values.
     *
     * Example:
     * {{{
     *   scala> List("ab", "bc", "cd", "ab", "bc", "de").withFrequencyBy(_.head)
     *   res1: scala.collection.immutable.Map[Char  ,Int] = Map(b -> 2, d -> 1, a -> 2, c -> 1)
     * }}}
     *
     * @return a map of type immutable.Map[B, Int] where Int represents a frequency of keys,
     *         obtained from original $coll by applying function `f` to elements
     * @since 0.2.0
     */
    def withFrequencyBy[B](f: A => B): immutable.Map[B, Int] = {
      val m = mutable.Map.empty[B, Int]
      for (a <- traversableLike) {
        val key = f(a)
        val currentFreq = m.getOrElse(key, 0)
        m.update(key, currentFreq + 1)
      }
      m.toMap
    }

    /**
      * Finds the largest element wrapped in `Option` or `None` if $coll is empty
      *
      * Example:
      * {{{
      *   scala> List(1,2,1).maxOption
      *   res1: Option[Int] = Some(2)
      *
      *   scala> List.empty[Int].maxOption
      *   res2: Option[Int] = None
      * }}}
      *
      * @tparam B The type over which the ordering is defined.
      * @return the largest element wrapped in `Option` or `None` if $coll is empty
      * @since 0.1.2
      *
      */
    def maxOption[B >: A: Ordering]: Option[A] =
      if (traversableLike.isEmpty) None
      else Some(traversableLike.max[B])

    /**
      * Finds the smallest element wrapped in `Option` or `None` if $coll is empty.
      *
      * Example:
      * {{{
      *   scala> List(1,2,1).minOption
      *   res1: Option[Int] = Some(1)
      *
      *   scala> List.empty[Int].minOption
      *   res2: Option[Int] = None
      * }}}
      *
      * @tparam B The type over which the ordering is defined.
      * @return the smallest element wrapped in `Option` or `None` if $coll is empty
      * @since 0.1.2
      */
    def minOption[B >: A: Ordering]: Option[A] =
      if (traversableLike.isEmpty) None
      else Some(traversableLike.min[B])

    /**
      * Finds the element where function `f` returns largest value according to `ord`.
      * Returns result wrapped in `Option` or `None` if $coll is empty.
      *
      * Example:
      * {{{
      *   scala> List(1,2,1).maxOptionBy(_ * -1)
      *   res1: Option[Int] = Some(1)
      *
      *   scala> List.empty[Int].maxOptionBy(_ * -1)
      *   res2: Option[Int] = None
      * }}}
      *
      * @param f Transforming function that applied to elements produces results to be compared.
      * @tparam B The type over which the ordering is defined.
      * @return the largest element (as identified by `ord` after applying `f`)
      *         wrapped in `Option` or `None` if $coll is empty
      * @since 0.1.2
      */
    def maxOptionBy[B: Ordering](f: A => B): Option[A] =
      if (traversableLike.isEmpty) None
      else Some(traversableLike.maxBy(f))

    /**
      * Finds the element where function `f` returns smallest value according to `ord`.
      * Returns result wrapped in `Option` or `None` if $coll is empty.
      *
      * Example:
      * {{{
      *   scala> List(1,2,1).minOptionBy(_ * -1)
      *   res1: Option[Int] = Some(2)
      *
      *   scala> List.empty[Int].minOptionBy(_ * -1)
      *   res2: Option[Int] = None
      * }}}
      *
      * @param f Transforming function that applied to elements produces results to be compared.
      * @tparam B The type over which the ordering is defined.
      * @return the smallest element (as identified by `ord` after applying `f`)
      *         wrapped in `Option` or `None` if $coll is empty
      * @since 0.1.2
      */
    def minOptionBy[B: Ordering](f: A => B): Option[A] =
      if (traversableLike.isEmpty) None
      else Some(traversableLike.minBy(f))

    /**
      * @since 0.3.0
      */
    def transform(pf: PartialFunction[A, A])(implicit cbf: CanBuildFrom[Repr, A, Repr]): Repr =
      traversableLike.map(a => if (pf.isDefinedAt(a)) pf(a) else a)

  }

  /**
    * @define coll iterable
    */
  implicit class IterableLikeExtension[A, Repr <: IterableLike[A, Repr]](
      val iterableLike: IterableLike[A, Repr])
      extends AnyVal {

    /** Applies a binary operator to a start value and all elements of this $coll while predicate `p` is satisfied,
      * going left to right.
      *
      * Example:
      * {{{
      *   scala> Iterable(List(1,2,3), List(4,5), List(6,7,8,9)).
      *        | foldLeftWhile(List.empty[Int])(_.size < 4){ case (acc, l) => acc ++ l }
      *   res1: List[Int] = List(1, 2, 3, 4, 5)
      * }}}
      *
      * @param   z    the start value.
      * @param   p    the predicate used to test if folding should continue.
      * @param   op   the binary operator.
      * @tparam  B    the result type of the binary operator.
      * @return  the result of inserting `op` between consecutive elements of this $coll while predicate `p` is satisfied,
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

  }

  implicit class MapLikeExtension[K, V, Repr <: MapLike[K, V, Repr] with Map[K, V]](val mapLike: MapLike[K, V, Repr]) extends AnyVal {

    /**
     * Merges this map with `another` using function `f`
     * to calculate result value for duplicate keys.
     * Value from this map is the first argument to `f`
     *
     * Returns a new map with all keys present in this and `another` maps
     * The type of result map is same as this map
     *
     * Example:
     * {{{
     *   scala> Map("1" -> 1, "2" -> 2).mergedWith(Map("1" -> 1, "2" -> 2))(_ + _)
     *   res1: scala.collection.immutable.Map[String,Int] = Map(1 -> 2, 2 -> 4)
     * }}}
     *
     * @since 0.1.2
     *
     */
    def mergedWith(another: Map[K, V])(f: (V, V) => V): Repr =
      if (another.isEmpty) mapLike.asInstanceOf[Repr]
      else {
        val mapBuilder = new mutable.MapBuilder[K, V, Repr](mapLike.asInstanceOf[Repr])
        another.foreach { case (k, v) =>
          mapLike.get(k) match {
            case Some(ev) => mapBuilder += k -> f(ev, v)
            case _ => mapBuilder += k -> v
          }
        }
        mapBuilder.result()
      }
  }

}
