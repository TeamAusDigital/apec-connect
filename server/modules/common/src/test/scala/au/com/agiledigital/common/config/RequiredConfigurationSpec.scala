package au.com.agiledigital.common.config
import au.com.agiledigital.rest.tests.BaseSpec
import play.api.Configuration

/**
  * Tests for [[RequiredConfiguration]].
  */
class RequiredConfigurationSpec extends BaseSpec {
  "Getting a required string config value" should {

    "throw if the value is missing" in {
      // Given an empty Configuration.
      val config = Configuration.empty

      // When we get a value via RequiredConfiguration.
      val requiredConfig = new RequiredConfiguration(config)
      def result = requiredConfig.getRequiredString("foo")

      // Then it throws.
      result must throwAn[Exception](message = "Configuration error\\[\\[foo\\] configuration value not specified\\.\\]")
    }

    "return the value if it exists" in {
      // Given a Configuration with a config value.
      val config = Configuration("foo" -> "bar")

      // When we get the config value via RequiredConfiguration.
      val requiredConfig = new RequiredConfiguration(config)
      val result = requiredConfig.getRequiredString("foo")

      // Then it returns the config value.
      result must beEqualTo("bar")
    }
  }

  "Getting a required int config value" should {

    "throw if the value is missing" in {
      // Given an empty Configuration.
      val config = Configuration.empty

      // When we get a value via RequiredConfiguration.
      val requiredConfig = new RequiredConfiguration(config)
      def result = requiredConfig.getRequiredInt("foo")

      // Then it throws.
      result must throwAn[Exception](message = "Configuration error\\[\\[foo\\] configuration value not specified\\.\\]")
    }

    "return the value if it exists" in {
      // Given a Configuration with a config value.
      val config = Configuration("foo" -> 99)

      // When we get the config value via RequiredConfiguration.
      val requiredConfig = new RequiredConfiguration(config)
      val result = requiredConfig.getRequiredInt("foo")

      // Then it returns the config value.
      result must beEqualTo(99)
    }
  }

}