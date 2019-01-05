package com.cooltool.util

import java.nio.file.Path

object Cypher {

  def encode(bytes: Array[Byte]): String = {
    java.util.Base64.getEncoder.encodeToString(bytes)
  }

  def encode(originalString: String): String = {
    java.util.Base64.getEncoder.encodeToString(originalString.getBytes("utf-8"))
  }

  def encode(filePath: Path): String = {
    java.util.Base64.getEncoder.encodeToString(java.nio.file.Files.readAllBytes(filePath))
  }

  def decode(encodedString: String): String = {
    new String(java.util.Base64.getDecoder.decode(encodedString), "utf-8")
  }
}
