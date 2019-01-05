package com.cooltool.files

import java.io._
import java.nio.file._

import com.cooltool.util.{LogRunner, ResourceManager}

object FileManager extends ResourceManager with LogRunner {

  def pathJoin(path: String, fileName: String): String = {
    Paths.get(path, fileName).toString // may need toAbsolutePath() on server (?)
  }

  /**
    * Only works on OSX or xUnix computers
    *
    * @param serverName
    * @return
    */
  def getIpByName(serverName: String): String = {
    log.debug("Getting an IP address for {}", serverName)
    var ip: String = ""
    try {
      val cmd: Array[String] = Array(
        """/bin/sh""", "-c", "ping -c1 " +
          serverName +
          """ | grep -Eo -m1 '([0-9]*\.){3}[0-9]*'""")
      val p: Process = Runtime.getRuntime.exec(cmd)
      p.waitFor

      ip = new BufferedReader(new InputStreamReader(p.getInputStream)).readLine()
      log.debug(s"ip:$ip")
    }
    catch {
      case e: Exception => {
        log.error(e.getMessage)
      }
    }
    ip
  }

  /**
    * Copies a text file with pattern replacement
    *
    * @param sourceFileName
    * @param destinationFileName
    * @param replacement
    */
  def copyTemplate(
                    sourceFileName: String,
                    destinationFileName: String,
                    replacement: Map[String, String]): Unit = {

    import java.nio.charset.StandardCharsets
    import java.util.Scanner

    using(new Scanner(Paths.get(sourceFileName), StandardCharsets.UTF_8.name())) { scanner =>
      using(new FileWriter(destinationFileName)) { writer =>
        while (scanner.hasNextLine) {
          var line = scanner.nextLine()
          replacement.keys.foreach { fromText =>
            line = ("(\\" + fromText + ")").r.replaceAllIn(line, replacement.get(fromText).get)
          }
          writer.write(line + "\r")
        }
      }
    }
  }


  /**
    * Copies file from fromFile to toFile
    * where fromFile and toFile are source and destination file names
    */
  def copyFile(fromFile: String, toFile: String) = {
    java.nio.file.Files.copy(
      Paths.get(fromFile),
      Paths.get(toFile),
      StandardCopyOption.REPLACE_EXISTING)
  }

  def moveFile(fromFile: String, toFile: String): Unit = {
    java.nio.file.Files.move(
      Paths.get(fromFile),
      Paths.get(toFile),
      StandardCopyOption.REPLACE_EXISTING)
  }

  def fileExist(fileName: String): Boolean = {
    java.nio.file.Files.exists(Paths.get(fileName), LinkOption.NOFOLLOW_LINKS)
  }

  def deleteFile(fileToDelete: String): Boolean = {
    java.nio.file.Files.deleteIfExists(Paths.get(fileToDelete))
  }

  // converts file to a byte array
  def toBytes(fileName: String): Array[Byte] = {
    val file = new File(fileName)
    using(new FileInputStream(file)) { in =>
      val bytes = new Array[Byte](file.length.toInt)
      in.read(bytes)
      bytes
    }
  }

  def saveTextToFile(text: String, fileName: String): Unit = {
    using(new FileWriter(fileName)) { writer =>
      writer.write(text)
    }
  }

  /**
    * Reads text file into a string
    */
  def readFile(fileName: String, encoding: String = "utf-8"): String = {
    io.Source.fromFile(fileName, encoding).getLines.mkString("\n")
  }

  def folderExists(folderName: String): Boolean = {
    val fld = new File(folderName)
    fld.exists() && fld.isDirectory
  }

  def createFolder(folderName: String): String = {
    val fld = new File(folderName)
    if (!fld.exists()) {
      fld.mkdirs()
    }
    fld.getCanonicalPath
  }

  /**
    * Deletes folder with all existing content
    */
  def deleteFolder(folderName: String): Unit = {
    def deleteRecursively(file: File): Unit = {
      if (file.isDirectory)
        file.listFiles.foreach(deleteRecursively)
      if (file.exists && !file.delete)
        throw new Exception(s"Unable to delete ${file.getAbsolutePath}")
    }

    deleteRecursively(new File(folderName))
  }

  /**
    * Deletes all content of the folder
    */
  def clearFolder(folderName: String): Unit = {
    def deleteRecursively(file: File) {
      if (file.isDirectory)
        Option(file.listFiles).map(_.toList).getOrElse(Nil).foreach(deleteRecursively)

      if (file.getName != folderName)
        file.delete
    }

    deleteRecursively(new File(folderName))
  }
}
