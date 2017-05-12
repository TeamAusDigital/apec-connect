package org.ausdigital.apecconnect.common

package object parsers {

  /**
   * Provides methods that are clearly marked as unsafe when they may throw exceptions.
   * @param string the string to be wrapped.
   */
  implicit class UnsafeString(string: String) {
    /**
     * Converts the content of the string to a Long.
     * @throws NumberFormatException if the String is not a valid Long.
     * @return the Long contains in the string.
     */
    @throws[NumberFormatException]("if the string does not contain a valid Long")
    @SuppressWarnings(Array("org.danielnixon.playwarts.StringOpsPartial"))
    def toLongUnsafe: Long = string.toLong
  }

}
