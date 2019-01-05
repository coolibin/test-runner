package com.cooltool.util

import org.json4s.{DefaultFormats, Formats}
import org.json4s.native.Serialization._

trait JsonDataExt extends LogRunner {
  implicit val formats: Formats = DefaultFormats

  def asJson: String = {
    write(this)
  }

  def asPrettyJson: String = {
    writePretty(this)
  }

}
