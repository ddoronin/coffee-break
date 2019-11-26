ThisBuild / organization := "com.coffeebreak"
ThisBuild / scalaVersion := "2.12.10"
ThisBuild / version      := "0.1.0-SNAPSHOT"

lazy val global = (project in file("."))
    .settings(
        name := "Coffee Break"
    )

libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.0"
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.3.1"
