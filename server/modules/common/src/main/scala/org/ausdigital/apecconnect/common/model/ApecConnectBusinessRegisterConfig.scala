package org.ausdigital.apecconnect.common.model

/**
  * Provides configurations required for the integration of APEC Connect Business Register API.
  * @param createUserApi url for create user API of APEC Connect Business Register.
  * @param businessLookupApi url for discovery businesses API of APEC Connect Business Register.
  */
final case class ApecConnectBusinessRegisterConfig(createUserApi: String, businessLookupApi: String)