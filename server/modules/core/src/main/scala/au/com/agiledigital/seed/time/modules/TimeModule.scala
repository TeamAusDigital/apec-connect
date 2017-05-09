package au.com.agiledigital.seed.time.modules

import java.time.Clock

import net.codingwell.scalaguice.ScalaModule

/**
  * Provides injected dependencies of the classes in the time packages.
  */
class TimeModule extends ScalaModule {
  override def configure(): Unit = {
    bind[Clock].toInstance(Clock.systemDefaultZone())
  }
}
