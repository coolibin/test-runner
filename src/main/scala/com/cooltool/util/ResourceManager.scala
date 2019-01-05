package com.cooltool.util

import scala.language.reflectiveCalls

trait ResourceManager {

  /**
    * Automatic resource management helper
    * Usage example:
    * def copyFileCPS = using(new BufferedReader(new FileReader("test.txt"))) {
    *   reader => {
    *     using(new BufferedWriter(new FileWriter("test_copy.txt"))) {
    *       writer => {
    *         var line = reader.readLine
    *         var count = 0
    *         while (line != null) {
    *           count += 1
    *           writer.write(line)
    *           writer.newLine
    *           line = reader.readLine
    *         }
    *       count
    *     }
    *   }
    * }
    * }
    *
    * @param resource
    * @param f
    * @tparam X
    * @tparam A
    * @return
    */
  def using[X <: {def close()}, A](resource: X)(f: X => A) = {
    try {
      f(resource)
    } finally {
      resource.close()
    }
  }
}
