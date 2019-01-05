package com.cooltool.util

import scala.concurrent.duration.Duration

trait Retriable extends LogRunner {

  /**
    * Provides a wrapper for a function where multiple attempts might be required
    * or need to wait for async service to finish its job
    *
    * @param times how many time to retry
    * @param fn    - function to run
    * @tparam T
    * @return
    */
  def retry[T](times: Int)(fn: => T): Option[T] =
    (1 to times).view flatMap (_ =>
      try
        Some(fn)
      catch {
        case e: Exception => None
      }) headOption

  def waitForCondition[T](cond: => Boolean, maxDuration: Duration)(fn: => T): Option[T] = {
    retry(20) {
      if (!cond) {
        log.info("Waiting for condition met...")
        Thread.sleep((maxDuration / 20).toMillis)
        throw new Exception("Retry")
      } else fn
    }
  }
}
