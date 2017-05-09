package au.com.agiledigital.seed.time.config

import au.com.agiledigital.rest.tests.BaseSpec
import com.typesafe.config.ConfigFactory
import play.api.{ Configuration, PlayException }

/**
  * Contains unit tests for the [[TimeTransportConfig]]
  */
class TimeTransportConfigProviderSpec extends BaseSpec {

  "Creating an instance from a configuration" should {
    "work if the configuration is sufficient" in {
      // Given a configuration that contains the required items.

      // And a provider that uses that configuration.
      val provider = new TimeTransportConfigProvider(validConfiguration)

      // When a time transport configuration is created.
      val configuration = provider.get()

      // Then it should have been created as expected.
      configuration.url must_=== "configured_url"
    }
    "fail if the configuration does not have a URL" in {
      // Given a configuration that is missing the URL.

      // And a provider that uses that configuration.
      val provider = new TimeTransportConfigProvider(missingUrlConfiguration)

      // When a time transport configuration is created.
      // Then an exception should have been thrown
      provider.get() must throwA[PlayException](message = "time_transport.url")
    }
  }

  val validConfiguration = Configuration(ConfigFactory.parseString(
    """
      | {
      |   time_transport: {
      |     url: configured_url
      |   }
      | }
    """.stripMargin
  ))

  val missingUrlConfiguration = Configuration(ConfigFactory.parseString(
    """
      | {
      |   time_transport: {
      |     not_a_url: configured_url
      |   }
      | }
    """.stripMargin
  ))

}
