package com.daodecode.scalax.benchmarks

import org.openjdk.jmh.annotations._
import com.daodecode.scalax.collection.extensions._

import scala.collection.immutable

@State(Scope.Benchmark)
@BenchmarkMode(Array(Mode.Throughput))
class IterableLikeExtensionsBenchmark {

  @Param(Array("10", "100", "1000", "10000", "100000", "1000000"))
  var size: Int = _

  var iterable: Iterable[(Int, String)] = _

  @Setup
  def prepare: Unit = {
    iterable = Iterable((0 to size).map(i => i -> i.toString): _*)
  }

//  @Benchmark
//  def identityMap: Unit = {
//    iterable.map(identity)
//  }

  @Benchmark
  def mapAndToMapBenchmark: Unit = {
    iterable.map(identity).toMap
  }

  @Benchmark
  def mapAndToMapBenchmark2: Unit = {
    iterable.map(identity).toMap
  }

  @Benchmark
  def mapToMapBenchmark: Unit = {
    iterable.mapToMap(identity)
  }

  @Benchmark
  def mapToMapInlinedBenchmark: Unit = {
    val b = immutable.Map.newBuilder[Int, String]
    for (a <- iterable)
      b += identity(a)
    b.result()
  }

  @Benchmark
  def mapToMapInlinedBenchmark2: Unit = {
    val b = immutable.Map.newBuilder[Int, String]
    for (a <- iterable)
      b += identity(a)
    b.result()
  }

}
