organization := "com.github.maxpsq"

name := "google-maps-api-ws-scala-client"

version := "0.2"

scalaVersion := "2.11.1"

resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
    "com.typesafe.play" %% "play-json" % "2.3.4",
    "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
    "org.specs2" %% "specs2" % "2.3.13" % "test"
)

/* Module name: it will be given to the packaged .jar file */
moduleName := "maxpsq-gmapsclient"

/* Sonatype repo credential for publishing (publish-signed) */
credentials += Credentials(Path.userHome / ".sbt" / ".sonatype_credentials.sbt") 

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <url>https://github.com/maxpsq/google-maps-api-ws-scala-client</url>
  <licenses>
    <license>
      <name>The MIT License (MIT)</name>
      <url>http://opensource.org/licenses/mit-license.php</url>
      <distribution>repo</distribution>
    </license>
  </licenses>
  <scm>
    <url>git@github.com:maxpsq/google-maps-api-ws-scala-client.git</url>
    <connection>scm:git:git@github.com:maxpsq/google-maps-api-ws-scala-client.git</connection>
  </scm>
  <developers>
    <developer>
      <id>maxpsq</id>
      <name>Massimo Pasquini</name>
      <url>https://github.com/maxpsq</url>
    </developer>
  </developers>
)

