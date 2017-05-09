import sbt._

object Dependencies {

  val CompileAndTest = "test->test;compile->compile"

  val Specs2Version = "3.6.5"

  val PlayVersion = "2.5.1"

  val KamonVersion = "0.6.0"

  val AgileKamonVersion = "0.5"

  val SlickVersion = "3.1.1"

  val PlaySlickVersion = "2.0.0"

  val AkkaVersion = "2.4.3"

  val SilhouetteVersion = "4.0.0"

  val FicusVersion = "1.4.0"

  val Overrides = Set(
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
    "com.typesafe.akka" %% "akka" % AkkaVersion,
    "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    "com.typesafe.play" %% "play" % Dependencies.PlayVersion
  )

  val AkkaDependencies = Seq(
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion,
    "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
    "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test
  )

  val KamonDependencies = Seq(
    "org.aspectj" % "aspectjweaver" % "1.8.6",
    "io.kamon" %% "kamon-core" % KamonVersion,
    "io.kamon" %% "kamon-play-25" % "0.6.1", // Different version number, as this is supplied by Agile at the moment.
    "io.kamon" %% "kamon-statsd" % KamonVersion,
    "io.kamon" %% "kamon-scala" % KamonVersion,
    "io.kamon" %% "kamon-log-reporter" % KamonVersion,
    "io.kamon" %% "kamon-akka" % KamonVersion,
    "io.kamon" %% "kamon-system-metrics" % KamonVersion,
    "com.lihaoyi" %% "sourcecode" % "0.1.1",
    "au.com.agiledigital" %% "play-kamon-extensions" % AgileKamonVersion,
    "au.com.agiledigital" %% "play-kamon-extensions-testkit" % AgileKamonVersion % Test
  )

  val PlayDependencies = Seq(
    "net.codingwell" %% "scala-guice" % "4.0.1",
    "com.typesafe.play" %% "play" % Dependencies.PlayVersion,
    "com.typesafe.play" %% "play-ws" % Dependencies.PlayVersion
  )

  val SilhouetteDependencies = Seq(
    "com.mohiva" %% "play-silhouette" % SilhouetteVersion,
    "com.mohiva" %% "play-silhouette-password-bcrypt" % SilhouetteVersion,
    "com.mohiva" %% "play-silhouette-crypto-jca" % SilhouetteVersion,
    "com.mohiva" %% "play-silhouette-persistence" % SilhouetteVersion,
    "com.mohiva" %% "play-silhouette-testkit" % SilhouetteVersion % "test"
  )

  val DatabaseDependencies = Seq(
    "com.typesafe.slick" %% "slick" % SlickVersion,
    "com.typesafe.play" %% "play-slick" % PlaySlickVersion,
    "com.typesafe.play" %% "play-slick-evolutions" % PlaySlickVersion,
    "org.postgresql" % "postgresql" % "9.4.1208.jre7",
    "au.com.agiledigital" %% "dao-slick" % "0.0.1",
    "com.h2database" % "h2" % "1.4.191" % Test
  )

  val HealthCheckDependencies = Seq(
    "au.com.agiledigital" %% "play2-health-checker" % "3.2.32"
  )

  val SpecsDependencies = Seq(
    "org.specs2" %% "specs2-core" % Specs2Version % Test,
    "org.specs2" %% "specs2-junit" % Specs2Version % Test,
    "org.specs2" %% "specs2-matcher-extra" % Specs2Version % Test,
    "org.specs2" %% "specs2-analysis" % Specs2Version % Test,
    "org.specs2" %% "specs2-mock" % Specs2Version % Test,
    "com.typesafe.play" %% "play-specs2" % PlayVersion % Test,
    "de.leanovate.play-mockws" %% "play-mockws" % "2.5.0" % Test
  )

  val ConfigDependencies = Seq(
    "com.iheart" %% "ficus" % FicusVersion
  )
}
