import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.http.scaladsl.Http
import controllers.WeatherController
import utils.Utils

import scala.io.StdIn
import scala.concurrent.ExecutionContext.Implicits.global

// import scala.util.Try
// Codigo de estudio comentado
/*
object Main {
  def main(args: Array[String]): Unit = {
    println("Hola Mundoo")

    // Inmutabilidad
    var x = 1
    println("X: " + x)
    x = 2
    println("X: " + x)

    val y = 1
    println("Y: " + y)
    // y = 2. no se puede

    def z = 1

    println("Z: " + z)
    // z = 2. no se puede

    // Expresiones()
    // Funciones()
    // Reto1()
    // Colecciones()
    // Reto2()
    // TuplasObjetos()
    // PatternMatching()
    // Reto3()
    // Recursion()
    //Agregacion()
    //RazonamientoConTipos()
    //TiposGenericos()
    //DatosAlgebraicos()
    //EvaluacionPeresoza()
    //Disyunciones()
  }
}

  private def Expresiones(): Unit = {
    println("\nExpresiones")

    def x = 3
    // def x = (3)
    // def x = {3}
    println("X: " + x)

    def z = {
      1; 1 + 2
    }
    // def z = (1;  1 + 2 ) no se puede, porque el parentesis indica que puede haber solo una expresion
    println("Z: " + z)

    // el if en lenguajes funcionales siempre va retornar algo
    println(if (x != 3) "no es tres" else "es tres")
    println(if (x != 3) "no es tres")
  }

  private def Funciones(): Unit = {
    println("\nFunciones")

    println("Normales")

    def f(x: Int) = x * x

    println(f(2))
    println(f(3))

    println("Anonimas")
    val a = (x: Int) => x * x
    println(a(2))
    println(a(4))

    // funciones tratadas como objeto
    println(a.apply(2)) // se utiliza el apply porque es una funcion tratada como objeto
    val c = f _ // convertimos f para tratarla como objeto
    println(c.apply(5))

    // funciones de orden superior, es decir, que una funcion reciba como parametro otra funcion
    // como tambien retornar funciones

    println("orden superior")

    def g(h: Int => Int) = h(3)

    println(g(f))

    // los lenguajes fucniales permiten encadenar valores de entrada (Current)
    def k(h: Int => Int)(x: Int) = h(x)

    println(k(f)(4))

    println("como metodos")
    object Util {
      def metodo(x: Int): Int = x + x

      val a = metodo _
    }
    println(Util.metodo(3))
    println(Util.a(3))
  }

  /*
  * Crear una funion que genere una funcion para calcular
  * si un numero es mayor que el parametro que se le paso
  * */
  private def Reto1(): Unit = {
    println("\nReto 1")

    // mi solucion
    def generador(f: (Int, Int) => String)(v1: Int)(v2: Int) = f(v1, v2)

    def numeroMayor(v1: Int, v2: Int) = if (v1 > v2) "v1 Es mayor" else "v1 Es menor"

    println(generador(numeroMayor)(1)(3))

    //Solucion platzi:  Funcion que genera funciones
    def isGreaterGenerator(ref: Int): Int => Boolean = {
      (x: Int) => ref < x
    }

    // Usando el generador de funciones
    val isGreaterThanTen = isGreaterGenerator(10)

    // Usando la funcion generada
    println(isGreaterThanTen(11)) // true
    println(isGreaterThanTen(1)) // false
    println(isGreaterThanTen(10)) // false
  }

  //Documentacion scala: https://docs.scala-lang.org/overviews/collections-2.13/overview.html
  // Secuencias, Conjuntos, Mapas
  private def Colecciones(): Unit = {
    println("\nColecciones")

    // Listas (List, Seq, Array)
    println("Listas")
    val a1 = Seq(1, 2, 3)
    // a1.appended(4), no se puede porque se inmutable, entonces =>
    println(a1)
    val a2 = a1.appended(4)
    println(a2)
    val a3 = a2.:+(5) // el lo mismo a usar la funcion appended(x)
    println(a3)
    // seleccionar un valor de la lista
    println(a3(3))

    // Conjuntos (Set)
    println("Conjuntos")
    val c1 = Set(1, 2, 3)
    println(c1)
    // c1.incl(4). no se puede porque es inmutable, entonces =>
    val c2 = c1.incl(4)
    println(c2)
    val c3 = c1 + 4 // es lo mismo a usar la funcion .incl(x)
    println(c3)
    // seleccionar un valor del conjunto
    println(c3(4)) // no busca la posicion sino que pregunta si el parametro existe en el conjunto

    // Mapas (Map)
    println("Mapas")

    val m1 = Map(1 -> "hola") // o Map((1, "hola"))
    println(m1)

    val m2 = m1 + (2 -> "hello") // o m1 + ((2, "hello"))
    println(m2)

    // recorrer estas listas, todas se recorren igual a excepcion de los mapas
    println(c2.map(x => x + 1))

    // para los mapas
    println(m2.view.mapValues(s => s + "!")) // no lo muestra a ser el mapeo lazy o peresoso
  }

  /*
  * Crear un grupo de funciones para hallar la media, la mediana y la moda de una lista de numeros,
  * devolviendo la respuesta dentro de un tipo Map
  * */
  private def Reto2(): Unit = {
    println("\nReto 2")
    val lista = Seq(1, 9, 5, 7, 8, 9, 11, 20, 20, 17)

    def media(l: Seq[Int]): Float = {
      var sum = 0
      l.foreach(x => sum = sum + x)
      sum / l.length
    }

    def mediana(l: Seq[Int]): Int = {
      val l2 = l.sorted
      val pos = l.length / 2
      l2(pos)
    }

    def moda(l: Seq[Int]):Int = {
      val l2 = l.groupBy(x => if (x == x) x)
      var long = 0
      l2.foreach(x => {
        if (x._2.length > long) long = x._2.length
      })
      l2.filter(x => x._2.length == long).head._2.head
    }

    println("Media: " + media(lista) )
    println("Mediana: " + mediana(lista) )
    println("Moda: " + moda(lista) )
  }

  // Tuplas y Objetos
  private def TuplasObjetos(): Unit = {
    println("\nTuplas y Objetos")
    // Tuplas
    println("Tuplas")
    val tupla = (1, "Camilo", false)
    println("Int: " + tupla._1 + " \nString: " + tupla._2 + " \nBoolean: " + tupla._3) // acceder a un atributo de la tupla


    // Objetos
    println("Objetos")
    case class Persona(id:Int, nombre: String, activo: Boolean)
    val p = Persona(1, "Camilo", true)
    println("Id: " + p.id + " \nNombre: " + p.nombre + " \nActivo: " + p.activo) // acceder a un atributo de un objeto

    println( Persona.unapply(p) ) // retorna una tupla a partir de un objeto

    // se agrega una persona en base a una tupla
    val tupla2 = (2, "Johana", false)
    println( Persona.tupled(tupla2) )

  }

  // Pattern Matching
  private def PatternMatching(): Unit = {
    println("\nPattern Matching")

    println(
      "hola" match {
        case "Mundo" => "oo!"
        case "hola" => "aa!"
      }
    )

    def g(x: Seq[Int]) = x match {
      case List(a, b, c) => a + b + c
      case List(a, b, c, d) => a + b + c + d
      case _ => 0
    }

    println(g(Seq(1,1, 1, 3, 1)))

    case class Persona(nombre: String, edad: Int)
    val p1 = Persona("Maria", 20)
    val p2 = Persona("Fredy", 15)

    def h(x: Persona) = x match {
      case y if y.edad >= 18 => "Mayor de edad"
      case _ => "No es mayor de edad"
    }

    println(h(p1))
    println(h(p2))
  }

  /*
  * Mejorar el ultimo match, para responder diferente si alguien se llama Maria y es mayor de edad,
  * cuando se llama Maria y es menor de edad, y los otros casos
  * */
  private def Reto3(): Unit = {
    println("\nReto3")

    case class Persona(nombre: String, edad: Int)
    val p1 = Persona("Maria", 20)
    val p2 = Persona("Maria", 15)
    val p3 = Persona("Fredy", 15)
    val p4 = Persona("Julian", 30)



    def h(x: Persona) = x match {
      case y if y.edad >= 18 && y.nombre == "Maria" => "Maria es mayor de edad"
      case y if y.edad <= 18 && y.nombre == "Maria" => "Maria es menor de edad"
      case z if z.edad >= 18 => z.nombre + " es mayor de edad"
      case p => p.nombre + " es menor de edad"
    }

    println(h(p1))
    println(h(p2))
    println(h(p3))
    println(h(p4))
  }

  // Recursion
  private def Recursion(): Unit = {
    println("\nRecursion")

    def factorial(n: Long, resultado: Long = 1L): Long = {
        if (n == 0) {
          println("Termino")
          resultado
        } else {
          println(s"Va calculando ${n}, resultado: ${resultado}")
          factorial(n - 1, n * resultado)
        }
    }

    println(factorial(3))
  }

  // Agregacion
  private def Agregacion(): Unit = {
    println("\nAgregacion")

    // Acumuladores
    // En este ejemplo se esta haciendo una funcion factorial como lo que se hizo arriba,
    // pero con la ayuda de los acumuladores ya disponibles para lisa dentro de scala
    val resultado = (1 to 3).foldLeft(1L)((r, n) => r*n)
    println(resultado)

  }

  // Razonamientos con tipos
  private def RazonamientoConTipos(): Unit = {
    println("\nRazonamientos con tipos")

    type Personaid = Int
    case class Persona(id: Personaid, nombre: String)
    type Estudiante = Persona

    println( new Estudiante(1, "Camilo") )
  }

  // Tipos genericos
  private def TiposGenericos(): Unit = {
    println("\nTipos genericos")

    // Dependiendo del uso que se le de el compilador interpreta de que tipo es
    def f[A](x: A): String = s"$x"
    println( f(23) )
    println( f("Hola") )

    // Uso de un trait
    trait ejemplo[A, B] {
      def g(x: A, f: A => B): B = f(x)
    }

    object genericos extends ejemplo[Int, String]
    println( genericos.g(3, f) )
  }

  // Datos algebraicos
  private def DatosAlgebraicos(): Unit = {
    println("\nTipos genericos")

    sealed trait Persona
    case class Estudiante(nombre: String) extends Persona
    case class Profesor(nombre: String, profesion: String) extends Persona

    val me: Persona = Profesor("Camilo", "Ingeniero de sistemas")
    println(me)

    println(
      me match {
        case Profesor(nombre, profesion) => s"$nombre, es $profesion"
        case Estudiante(nombre) => s"$nombre, es estudiante"
      }
    )
  }

  // Evaluacion Peresoza
  private def EvaluacionPeresoza(): Unit = {
    println("\nEvaluacion Peresoza")

    // Lazy collection
    lazy val y = x - 1
    lazy val x = 10

    println(y)

    val a = LazyList(1, 2, 3)
    println(a) // LazyList(<not computed>) porque se van agregando a medida que los necesitemos
  }

  // Disyunciones
  private def Disyunciones(): Unit = {
    println("\nDisyunciones")

    // Option
    println("Option")
    val a = Option(2)
    println(a)
    val b = Option(null)
    println(b)
    val c = Some(null)
    println(c)

    val lista = List(1, 2, 3)
    println( lista.find(x => x == 3) )
    println( lista.find(x => x == 4) )
    println( lista.find(x => x == 3)map(y => y + 1) )
    println( lista.find(x => x == 3)map(_ + 1) ) // lo mismo que la linea de arriba

    val resultado = lista.find(x => x == 3).flatMap(x => lista.headOption.map(y => x + y))
    println(resultado)

    println(resultado.getOrElse(0))

    // Try
    val t = Try(Nil.head)
    println(t) // genera un Failure con el mensaje de excepcion

    // Either
    println(Right(10).map(_ + 1))
    println(Left[Int, Int](10).map(_ + 1)) // como del lado izquierdo no lo encuentra, no hace nada
  }

}
*/

object Main extends App {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "mainSystem")

  private val httpHost = Utils.getStringProperty("http.host")
  private val httpPort = Utils.getIntProperty("http.port")

  private val bindingFuture = Http()
    .newServerAt(httpHost, httpPort)
    .bind(WeatherController.route)

  println("")
  println("Para consumir el api utilice la URL: ")
  println("http://" + httpHost + ":" + httpPort + "/weather")
  println("Presione ENTER para finalizar...")
  println("")
  StdIn.readLine()

  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
