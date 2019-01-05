package com.cooltool.util

/**
  * Inherit the trait to add a logger to your class
  */
trait LogRunner {
  lazy protected val log: org.slf4j.Logger = LogManager.getLogger(this.getClass.getName)

  implicit class LogImprovements(val logger: org.slf4j.Logger) {
    def setLevel(level: ch.qos.logback.classic.Level): Unit = {
      log.asInstanceOf[ch.qos.logback.classic.Logger].setLevel(level)
    }
  }

  object LogLevel {
    import ch.qos.logback.classic.Level
    val ALL: Level = Level.ALL
    val ERROR: Level = Level.ERROR
    val WARN: Level = Level.WARN
    val INFO: Level = Level.INFO
    val DEBUG: Level = Level.DEBUG
    val TRACE: Level = Level.TRACE
  }
}