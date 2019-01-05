package com.cooltool.util

/**
  * Allows to organize methods in a pipeline
  *
  * method1 |> method2 |> ...
  *
  * The requirement to methods is to have the input and output types compliance (compile-time checking)
  */

object Pipelining {

  implicit class Pipeliner[T](val s: T) {
    def |>[U](f: T => U) = f(s)
  }

}