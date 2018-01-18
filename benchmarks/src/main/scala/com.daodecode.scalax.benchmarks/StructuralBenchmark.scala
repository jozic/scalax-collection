package com.daodecode.scalax.benchmarks

import org.openjdk.jmh.annotations.{Benchmark, Scope, State}

@State(Scope.Benchmark)
class StructuralBenchmark {

  trait `M-able` {
    def m(s: String): String
  }

  class A extends `M-able` {
    def m(s: String) = s
  }

  class B extends `M-able` {
    def m(s: String) = s + s
  }

  def structural(hasM: {def m(s: String): String}) = hasM.m("boo")

  def direct(hasM: `M-able`) = hasM.m("boo")

  @Benchmark
  def benchMarkStructural(): Unit = {
    structural(new A)
    structural(new B)
  }

  @Benchmark
  def benchMarkDirect(): Unit = {
    direct(new A)
    direct(new B)
  }

}
