package au.com.agiledigital.seed.time.services

import java.time.format.DateTimeFormatter
import java.time.{ Clock, Instant, LocalDateTime, ZoneOffset }
import javax.inject.Inject

import au.com.agiledigital.kamon.play_extensions.Metrics
import au.com.agiledigital.seed.time.model.Time

import scala.concurrent.{ ExecutionContext, Future }

/**
  * Supplies system time information.
  */
class TimeService @Inject() (metrics: Metrics, clock: Clock, timeTransport: TimeTransport) {

  /**
    * Add a category to the system metrics to make them easier to plot.
    */
  val systemTimeMetrics = metrics.withCategory("system-time")

  /**
    * Returns the current system time in a variety of formats.
    * @return the current system time.
    */
  def systemTime(): Time = systemTimeMetrics.traced {
    val dateTime = LocalDateTime.now(clock)
    val millis = dateTime.atZone(ZoneOffset.UTC).toInstant.toEpochMilli

    Time(
      dateTime.toLocalDate.format(DateTimeFormatter.ISO_DATE),
      dateTime.format(DateTimeFormatter.ISO_DATE_TIME),
      millis
    )
  }

  /**
    * Returns the current time of a remote system in a variety of formats.
    * @param ec the execution context that will be used to map the response from the remote system.
    * @return a Future that will provider the remote system time.
    */
  def remoteTime()(implicit ec: ExecutionContext): Future[Time] = systemTimeMetrics.traced {
    val remoteTimeFuture = timeTransport.fetchTime()

    remoteTimeFuture map { millis =>

      val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC)

      Time(
        dateTime.toLocalDate.format(DateTimeFormatter.ISO_DATE),
        dateTime.format(DateTimeFormatter.ISO_DATE_TIME),
        millis
      )
    }

  }

}
