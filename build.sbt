version := "1.0" // version del proyecto

scalaVersion := "2.13.8" // version a utilizar de scala

// raiz y nombre del proyecto
lazy val root = (project in file("."))
  .settings(
    name := "NutClimate"
  )

// versiones a utilizar
val PlayVersion = "2.8.10"
val AkkaVersion = "2.7.0"
val AkkaHttpVersion = "10.5.2"
val liftVersion = "3.5.0"
val cacheVersion = "5.2.1"
val slickVersion = "3.3.3"
val mysqlVersion = "8.0.28"

/*
* Agrega un repositorio llamado "Akka library repository" desde el cual se pueden descargar las dependencias de Akka
* que no están disponibles en los repositorios por defecto.
* */
resolvers += "Akka library repository".at("https://repo.akka.io/maven")

// Dependecias dispuestas para el desarrollo del proyecto
libraryDependencies ++= Seq(
  // Framework play para la utilizacion facil de akka
  "com.typesafe.play" %% "play" % PlayVersion,
  "net.liftweb" %% "lift-json" % liftVersion,

  // Dependencias necesarias de Akka
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-serialization-jackson" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,

  // Dependencia para guardar el cache de las consultas.
  "com.github.blemale" %% "scaffeine" % cacheVersion % "compile",

  // Dependencia para la conexion y manipulacion de la base de datos
  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "mysql" % "mysql-connector-java" % mysqlVersion // Dependencia del controlador JDBC de MySQL

)