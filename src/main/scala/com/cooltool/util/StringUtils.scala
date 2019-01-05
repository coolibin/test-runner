package com.cooltool.util

import com.cooltool.config.AppConfig
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.json4s.native.JsonMethods.parse
import org.json4s.{DefaultFormats, JValue}

import scala.util.Try

object StringUtils {

  implicit class StringImprovements(val s: String) {

    /**
      * Method converts a string into Option[Int]
      **/
    def asIntOpt: Option[Int] = {
      Try(if (s.isEmpty) 0 else s.trim.toInt).toOption
    }

    /**
      * Converts string to Int with 0 default
      */
    def asInt: Int = {
      asIntOpt.getOrElse(0)
    }

    def asLongOpt: Option[Long] = {
      Try(if (s.isEmpty) 0L else s.trim.toLong).toOption
    }

    def asLong: Long = {
      asLongOpt.getOrElse(0L)
    }

    /**
      * Method converts a string representing Date to Date
      **/
    def asDate(pattern: String): DateTime = {
      DateTime.parse(s.take(pattern.length), DateTimeFormat.forPattern(pattern))
    }

    def asDate: DateTime = asDate(AppConfig.Defaults.defaultDataFormat)

    // converts DateTime string into a short DATE string "2016-08-12 11:45:57" -> "2016-08-12"
    def asSimpleDateString: String = {
      if (s.length > 10 && s"${s(4)}${s(7)}" == "--") {
        s.take(10)
      } else s
    }

    /**
      * Converts string to a Option[Boolean]
      **/
    def asBooleanOpt: Option[Boolean] = {
      s.noNulls.toLowerCase match {
        case "y" | "yes" | "true" => Some(true)
        case "n" | "no" | "false" => Some(false)
        case _ => None
      }
    }

    def asBoolean: Boolean = asBooleanOpt.getOrElse(false)

    /**
      * Converts string to Double
      **/
    def asDouble: Double = {
      if (!s.isEmpty) s.toDouble else 0.0
    }

    /**
      * Converts string to Option[Double]
      **/
    def asDoubleOpt: Option[Double] = {
      Try(if (s.isEmpty) 0.0 else s.trim.toDouble).toOption
    }

    /**
      * Converts string value to an Option of the requested type
      *
      * @param anyString
      * @tparam T - returned type
      * @return
      */
    def asOption: Option[String] = {
      s match {
        case null | "" | "None" => None
        case _ => Some(s)
      }
    }

    /**
      * Splits a single string to a List of Strings
      * based on the separator from configuration
      **/
    def asList: List[String] = {
      if (s == null)
        Nil
      else
        s.split(AppConfig.Defaults.listDelimiter).toList
          .map {
            case "NA" | "N/A" | "None" => ""
            case s: String => s.trim
          }.filterNot(_ == "")

    }

    def asListOpt: Option[List[String]] = {
      s match {
        case null | "" | "NA" | "N/A" | "None" => None
        case _ => Some(s.asList)
      }
    }


    // extracts list of pairs from a simple field:
    // ( txt1 : type1 + txt2 : type2 => List((txt1, type1), (txt2,type2))
    def asPairList(omitFirstPartOnPairByDefault: Boolean = true): Option[List[(Option[String], Option[String])]] = {
      s.trim match {
        case null | "" | "NA" | "N/A" | "None" => None
        case _ => Some(s.asList.map { p =>
          val pair = p.split(AppConfig.Defaults.keyValueDelimiter)
          Tuple2(
            if (pair.size > 1 || !omitFirstPartOnPairByDefault) pair.head.trim.asOption
            else None,
            if (pair.size > 1 || omitFirstPartOnPairByDefault) pair.last.trim.asOption
            else None
          )
        })
      }
    }

    def asKeyValueMap: Map[String, String] = {
      s match {
        case null => Map.empty[String, String]
        case "" => Map.empty[String, String]
        case _ =>
          s.trim.asList.map(p => p.split(AppConfig.Defaults.keyValueDelimiter))
            .map(pair => pair.head.trim -> pair.tail.headOption.getOrElse("").trim)
            .toMap

      }
    }

    // converts a string into a list of triples
    // ( "A1 : B1 : C1 + A2 : B2 : C2" => List((A1, B1, C1), (A2, B2, C2))
    def asTripletList: Option[List[(Option[String], Option[String], Option[String])]] = {
      def getValueOpt(element: String): Option[String] = element.toUpperCase match {
        case "" | " " | "NONE" => None
        case _ => Option(element.trim)
      }

      s.trim.toUpperCase match {
        case "" | "NA" | "N/A" | "NONE" => None
        case _ => Some(s.asList.map { p =>
          val triplet = p.split(AppConfig.Defaults.keyValueDelimiter)

          Tuple3(
            if (triplet.nonEmpty) getValueOpt(triplet.head) else None,
            if (triplet.size > 1) getValueOpt(triplet.tail.head) else None,
            if (triplet.size > 2) getValueOpt(triplet.tail.tail.head) else None
          )
        })
      }
    }

    /**
      * Method cuts the tail from a string if it has one
      **/
    def dropTail(tail: String): String = {
      if (s.takeRight(tail.length) == tail) {
        s.dropRight(tail.length)
      } else {
        s
      }
    }

    /**
      * Method cuts the prefix from a string if it has one
      **/
    def dropHead(prefix: String): String = {
      if (s.take(prefix.length) == prefix) {
        s.drop(prefix.length)
      } else {
        s
      }
    }

    def urlEncoded: String = {
      java.net.URLEncoder.encode(s, "UTF-8")
    }

    /**
      * nullable string to not nullable string
      */
    def noNulls: String = if(s == null) "" else s

    def isNullOrEmpty: Boolean = {
      s == null || s.isEmpty
    }

    def isNotNullOrEmpty: Boolean = {
      !isNullOrEmpty
    }

    def isNumber: Boolean = {
      s forall Character.isDigit
    }

    def extract[T: Manifest]: T = {
      import org.json4s.native.JsonMethods._
      implicit val formats = DefaultFormats
      parse(s).extract[T]
    }

    /**
      * Returns JValue object for the string
      */
    def asJson: JValue = {
      parse(s)
    }

    /**
      * Creates a string of length by adding a character on the left
      */
    def padLeft(length: Int, zeroStr: String): String = {
      s.reverse.padTo(length, zeroStr.take(1)).reverse.mkString
    }

    def padLeft(length: Int): String = {
      padLeft(length, " ")
    }

    def padRight(length: Int, zeroStr: String): String = {
      s.padTo(length, zeroStr.take(1)).mkString
    }

    def padRight(length: Int): String = {
      padRight(length, " ")
    }

    // adds 'prefix' and 'suffix' to the string
    def wrapUp(prefix: String, suffix: String): String = {
      s"$prefix$s$suffix"
    }
  }
}
