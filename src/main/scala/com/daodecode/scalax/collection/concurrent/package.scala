package com.daodecode.scalax.collection

import java.lang
import java.util.Collections
import java.util.concurrent.ConcurrentHashMap

import scala.collection.JavaConverters._
import scala.collection.mutable

package object concurrent {

  type ConcurrentMap[K, V] = collection.concurrent.Map[K, V]

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
     * Builds empty set baked by ConcurrentMap
     *
     * @since 0.1.0
     */
    def empty[K]: mutable.Set[K] = Collections.newSetFromMap[K](new ConcurrentHashMap[K, lang.Boolean]()).asScala
  }

}
