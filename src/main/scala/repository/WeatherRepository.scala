package repository
import models._
import slick.jdbc.MySQLProfile.api._
import scala.concurrent._
import scala.concurrent.duration.DurationInt

object WeatherRepository {

  private val db: Database = Database.forURL("jdbc:mysql://localhost:3306/clima", "root", "toor")

  private class Climas(tag: Tag) extends Table[(String, String, String)] (tag, "climas") {
    private def descripcion = column[String]("descripcion")
    private def icono = column[String]("icono")
    private def ubicacion = column[String]("ubicacion")
    def * = (descripcion, icono, ubicacion)
  }

  private class ClimasConsulta(tag: Tag) extends Table[WeatherConsulta](tag, "climas") {
    def id = column[Int]("idclima")
    def fecha = column[String]("fecha")
    private def descripcion = column[String]("descripcion")
    private def icono = column[String]("icono")
    private def ubicacion = column[String]("ubicacion")

    def * =
      (id, fecha, descripcion, icono, ubicacion) <> (WeatherConsulta.tupled, WeatherConsulta.unapply)
  }

  private val events = TableQuery[Climas]
  private val consultas = TableQuery[ClimasConsulta]

  def insertClima(w: Weather, ubicacion: String): Future[Int] = {
    val insertAtributes = (w.description, w.icon, ubicacion)
    val insertAction = events += insertAtributes
    db.run(insertAction)
  }

  def getClimas: Future[Seq[WeatherConsulta]] = {
    val query = consultas.sortBy(_.fecha.desc)
    db.run(query.result)
  }

  def deleteHistory(id: Int): Future[Int] = {
    val delete = consultas.filter(_.id === id)
    db.run(delete.delete)
  }

  def updateHistory(weather: WeatherConsulta): WeatherConsulta = {
    val update = consultas.filter(_.id === weather.id)
    Await.result(db.run(update.update(weather)), 5.second)
    Await.result(db.run(update.result.head), 5.second)
  }
}