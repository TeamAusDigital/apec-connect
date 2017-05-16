package org.ausdigital.apecconnect.common

import scala.util.Random._

package object random {

  /**
   * This random words generator is taken from: https://kernelgarden.wordpress.com/2014/06/27/a-heroku-like-name-generator-in-scala/
   */
  object RandomWordsGenerator {

    private val adjs = List("autumn", "hidden", "bitter", "misty", "silent",
      "reckless", "daunting", "short", "rising", "strong", "timber", "tumbling",
      "silver", "dusty", "celestial", "cosmic", "crescent", "double", "far",
      "terrestrial", "huge", "deep", "epic", "titanic", "mighty", "powerful")

    private val nouns = List("waterfall", "river", "breeze", "moon", "rain",
      "wind", "sea", "morning", "snow", "lake", "sunset", "pine", "shadow", "leaf",
      "sequoia", "cedar", "wrath", "blessing", "spirit", "nova", "storm", "burst",
      "giant", "elemental", "throne", "game", "stone", "apogee", "bang")

    private def getRandElt[A](xs: List[A]): A = xs.apply(nextInt(xs.size))

    private def getRandNumber(ra: Range): String = {
      (ra.head + nextInt(ra.end - ra.head)).toString
    }

    def randomWords(shouldAddPrefix: Boolean, prefix: String): String = {
      val xs = getRandNumber(1 to 9999) :: List(nouns, adjs).map(getRandElt)
      if (shouldAddPrefix) {
        prefix + xs.reverse.mkString("-")
      } else {
        xs.reverse.mkString("-")
      }
    }
  }
}
