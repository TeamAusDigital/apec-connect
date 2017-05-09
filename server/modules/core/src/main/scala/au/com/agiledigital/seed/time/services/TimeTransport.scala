package au.com.agiledigital.seed.time.services

import javax.inject.Inject

import au.com.agiledigital.seed.time.config.TimeTransportConfig
import play.api.libs.ws.WSClient

import scala.concurrent.{ ExecutionContext, Future }

/**
  * Fetches the current time in millis of a remote system.
  */
class TimeTransport @Inject() (wsClient: WSClient, timeTransportConfig: TimeTransportConfig) {

  /**
    * Fetches the time (in milliseconds since epoch) from the remote system.
    *
    * @param ec execution context used to map the results.
    * @return a Future that will contain the time in milliseconds.
    */
  def fetchTime()(implicit ec: ExecutionContext): Future[Long] = {
    val responseFuture = wsClient.url(timeTransportConfig.url).withQueryString("""\s""" -> "").get()
    responseFuture map { response =>
      import au.com.agiledigital.common.parsers._
      // Convert from seconds to millis since epoch.
      response.body.toLongUnsafe * 1000
    }
  }
}
