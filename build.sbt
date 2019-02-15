name := "rest_api_assignment"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies ++= Seq(
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.1.1",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.slick" %% "slick" % "3.1.1",
  "org.apache.tika" % "tika-parsers" % "1.10",
  "com.typesafe.akka" %% "akka-http" % "10.0.10",
  "org.json4s" %% "json4s-native" % "3.5.0",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.0.10" % Test,
  "org.scalatest" %% "scalatest" % "3.0.5" % Test,
  "com.h2database" % "h2" % "1.4.187" % "test",
  "org.mockito" % "mockito-all" % "1.9.5" % Test
)


    