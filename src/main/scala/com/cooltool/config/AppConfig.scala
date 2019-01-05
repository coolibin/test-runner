package com.cooltool.config

import com.typesafe.config._

import scala.util.Try

object AppConfig {
  lazy val config: Config = ConfigFactory.load()

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  object Environment {
    lazy val name: String = config.getString("environment")
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  object Defaults {

    def defaultDataFormat: String = "yyyy-MM-dd"
    def listDelimiter: String = "+"
    def keyValueDelimiter: String = "="

    object CSV {
      lazy val fieldsDelimiter: Char = config.getString("defaults.csv.fieldDelimiter").headOption.getOrElse(',')
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  object UI {

    object WebDriver {
      lazy val takeScreenShot: Boolean =
        Try(config.getBoolean("UI.WebDriver.takeScreenShot")).getOrElse(false)
    }

  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  def getStringOpt(path: String): Option[String] = {
    Try(config.getString(path)).toOption
  }

  def getBooleanOpt(path: String): Option[Boolean] = {
    Try(config.getBoolean(path)).toOption
  }

  def getIntOpt(path: String): Option[Int] = {
    Try(config.getInt(path)).toOption
  }

  //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
