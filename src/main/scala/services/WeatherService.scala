package services

import repository.WeatherRepository
import clients.WeatherExternalApi
import models._
import utils._
import scala.concurrent.Future

object WeatherService {
  private def insertLogWeather(clima: Weather, ubicacion: String): Future[Int] = {
    WeatherRepository.insertClima(clima, ubicacion)
  }

  def getWeather(location: String): String = {
    var weatherResponse: Weather = Cache.getCache(location);
    if (weatherResponse == null) {
      weatherResponse = WeatherExternalApi.getWeatherLocation(location)
      Cache.putCache(location, weatherResponse)
    }
    insertLogWeather(weatherResponse, location)
    val img = Utils.getUrlImg(weatherResponse.icon)
    val html = scala.io.Source
      .fromInputStream(
        getClass.getResourceAsStream("/views/weather.html")
      )
      .mkString
      .replace("{{location}}", location)
      .replace("{{description}}", weatherResponse.description)
      .replace("{{img}}", img)
    html
  }

  def getWeatherJson(location: String): Weather = {
    var weatherResponse: Weather = Cache.getCache(location)
    if (weatherResponse == null) {
      weatherResponse = WeatherExternalApi.getWeatherLocation(location)
      Cache.putCache(location, weatherResponse)
    }
    insertLogWeather(weatherResponse, location)
    println("Se inserto clima correctamente")
    weatherResponse
  }

  def getWeathersHistory: Future[Seq[WeatherConsulta]] = WeatherRepository.getClimas

  def deleteWeathersHistoryById(id: Int): Future[Int] = WeatherRepository.deleteHistory(id)

  def updateWeathersHistory(weather: WeatherConsulta): WeatherConsulta =
    WeatherRepository.updateHistory(weather)
}
