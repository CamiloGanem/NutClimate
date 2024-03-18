package controllers

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server.Directives._
import clients.WeatherExternalApi.system
import models.{Weather, WeatherConsulta}
import net.liftweb.json._
import services.WeatherService
import play.api.libs.json.{Json, OFormat}
import akka.http.scaladsl.model.MediaTypes

import scala.concurrent.{ExecutionContextExecutor, Future}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshaller

// Controlador que contiene los endpoints para consultar el clima por ubicación y el historial(en desarrollo)
object WeatherController {

  implicit val executionContext: ExecutionContextExecutor = system.executionContext
  implicit val formats: DefaultFormats.type = DefaultFormats
  implicit val serialization: OFormat[WeatherConsulta] = Json.format[WeatherConsulta]
  implicit val serialization2: OFormat[Weather] = Json.format[Weather]

  // Define el Unmarshaller para dar respuesta json con WeatherConsulta
  implicit val weatherConsultaUnmarshaller: Unmarshaller[HttpEntity, WeatherConsulta] =
    Unmarshaller.stringUnmarshaller
      .forContentTypes(MediaTypes.`application/json`)
      .map { data =>
        Json.fromJson[WeatherConsulta](Json.parse(data)).get
      }

  private val weather =
    path("weather") {
      get {
        parameters("location".as[String]) { location => {
          val html = WeatherService.getWeather(location)
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, html))
        }
        }
      }
    }

  private val weatherRoute =
    path("weatherJson") {
      get {
        parameters("location".as[String]) { location => {
          val weather = WeatherService.getWeatherJson(location)
          complete(HttpEntity(ContentTypes.`application/json`, Json.obj("weather" -> weather).toString()))
        }
        }
      }
    }

  private val updateRoute =
    path("update") {
      post {
        // Utiliza el método entity(as[Type]) para extraer el JSON del cuerpo de la solicitud
        entity(as[WeatherConsulta]) { datos => {
          val result = WeatherService.updateWeathersHistory(datos)
          complete(HttpResponse(entity =
            HttpEntity(ContentTypes.`application/json`, Json.obj("updated" -> result).toString())))
        }
        }
      }
    }


  private val historyRoute =
    path("history") {
      get {
        val weathersFuture = WeatherService.getWeathersHistory
        val jsonResponseFuture: Future[HttpResponse] = weathersFuture.map { weathers =>
          HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, Json.obj("data" -> weathers).toString()))
        }
        complete(jsonResponseFuture)
      }
    }

  private val deleteRoute =
    path("delete") {
      delete {
        parameters("id".as[Int]) { id => {
          val result = WeatherService.deleteWeathersHistoryById(id)
          complete(HttpResponse(entity =
            HttpEntity(ContentTypes.`application/json`,
              Json.obj(
                "data" -> result.toString,
                "mensaje" -> s"Eliminado correctamente con id: $id"
              ).toString())))
        }
        }
      }
    }


  val route: Route = weather ~ weatherRoute ~ historyRoute ~ deleteRoute ~ updateRoute
}
