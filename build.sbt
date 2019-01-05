
name := "test-runner"

version := "0.1.03-SNAPSHOT"

scalaVersion := "2.12.8"

organization := "com.cooltool"

description := "A test automation tool library"

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-reflect" % "2.12.8",
  "com.typesafe" % "config" % "1.3.2",
  "org.json4s" % "json4s-native_2.11" % "3.6.3",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "joda-time" % "joda-time" % "2.10.1",
  "org.scalactic" %% "scalactic" % "3.0.5",
  "org.scalatest" %% "scalatest" % "3.0.5",
  "org.testng" % "testng" % "6.9.10"
)

trapExit := false

parallelExecution in Test := false

scalacOptions ++= Seq("-feature", "-language:postfixOps", "-language:implicitConversions")

assemblyMergeStrategy in assembly := {
  case x if x.startsWith("META-INF") => MergeStrategy.discard
  case x if x.contains("pom.properties") => MergeStrategy.discard
  case PathList(ps@_*) if ps.last endsWith ".txt" => MergeStrategy.first
  case "application.conf" => MergeStrategy.concat
  case "reference.conf" => MergeStrategy.concat
  case "unwanted.txt" => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}
