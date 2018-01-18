package com.daodecode.scalax.benchmarks

import org.openjdk.jmh.annotations.{Benchmark, Scope, State}
import com.daodecode.scalax.collection.extensions._

case class Person(fName: String, lName: String, age: Int)


@State(Scope.Benchmark)
class SeqExtensionsBenchmark {


  val eugeneP = Person("Eugene", "Platonov", 27)
  val xeniya = Person("Xeniya", "Skrypnyk", 27)
  val eugeneM = Person("Eugene", "Medvediev", 31)
  val vasiliy = Person("Vasiliy", "Platonov", 2)

  val people = Seq(eugeneP, xeniya, eugeneM, vasiliy)

  @Benchmark
  def testDistinctBySimple: Unit = {
    people.distinctBy(_.fName)
  }

}
