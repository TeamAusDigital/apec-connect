package au.com.agiledigital.seed.filters

import javax.inject.Inject

import au.com.agiledigital.rest.filters.ExpiresFilter
import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter

/**
  * Defines the filters that will be used by the application.
  *
  * @param expires the expires filter.
  */
class Filters @Inject() (expires: ExpiresFilter) extends HttpFilters {

  override def filters: Seq[EssentialFilter] = Seq(expires)
}
