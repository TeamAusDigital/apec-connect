// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.1")

// The scala style plugin
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

// Coverage plugin
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.5")

// Wart removers
addSbtPlugin("org.brianmckenna" % "sbt-wartremover" % "0.14")

// Play wart removers
addSbtPlugin("org.danielnixon" % "sbt-playwarts" % "0.18")

// For running the project with Kamon metrics that require weaving
addSbtPlugin("io.kamon" % "aspectj-play-runner" % "0.1.3")
