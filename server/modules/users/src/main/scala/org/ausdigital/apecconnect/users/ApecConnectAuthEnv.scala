package org.ausdigital.apecconnect.users

import com.mohiva.play.silhouette.api.Env
import com.mohiva.play.silhouette.impl.authenticators.JWTAuthenticator
import org.ausdigital.apecconnect.users.model.ApecConnectUser

/**
  * Authentication environment used by Apec Connect.
  *
  * Defines the type of Identity (ApecConnectUsers) and the authentication mechanism (JWT Tokens).
  */
trait ApecConnectAuthEnv extends Env {
  type I = ApecConnectUser
  type A = JWTAuthenticator
}