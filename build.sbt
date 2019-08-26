name := "blog-post-akka"

lazy val akkaHttpVersion  = "10.1.9"
lazy val akkaStreamVersion      = "2.5.23"
lazy val akkaSlickVersion = "1.1.1"

lazy val akkaHttp = "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion
lazy val akkaStream  = "com.typesafe.akka" %% "akka-stream"          % akkaStreamVersion
lazy val akkaSlick =  "com.lightbend.akka" %% "akka-stream-alpakka-slick" % akkaSlickVersion
// lazy val doobiePostgres = "org.tpolecat"      %% "doobie-postgres"      % "0.5.3"
lazy val akkaStreamTest = "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.19"
lazy val akkaHttpTest = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion

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
    Resolver.bintrayRepo("naftoligug", "maven"),
    "bintray" at "https://api.bintray.com/maven/naftoligug/maven/slick-migration-api",
    Resolver.jcenterRepo,
    "Artima Maven Repository" at "http://repo.artima.com/releases"
  )
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      akkaHttp,
      akkaStream,
      akkaSlick,
    )
  )