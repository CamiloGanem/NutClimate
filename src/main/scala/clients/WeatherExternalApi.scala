package clients

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import models._
import net.liftweb.json._
import utils.Utils

import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.concurrent.duration.DurationInt

// Objeto que realiza la comunicacion con la api externa de OpenWeatherMap.
object WeatherExternalApi {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "weatherSystem")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext
  implicit val formats: DefaultFormats.type = DefaultFormats

  /*
  * Funcion que se encarga de enviar la peticion a la api externa con la ubicacion determinada (location)
  * y a su vez recibe la respuesta convitiendola a utf8.
  * */
  private def sendRequest(location: String): Future[String] = {
    val request = HttpRequest(
      method = HttpMethods.GET,
      uri = Utils.getUrlRequest(location)
    )
    val responseFuture: Future[HttpResponse] = Http().singleRequest(request)
    val entityFuture: Future[HttpEntity.Strict] =
      responseFuture.flatMap(_.entity.toStrict(5.seconds))
    entityFuture.map(_.data.utf8String)
  }

  // Funcion que obtiene la peticion previamente realizada y la convierte objeto de tipo Weather
  def getWeatherLocation(location: String): Weather = {
    val response = sendRequest(location)
      .map(x => {
        val jValue = parse(x)
        val weathers: Weathers = jValue.extract[Weathers]
        weathers.weather.head
      })
      .recover(a => {
        println("-------------> Ocurrio un error <-----------------")
        println(a)
      })
    // espera de manera sincrona 5 segundos la respuesta
    Await.result(response, 5.second).asInstanceOf[Weather]
  }
}
