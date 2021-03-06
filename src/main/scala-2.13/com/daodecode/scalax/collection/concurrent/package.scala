package com.daodecode.scalax.collection

import java.lang
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

import scala.jdk.CollectionConverters._
import scala.collection.mutable

package object concurrent {

  type ConcurrentMap[K, V] = scala.collection.concurrent.Map[K, V]

  object ConcurrentMap {

    /**
      * Builds empty ConcurrentMap
      *
      * @since 0.1.0
      */
    def empty[K, V]: ConcurrentMap[K, V] = new ConcurrentHashMap[K, V]().asScala
  }

  object ConcurrentSet {

    /**
      * Builds empty set backed by ConcurrentMap
      *
      * @since 0.1.0
      */
    def empty[K]: mutable.Set[K] = Collections.newSetFromMap[K](new ConcurrentHashMap[K, lang.Boolean]()).asScala
  }

}
