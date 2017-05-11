package org.ausdigital.apecconnect.participants.model

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator

trait ParticipantAuthEnv extends Env {
  type I = ParticipantIdentity
  type A = JWTAuthenticator
}
