package com.cooltool.util

import java.util.concurrent.TimeUnit

import scala.concurrent.duration._

/**
  * Wait helper. Will check the predicate method until it is true, and after that run andThen methods chain
  * If condition is not met during timeout period, then it fires the orOtherwise() method
  *
  *
  * @param message - message that appears during waiting
  * @param timeout - maximum time for checking the condition
  * @param pollEvery - how often to repeat the condition check
  */

case class Wait(
  message: () => String = () => "Waiting for condition to be met...",
  timeout: Duration = 2 seconds,
  pollEvery: Option[Duration] = None
) extends LogRunner with Retriable {

  var success: Boolean = false

  def whileTrue(condition: => Boolean): WaitUntil = {
    var counter = 0
    val startTime = System.nanoTime()

    retry(20) {
      if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) > timeout.toMillis)
        throw new Exception("Timeout")

      counter += 1
      if (!condition) {
        success = true
        new WaitUntil(this)
      } else {
        log.info(s"${message.apply()} ($counter)")
        Thread.sleep(pollEvery.getOrElse(timeout / 20).toMillis)
        throw new Exception("Retry")
      }
    }.getOrElse {
      success = false
      new WaitUntil(this)
    }
  }

  def until(condition: => Boolean): WaitUntil = {
    var counter = 0
    val startTime = System.nanoTime()

    retry(20) {
      if (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime) > timeout.toMillis)
        throw new Exception("Timeout")

      counter += 1
      if (condition) {
        success = true
        new WaitUntil(this)
      } else {
        log.info(s"${message.apply()} ($counter)")
        Thread.sleep(pollEvery.getOrElse(timeout / 20).toMillis)
        throw new Exception("Retry")
      }
    }.getOrElse {
      success = false
      new WaitUntil(this)
    }
  }

  class WaitUntil(wait: Wait) {

    def andThen(action: => Unit): WaitUntil = {
      if (wait.success)
        action
      this
    }

    def orOtherwise(action: => Unit = {}): Unit = {
      if (!wait.success)
        action
    }
  }
}
