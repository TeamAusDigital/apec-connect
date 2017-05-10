package org.ausdigital.apecconnect.modules

import java.time.Clock

import com.google.inject.AbstractModule

/**
 * Created by haolinj on 10/05/17.
 */
class ApecConnectModule extends AbstractModule {

  override def configure(): Unit = {
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone())
  }

}
