package utils

import com.github.blemale.scaffeine._
import models.Weather
import scala.concurrent.duration.Duration

// Objeto que maneja el cache de la consultas.
object Cache {
  // Extrae las propiedades del archivo de configuracion
  private val expire: Duration = Duration(Utils.getStringProperty("cache.expire"))
  private val size: Long = Utils.getLongProperty("cache.size")

  // Crear un funcion que se encarga de construir la cache, haciendo uso de la biblioteca Scaffeine
  private val cache: Cache[String, Weather] =
    Scaffeine()
      .recordStats()
      .expireAfterWrite(Duration.fromNanos(expire.toNanos))
      .maximumSize(size)
      .build[String, Weather]()

  // Funcion que agrega el cache
  def putCache(name: String, value: Weather) {
    cache.put(name, value)
  }

  // Funcion que extrae el cache almacenado
  def getCache(name: String): Weather = {
    val test = cache.getIfPresent(name)
    test match {
      case None => null
      case Some(testValue: Weather) => testValue
    }
  }

}
