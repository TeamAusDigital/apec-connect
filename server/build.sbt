val ProjectName = "apec-connect"

lazy val commonSettings = Seq(
  organization := "org.ausdigital",
  scalaVersion := "2.11.8",
  javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint:unchecked", "-encoding", "UTF-8"),
  scalacOptions ++= Seq(
    "-deprecation", // Emit warning and location for usages of deprecated APIs.
    "-feature", // Emit warning and location for usages of features that should be imported explicitly.
    "-unchecked", // Enable additional warnings where generated code depends on assumptions.
    "-Xfatal-warnings", // Fail the compilation if there are any warnings.
    "-Xlint", // Enable recommended additional warnings.
    "-Ywarn-adapted-args", // Warn if an argument list is modified to match the receiver.
    "-Ywarn-dead-code", // Warn when dead code is identified.
    "-Ywarn-inaccessible", // Warn about inaccessible types in method signatures.
    "-Ywarn-nullary-override", // Warn when non-nullary overrides nullary, e.g. def foo() over def foo.
    "-Ywarn-numeric-widen" // Warn when numerics are widened.
  ),
  resolvers ++= Seq(
    "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases",
    "Atlassian Releases" at "https://maven.atlassian.com/public/",
    "Agile Digital" at "http://nexus.agiledigital.com.au/nexus/content/repositories/releases",
    "dnvriend at bintray" at "http://dl.bintray.com/dnvriend/maven",
    "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases",
    Opts.resolver.sonatypeSnapshots
  ),
  // Disable scaladoc generation when building dist.
  sources in (Compile, doc) := Seq.empty,
  publishArtifact in (Compile, packageDoc) := false,
  updateOptions := updateOptions.value.withCachedResolution(true),
  concurrentRestrictions in Global := Seq(
    Tags.limitSum(1, Tags.Compile, Tags.Test),
    Tags.limitAll(2)
  ),
  dependencyOverrides ++= Dependencies.Overrides,
  libraryDependencies ++= Seq(
    "org.scalaz"              %% "scalaz-core"               % "7.2.9",
    "com.github.nscala-money" %% "nscala-money"              % "0.12.3",
    "com.github.nscala-money" %% "nscala-money-play-json"    % "0.12.3",
    "au.com.agiledigital"     %% "play-rest-support"         % "0.0.3",
    "au.com.agiledigital"     %% "play-rest-support-testkit" % "0.0.3" % Test
  ),
  libraryDependencies ++= Dependencies.PlayDependencies,
  libraryDependencies ++= Dependencies.KamonDependencies,
  libraryDependencies ++= Dependencies.HealthCheckDependencies,
  libraryDependencies ++= Dependencies.SpecsDependencies,
  libraryDependencies ++= Dependencies.AkkaDependencies,
  libraryDependencies ++= Dependencies.ConfigDependencies
)

lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := ProjectName + "-root"
  )
  .aggregate(
    core,
    api,
    common,
    db,
    participants
  )

lazy val common = (project in file("modules/common"))
  .settings(commonSettings: _*)
  .settings(
    version := (version in LocalProject("root")).value,
    name := ProjectName + "-common",
    libraryDependencies ++= Dependencies.PlayDependencies
  )

lazy val api = (project in file("modules/api"))
  .enablePlugins(PlayScala)
  .settings(commonSettings: _*)
  .settings(
    version := (version in LocalProject("root")).value,
    name := ProjectName + "-api",
    libraryDependencies ++= Seq(
      "io.kanaka" %% "play-monadic-actions"            % "2.0.0",
      "io.kanaka" %% "play-monadic-actions-scalaz_7-2" % "2.0.0"
    ),
    bashScriptExtraDefines ++= Seq(
      """addJava "-javaagent:${lib_dir}/org.aspectj.aspectjweaver-1.8.6.jar"""",
      """addJava "-Dorg.aspectj.tracing.factory=default"""" // See https://github.com/playframework/playframework/issues/5997
    ),
    // Exclude generated classes from warts.
    wartremoverExcluded += crossTarget.value / "routes" / "main" / "api" / "Routes.scala",
    wartremoverExcluded += crossTarget.value / "routes" / "main" / "api" / "RoutesPrefix.scala",
    wartremoverExcluded += crossTarget.value / "routes" / "main" / "controllers" / "ReverseRoutes.scala",
    wartremoverExcluded += crossTarget.value / "routes" / "main" / "controllers" / "javascript" / "JavaScriptReverseRoutes.scala",
    // Exclude generated classes from coverage.
    coverageExcludedPackages := """.*controllers\..*Reverse.*;.*api.Routes.*;"""
  )
  .dependsOn(
    core         % Dependencies.CompileAndTest,
    common       % Dependencies.CompileAndTest,
    participants % Dependencies.CompileAndTest
  )

lazy val core = (project in file("modules/core"))
  .settings(commonSettings: _*)
  .settings(
    version := (version in LocalProject("root")).value,
    name := ProjectName + "-core",
    coverageEnabled.in(Test, test) := true,
    libraryDependencies ++= Dependencies.SilhouetteDependencies
  )
  .dependsOn(
    common       % Dependencies.CompileAndTest,
    participants % Dependencies.CompileAndTest
  )

lazy val db = (project in file("modules/db"))
  .settings(commonSettings: _*)
  .settings(
    version := (version in LocalProject("root")).value,
    name := ProjectName + "-db",
    libraryDependencies ++= Dependencies.DatabaseDependencies
  )
  .dependsOn(common % Dependencies.CompileAndTest)

lazy val participants = (project in file("modules/participants"))
  .settings(commonSettings: _*)
  .settings(
    version := (version in LocalProject("root")).value,
    name := ProjectName + "-participants",
    libraryDependencies ++= Dependencies.SilhouetteDependencies
  )
  .dependsOn(
    common % Dependencies.CompileAndTest,
    db     % Dependencies.CompileAndTest
  )

addCommandAlias("run-api", ";project api; run 9000 -Dkamon.modules.kamon-system-metrics.auto-start=false")

// Kamon 0.6.0 with akka tracing enabled requires setting the config file as a workaround.
addCommandAlias(
  "run-api-weaved",
  ";project api; aspectj-runner:run  -Dkamon.modules.kamon-system-metrics.auto-start=false -Dconfig.file=api/conf/application.conf 9000"
)
