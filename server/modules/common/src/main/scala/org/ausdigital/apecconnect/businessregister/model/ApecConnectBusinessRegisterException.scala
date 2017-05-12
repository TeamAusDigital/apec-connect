package org.ausdigital.apecconnect.businessregister.model

/**
  * Provides specific exception to be thrown that is related APEC Connect Business Register.
  * @param message describes the reason of the exception.
  * @param cause provides information of why exception thrown.
  */
class ApecConnectBusinessRegisterException(message: String, cause: Throwable = null) extends Exception(message, cause)
