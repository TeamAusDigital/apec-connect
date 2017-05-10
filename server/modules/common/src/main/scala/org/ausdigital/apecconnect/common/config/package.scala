package au.com.agiledigital.common

import play.api.{ PlayException, Configuration }

/**
 * Contains EMLs, etc related to configuration.
 */
package object config {
  implicit class RequiredConfiguration(config: Configuration) {

    /**
     * Equivalent to getInt but throws an exception if the configuration key is not set.
     * @param path the configuration key, relative to the configuration root key
     * @throws PlayException if the configuration key is not set.
     * @return a configuration value
     */
    def getRequiredInt(path: String): Int = throwIfNone(config.getInt(path), path)

    /**
     * Equivalent to getString but throws an exception if the configuration key is not set.
     * @param path the configuration key, relative to the configuration root key
     * @throws PlayException if the configuration key is not set.
     * @return a configuration value
     */
    def getRequiredString(path: String): String = throwIfNone(config.getString(path), path)

    private def throwIfNone[A](configValue: Option[A], path: String): A = {
      configValue.getOrElse(throw config.reportError(path, s"[$path] configuration value not specified."))
    }
  }
}
