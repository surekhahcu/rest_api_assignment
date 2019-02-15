package edu.hcu.rest_api

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import edu.hcu.rest_api.RestServices.route

object RestApiApp extends App {

  implicit val system: ActorSystem = ActorSystem("RestServiceApp")

  implicit val materializer: ActorMaterializer = ActorMaterializer()


  val port = 8000

  Http().bindAndHandle(route, "localhost", port)

  println(s"Rest service is running on $port")

}


