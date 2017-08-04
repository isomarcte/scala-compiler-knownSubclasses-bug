lazy val scodecVersion = "1.10.3"

lazy val commonSettings =
  Seq(
    organization := "io.isomarcte",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "2.12.3",
    scalacOptions ++= Seq(
      "-deprecation"
    ),
    crossScalaVersions := Seq(
      "2.11.9",
      "2.11.10",
      "2.11.11",
      "2.12.0",
      "2.12.1",
      "2.12.2",
      "2.12.3",
      "2.13.0-M2"
    )
  )

lazy val root =
  (
    project in file(".")
  ).settings(
    commonSettings
  ).aggregate(
    macro,
    uses_macro_works,
    scodec_shapeless,
    uses_macro)

lazy val macro = (
  project in file("macro")
).settings(
  commonSettings,
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ))

lazy val uses_macro =
  (
    project in file("uses_macro")
  ).settings(
    commonSettings
  ).dependsOn(macro)

lazy val uses_macro_works =
  (
    project in file("uses_macro_works")
  ).settings(
    commonSettings
  ).dependsOn(macro)

lazy val scodec_shapeless =
  (
    project in file("scodec_shapeless")
  ).settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scodec" %% "scodec-core" % scodecVersion
    )
  ).dependsOn()

lazy val scodec_shapeless_works =
  (
    project in file("scodec_shapeless_works")
  ).settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scodec" %% "scodec-core" % scodecVersion
    )
  ).dependsOn()
