package com.cooltool.util

import scala.collection.immutable.SortedMap

/**
  * Improvement methods for Maps
  */
object MapUtils {

  implicit class MapImprovements(val sortedMap: SortedMap[String, String]) {

    /**
      * Gets Option of first found value from a sorted map
      */
    def oneOf(listOfAliases: List[String]): Option[String] = {
      listOfAliases.flatMap(k => sortedMap.get(k)).headOption
    }

    /**
      * Gets Option of first found value from a sorted map
      */
    def oneOf(alias: String*): Option[String] = {
      alias.flatMap(sortedMap.get).headOption
    }
  }

  implicit class StringMapImprovements(val map: Map[String, String]) {
    /**
      * Converts Map[String, String] => SortedMap[String, Strinng]
      */
    def asSortedMap: SortedMap[String, String] = {
      SortedMap[String, String](map.toArray: _*)(Ordering.by(_.toLowerCase))
    }
  }
}