package au.com.agiledigital.common.parsers

import au.com.agiledigital.rest.tests.BaseSpec

/**
  * Contains unit tests for the [[UnsafeString]]
  */
class UnsafeStringSpec extends BaseSpec {

  "Parsing a String to a Long" should {
    "throw an exception if the string is not a valid long" in {
      // Given a String that is not a long.
      val s = "not a long"

      // When it is wrapped in an UnsafeString.
      val wrapped = new UnsafeString(s)

      // And parsed as a long
      // Then an exception should be thrown.
      wrapped.toLongUnsafe must throwA[NumberFormatException]
    }
    "return the parsed long" in {
      // Given a String that is a long.
      val s = "10001"

      // When it is wrapped in an UnsafeString.
      val wrapped = new UnsafeString(s)

      // And parsed to a long
      val actual = wrapped.toLongUnsafe

      // Then the expected value should be returned
      actual must_=== 10001
    }
  }
}
