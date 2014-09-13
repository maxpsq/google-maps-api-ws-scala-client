name := "google-maps-api-ws-scala-client"

version := "1.0"

scalaVersion := "2.11.1"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.3.4",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
    "org.specs2" %% "specs2" % "2.3.13" % "test"
)