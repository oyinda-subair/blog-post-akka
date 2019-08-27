name := "blog-post-akka"

val akkaHttpVersion    = "10.1.9"
val akkaStreamVersion  = "2.5.23"
val akkaSlickVersion   = "1.1.1"
val playJsonVersion    = "2.7.3"
val flywayVersion      = "5.0.2"
val scalaTestVersion   = "3.0.8"

val akkaHttp       = "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion
val akkaStream     = "com.typesafe.akka" %% "akka-stream"          % akkaStreamVersion
val playJson       = "com.typesafe.play" %% "play-json"            % playJsonVersion
val playJsonSupport = "de.heikoseeberger" %% "akka-http-play-json" % "1.27.0"
val bcyrpt = "org.mindrot" % "jbcrypt" % "0.3m"

val akkaSlick      =  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % akkaSlickVersion
val postgres       = "org.postgresql" % "postgresql" % "42.2.6"
val flywayCore     = "org.flywaydb" % "flyway-core" % flywayVersion

val scalaTest      = "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
val akkaStreamTest = "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.19" % Test
val akkaTest       = "com.typesafe.akka" %% "akka-testkit" % "2.5.19" % Test
val akkaHttpTest   = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test

val slf4j          = "org.slf4j" % "slf4j-log4j12" % "1.7.26"

lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.12.8",
  scalacOptions ++= Seq(
    "-feature",
    "-deprecation",
    "-Xfatal-warnings"
  ),
  resolvers ++= Seq(
    "typesafe" at "http://repo.typesafe.com/typesafe/releases/",
    Resolver.jcenterRepo,
    "Artima Maven Repository" at "http://repo.artima.com/releases"
  )
)

lazy val root = (project in file("."))
  .enablePlugins(FlywayPlugin)
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      akkaHttp,
      akkaStream,
      akkaSlick,
      akkaStreamTest,
      akkaHttpTest,
      bcyrpt,
      flywayCore,
      postgres,
      playJson,
      playJsonSupport,
      scalaTest
    )
  )

flywayUrl := "jdbc:postgresql://localhost:5432/blogpost"
flywayUser := "postgres"
flywayPassword := ""
flywayLocations += "db/migration"
flywayUrl in Test := "jdbc:postgresql://localhost:5432/blogpost"
flywayUser in Test := "postgres"
flywayPassword in Test := ""
flywayBaselineOnMigrate := true