val ScalatraVersion = "2.3.2"

organization := "com.example"

name := "test-app"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.10.4"

resolvers += Classpaths.typesafeReleases

libraryDependencies ++= Seq(
  "org.scalatra" %% "scalatra" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalatest" % ScalatraVersion % "test",
  "org.scalatra" %% "scalatra-json" % ScalatraVersion,
  "org.scalatra" %% "scalatra-scalate" % ScalatraVersion,
  "org.scalatra" %% "scalatra-swagger"  % ScalatraVersion,
  "org.json4s"   %% "json4s-native" % "3.2.11",
  "org.json4s"   %% "json4s-jackson" % "3.2.11",
  "ch.qos.logback" % "logback-classic" % "1.2.3" % "runtime",
  "org.eclipse.jetty" % "jetty-webapp" % "9.4.8.v20171121" % "container",
  "javax.servlet" % "javax.servlet-api" % "3.1.0" % "provided",
  "mysql" % "mysql-connector-java" % "5.1.34",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "c3p0" % "c3p0" % "0.9.1.2"
)

enablePlugins(SbtTwirl)
enablePlugins(ScalatraPlugin)
