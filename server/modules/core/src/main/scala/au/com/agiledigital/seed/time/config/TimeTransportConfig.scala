package au.com.agiledigital.seed.time.config

import javax.inject.Inject

import au.com.agiledigital.common.config.RequiredConfiguration
import com.google.inject.{ ProvidedBy, Provider }
import play.api.Configuration

/**
  * Configuration for fetching time using the [[au.com.agiledigital.seed.time.services.TimeTransport]].
  *
  * @param url the URL of the remote time service.
  */
@ProvidedBy(classOf[TimeTransportConfigProvider])
final case class TimeTransportConfig(url: String)

/**
  * Uses the supplied configuration to create instances of the TimeTransport configuration.
  *
  * @param configuration the configuration that will be used.
  */
class TimeTransportConfigProvider @Inject() (configuration: Configuration) extends Provider[TimeTransportConfig] {

  /**
    * Base path for configuration values.
    */
  private val basePath = "time_transport"

  override def get(): TimeTransportConfig = {
    val url = configuration.getRequiredString(basePath + ".url")
    new TimeTransportConfig(url)
  }
}

