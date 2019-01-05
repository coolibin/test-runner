package com.cooltool.util

import org.slf4j.LoggerFactory
import ch.qos.logback.classic.Level

object LogManager {
  def getLogger(loggerName: String) = LoggerFactory.getLogger(loggerName)

  def setLevel(loggerName: String, level: Level): Unit = {
    getLogger(loggerName).asInstanceOf[ch.qos.logback.classic.Logger].setLevel(level)
  }
}
