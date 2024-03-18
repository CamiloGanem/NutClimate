package utils

import com.typesafe.config
import com.typesafe.config.ConfigFactory

object Utils {
  // Carga la configuracion del cache contenida en el application.conf
  private val configuration: config.Config = ConfigFactory.load("application")

  // Mapea las configuraciones y construye la url del clima a consultar
  def getUrlRequest(location: String): String = {
    val protocol = getStringProperty("services.weather.protocol")
    val host = getStringProperty("services.weather.host")
    val path = getStringProperty("services.weather.path")
    val appid = getStringProperty("services.weather.appid")
    protocol + "://" + host + "/" + path + "?q=" + location + "&appid=" + appid + "&lang=es"
  }

  // Mapea las configuraciones y construye la url del icono del clima a consultar
  def getUrlImg(icon: String): String = {
    val protocol = getStringProperty("services.weather.img.protocol")
    val host = getStringProperty("services.weather.img.host")
    val path = getStringProperty("services.weather.img.path")
    val typeIcon = getStringProperty("services.weather.img.type-icon")
    protocol + "://" + host + "/" + path + icon + typeIcon
  }

  // Funcion para extrar la configuracion en string
  def getStringProperty(name: String): String = {
    configuration.getString(name)
  }

  // Funcion para extrar la configuracion en Int
  def getIntProperty(name: String): Integer = {
    configuration.getInt(name)
  }

  // Funcion para extrar la configuracion en Long
  def getLongProperty(name: String): Long = {
    configuration.getLong(name)
  }

}
