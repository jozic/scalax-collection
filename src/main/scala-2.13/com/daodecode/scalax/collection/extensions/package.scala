package com.daodecode.scalax.collection

import scala.annotation.nowarn
import scala.collection.immutable.SeqOps
import scala.collection.{BuildFrom, IterableOps, MapOps, View, immutable, mutable}

package object extensions {

  /**
    * @define coll sequence
    */
  implicit class SeqOpsExtension[A, C](val seqOps: SeqOps[A, Seq, C]) extends AnyVal {

    /** Builds a new $coll from this $coll without any duplicate elements (as
      * determined by `==` after applying transforming function `f`).
      * Function `takeFirst` defines which of duplicates will be preserved.
      * If it returns `true` first of compared duplicates will be kept, second one otherwise.
      *
      * Example:
      * {{{
      *   scala> List(1 -> "one", 1 -> "ten", 2 -> "two", 2 -> "twenty").
      *        | distinctByUsing(_._1, takeFirst = _._2.length > _._2.length)
      *   res1: List[(Int, String)] = List((1,ten), (2,twenty))
      * }}}
      *
      * @return  A new $coll which contains selected occurrence of every element of this $coll.
      * @since 0.1.1
      */
    def distinctByUsing[B](f: A => B, takeFirst: (A, A) => Boolean)(implicit bf: BuildFrom[C, A, C]): C = {
      val seen = mutable.LinkedHashMap.empty[B, A]
      for (x <- seqOps) {
        val fx = f(x)
        seen.get(fx) match {
          case Some(a) => seen += fx -> (if (takeFirst(a, x)) a else x)
          case _ => seen += fx -> x
        }
      }
      bf.fromSpecific(seqOps.repr)(seen.values): @nowarn("cat=deprecation")
    }
  }

  /**
    * @define coll iterable
    */
  implicit class IterableOpsExtension[A, CC[_], C <: IterableOps[A, CC, C]](val iterableOps: IterableOps[A, CC, C])
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
      val it = iterableOps.iterator
      while (it.hasNext && p(result)) {
        result = op(result, it.next())
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
      if (iterableOps.isEmpty) z
      else {
        val result = iterableOps.tail.foldRightWhile(z)(p)(op)
        if (p(result)) op(iterableOps.head, result) else result
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
      *   res1: scala.collection.immutable.Map[Int,List[String]] = Map(1 -> List(1, 11), 2 -> List(2))
      * }}}
      *
      * @return a map of type `immutable.Map[K, That]` where `That` is a $coll of `V`
      * @since 0.1.1
     **/
    def toCompleteMap[K, V](implicit ev: A <:< (K, V)): immutable.Map[K, CC[V]] =
      iterableOps.groupMap(_._1)(_._2)

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
      for (a <- iterableOps)
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
      *   res1: scala.collection.immutable.Map[String,Int] = Map(a -> 2, b -> 2, c -> 1, d -> 1)
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
      *   res1: scala.collection.immutable.Map[Char  ,Int] = Map(a -> 2, b -> 2, c -> 1, d -> 1)
      * }}}
      *
      * @return a map of type immutable.Map[B, Int] where Int represents a frequency of keys,
      *         obtained from original $coll by applying function `f` to elements
      * @since 0.2.0
      */
    def withFrequencyBy[B](f: A => B): immutable.Map[B, Int] = iterableOps.groupMapReduce(f)(_ => 1)(_ + _)


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
    @deprecated("Use maxByOption from standard library", since = "0.3.0")
    def maxOptionBy[B: Ordering](f: A => B): Option[A] =
      iterableOps.maxByOption(f)

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
    @deprecated("Use minByOption from standard library", since = "0.3.0")
    def minOptionBy[B: Ordering](f: A => B): Option[A] =
      iterableOps.minByOption(f)


    /** Converts this iterable of Tuple4 into four collections of the first, second,
      *  third and fourth element of each Tuple4.
      *
      * Example:
      *    {{{
      *    scala> List((1, "one", '1', 1d), (2, "two", '2', 2d), (3, "three", '3', 3d), (4, "four", '4', 4d)).unzip4
      *    res1: (List[Int], List[String], List[Char], List[Double]) = (List(1, 2, 3, 4),List(one, two, three, four),List(1, 2, 3, 4),List(1.0, 2.0, 3.0, 4.0))
      *    }}}
      *
      *  @tparam A1       the type of the first member of the Tuple4 element
      *  @tparam A2       the type of the second member of the Tuple4 element
      *  @tparam A3       the type of the third member of the Tuple4 element
      *  @tparam A4       the type of the fourth member of the Tuple4 element
      *  @param asTuple4  an implicit conversion which asserts that the element type
      *                   of this iterable is a Tuple4.
      *  @return          a Tuple4 of iterables, containing the first, second, third and forth
      *                   member of each Tuple4 element of this iterable.
      *  @since 0.3.2
      */
    def unzip4[A1, A2, A3, A4](implicit asTuple4: A => (A1, A2, A3, A4)): (CC[A1], CC[A2], CC[A3], CC[A4]) = (
      iterableOps.iterableFactory.from(new View.Map[A, A1](iterableOps, asTuple4(_)._1)),
      iterableOps.iterableFactory.from(new View.Map[A, A2](iterableOps, asTuple4(_)._2)),
      iterableOps.iterableFactory.from(new View.Map[A, A3](iterableOps, asTuple4(_)._3)),
      iterableOps.iterableFactory.from(new View.Map[A, A4](iterableOps, asTuple4(_)._4))
    )

    /** Converts this iterable of Tuple5 into five collections of the first, second,
      *  third, fourth and fifth element of each Tuple5.
      *
      * Example:
      *    {{{
      *    scala> List((1, "one", '1', 1d, 1L), (2, "two", '2', 2d, 2L), (3, "three", '3', 3d, 3L), (4, "four", '4', 4d, 4L), (5, "five", '5', 5d, 5L)).unzip5
      *    res1: (List[Int], List[String], List[Char], List[Double], List[Long]) = (List(1, 2, 3, 4, 5),List(one, two, three, four, five),List(1, 2, 3, 4, 5),List(1.0, 2.0, 3.0, 4.0, 5.0),List(1, 2, 3, 4, 5))
      *    }}}
      *
      *  @tparam A1       the type of the first member of the Tuple5 element
      *  @tparam A2       the type of the second member of the Tuple5 element
      *  @tparam A3       the type of the third member of the Tuple5 element
      *  @tparam A4       the type of the fourth member of the Tuple5 element
      *  @tparam A5       the type of the fifth member of the Tuple5 element
      *  @param asTuple5  an implicit conversion which asserts that the element type
      *                   of this iterable is a Tuple5.
      *  @return          a Tuple5 of iterables, containing the first, second, third,
      *                   forth and fifth member of each Tuple5 element of this iterable.
      *  @since 0.3.2
      */
    def unzip5[A1, A2, A3, A4, A5](implicit asTuple5: A => (A1, A2, A3, A4, A5)): (CC[A1], CC[A2], CC[A3], CC[A4], CC[A5]) = (
      iterableOps.iterableFactory.from(new View.Map[A, A1](iterableOps, asTuple5(_)._1)),
      iterableOps.iterableFactory.from(new View.Map[A, A2](iterableOps, asTuple5(_)._2)),
      iterableOps.iterableFactory.from(new View.Map[A, A3](iterableOps, asTuple5(_)._3)),
      iterableOps.iterableFactory.from(new View.Map[A, A4](iterableOps, asTuple5(_)._4)),
      iterableOps.iterableFactory.from(new View.Map[A, A5](iterableOps, asTuple5(_)._5))
    )
    /** Converts this iterable of Tuple6 into five collections of the first, second,
      *  third, fourth, fifth and sixth element of each Tuple6.
      *
      * Example:
      *    {{{
      *    scala> List((1, "one", '1', 1d, 1L, true), (2, "two", '2', 2d, 2L, false), (3, "three", '3', 3d, 3L, true), (4, "four", '4', 4d, 4L, false), (5, "five", '5', 5d, 5L, true), (6, "six", '6', 6d, 6L, false)).unzip6
      *    res0: (List[Int], List[String], List[Char], List[Double], List[Long], List[Boolean]) = (List(1, 2, 3, 4, 5, 6),List(one, two, three, four, five, six),List(1, 2, 3, 4, 5, 6),List(1.0, 2.0, 3.0, 4.0, 5.0, 6.0),List(1, 2, 3, 4, 5, 6),List(true, false, true, false, true, false))
      *    }}}
      *
      *  @tparam A1       the type of the first member of the Tuple6 element
      *  @tparam A2       the type of the second member of the Tuple6 element
      *  @tparam A3       the type of the third member of the Tuple6 element
      *  @tparam A4       the type of the fourth member of the Tuple6 element
      *  @tparam A5       the type of the fifth member of the Tuple6 element
      *  @tparam A6       the type of the sixth member of the Tuple6 element
      *  @param asTuple6  an implicit conversion which asserts that the element type
      *                   of this iterable is a Tuple6.
      *  @return          a Tuple6 of iterables, containing the first, second, third,
      *                   forth, fifth and sixth member of each Tuple6 element of this iterable.
      *  @since 0.3.2
      */
    def unzip6[A1, A2, A3, A4, A5, A6](implicit asTuple6: A => (A1, A2, A3, A4, A5, A6)): (CC[A1], CC[A2], CC[A3], CC[A4], CC[A5], CC[A6]) = (
      iterableOps.iterableFactory.from(new View.Map[A, A1](iterableOps, asTuple6(_)._1)),
      iterableOps.iterableFactory.from(new View.Map[A, A2](iterableOps, asTuple6(_)._2)),
      iterableOps.iterableFactory.from(new View.Map[A, A3](iterableOps, asTuple6(_)._3)),
      iterableOps.iterableFactory.from(new View.Map[A, A4](iterableOps, asTuple6(_)._4)),
      iterableOps.iterableFactory.from(new View.Map[A, A5](iterableOps, asTuple6(_)._5)),
      iterableOps.iterableFactory.from(new View.Map[A, A6](iterableOps, asTuple6(_)._6))
    )

  }

  implicit class MapOpsExtension[K, V, CC[_, _] <: IterableOps[_, Any, _], C](val mapOps: MapOps[K, V, CC, C])
      extends AnyVal {

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
    def mergedWith(another: scala.collection.Map[K, V])(f: (V, V) => V): CC[K, V] =
      if (another.isEmpty) mapOps.mapFactory.from(mapOps)
      else {
        val mapBuilder = mapOps.mapFactory.newBuilder[K,V]
        another.foreach {
          case (k, v) =>
            mapOps.get(k) match {
              case Some(ev) => mapBuilder += k -> f(ev, v)
              case _ => mapBuilder += k -> v
            }
        }
        mapBuilder.result()
      }
  }

}
