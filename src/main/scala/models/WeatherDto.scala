package models

final case class Weather(id: Int, main: String, description: String, icon: String) // Clase de cada elemento consultado
final case class WeatherConsulta(id: Int, fecha:String, descripcion: String, icono: String, ubicacion: String)
final case class Weathers(weather: List[Weather]) // Clase de la lista de elementos consultados
