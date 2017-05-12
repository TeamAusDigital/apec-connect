package org.ausdigital.apecconnect.modules

import java.time.Clock

import com.google.inject.{ AbstractModule, TypeLiteral }
import com.mohiva.play.silhouette.api.SilhouetteProvider
import org.ausdigital.apecconnect.auth.ApecConnectSilhouette
import org.ausdigital.apecconnect.participants.model.ParticipantAuthEnv

class ApecConnectModule extends AbstractModule {

  override def configure(): Unit = {
    bind(new TypeLiteral[ApecConnectSilhouette] {}).to(new TypeLiteral[SilhouetteProvider[ParticipantAuthEnv]] {})
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone())
  }

}
