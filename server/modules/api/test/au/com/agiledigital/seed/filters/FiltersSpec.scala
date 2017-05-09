package au.com.agiledigital.seed.filters

import au.com.agiledigital.rest.filters.ExpiresFilter
import au.com.agiledigital.rest.tests.BaseSpec

/**
  * Contains unit tests for the [[Filters]].
  */
class FiltersSpec extends BaseSpec {

  "Filters" should {
    "provide the injected filters" in {
      // Given an expires filter.
      val expiresFilter = mock[ExpiresFilter]

      // And a filters that combines them.
      val filters = new Filters(expiresFilter)

      // When the filters are fetched.
      val actual = filters.filters

      // Then they should be equal to the expected filters.
      actual must_=== Seq(expiresFilter)
    }
  }

}
