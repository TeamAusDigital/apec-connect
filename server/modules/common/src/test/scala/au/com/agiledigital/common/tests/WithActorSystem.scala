package au.com.agiledigital.common.tests

import java.util.UUID

import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestKit, TestKitBase }
import com.typesafe.config.{ Config, ConfigFactory }
import org.specs2.execute.{ AsResult, Result }
import org.specs2.mutable.Around

import scala.language.postfixOps

/**
  * Test context that supplies and destroys an actor system and Akka testkit.
  */
trait WithActorSystem extends Around with TestKitBase with ImplicitSender {

  def config: Config = ConfigFactory.parseString(WithActorSystem.config)

  implicit lazy val system: ActorSystem = ActorSystem(
    "test-actor-system-" + UUID.randomUUID().toString,
    config
  )

  override def around[T](t: => T)(implicit evidence: AsResult[T]): Result = {
    try {
      AsResult.effectively(t)
    }
    finally {
      TestKit.shutdownActorSystem(system)
    }
  }
}

/**
  * Companion that contains the default actor system configuration for tests.
  */
object WithActorSystem {
  val config =
    """
      |    akka {
      |      loglevel = "INFO"
      |      actor.debug.fsm = true
      |    }
    """.stripMargin
}
