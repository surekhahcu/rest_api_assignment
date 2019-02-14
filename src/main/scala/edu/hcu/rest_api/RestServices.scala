package edu.hcu.rest_api


import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object RestServices extends Logging {


  import JsonUtility._

  val pdfObject = new PdfRepository

  val route: Route =
    path("pdf" / "save") {
      post {
        entity(as[String]) {
          pdfJson =>
            complete {
              info("Request Json " + pdfJson)
              val pdf = parse(pdfJson).extract[FileData]
              pdfObject.create(pdf)
              "Pdf created"
            }
        }
      }
    } ~
      path("pdf" / "update") {
        post {
          entity(as[String]) {
            pdfJson =>
              complete {
                info("Request Json " + pdfJson)
                val pdf = parse(pdfJson).extract[FileData]
                pdfObject.update(pdf)
                "Pdf updated"
              }
          }
        }
      } ~
      path("pdf" / "delete") {
        post {
          parameters('id.as[Int]) { id =>
            complete {
              pdfObject.delete(id)
              "Pdf Deleted"
            }
          }
        }
      } ~
      path("pdf" / "getById") {
        get {
          parameters('id.as[Int]) { id =>
            complete {
              info("Request id " + id)
              val pdfFuture: Future[Option[FileData]] = pdfObject.getById(id)
              pdfFuture.map { pdfData =>
                pdfData match {
                  case Some(pdf) => write(pdf)
                  case None => s"Pdf does not exist[id: $id]"
                }
              }
            }

          }
        }
      } ~
      path("pdf" / "getAll") {
        get {
          complete {
            val pdfsFuture: Future[List[FileData]] = pdfObject.getAll()
            pdfsFuture.map { pdfData => write(pdfData)
            }
          }

        }
      }

}