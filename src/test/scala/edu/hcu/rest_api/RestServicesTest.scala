package edu.hcu.rest_api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.unmarshalling.Unmarshaller._
import org.scalatest.{BeforeAndAfter, Matchers, WordSpec}
import slick.driver.MySQLDriver.api._

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.control.NonFatal


class RestServicesTest extends WordSpec with Matchers with ScalatestRouteTest with BeforeAndAfter {

  import RestServices._


  val pdf = new PdfRepository

  before {
    try {
      Await.result(Connection.db.run(pdf.filesTableQuery.schema.create), 10 seconds)
      pdf.create(FileData("This pdf content will always be available in rest Services test cases", Some(1)))
    } catch {
      case NonFatal(th) =>
        th.printStackTrace()
    }
  }

  after {
    try {
      Await.result(Connection.db.run(pdf.filesTableQuery.schema.drop), 10 seconds)
    } catch {
      case NonFatal(th) =>
        th.printStackTrace()
    }
  }


  "Rest service " should {

    "save record to pdf repository " in {
      val requestJson = """{"data":"This is test case data"}"""
      Post("/pdf/save", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> route ~> check {
        responseAs[String] shouldEqual "Pdf created"
      }
    }

    "update record of pdf " in {
      val requestJson = """{"data":"This content is for update test case","id": 1}"""
      Post("/pdf/update", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> route ~> check {
        responseAs[String] shouldEqual "Pdf updated"
      }
    }

    "delete the record of given id" in {
      Post("/pdf/delete?id=1") ~> route ~> check {
        responseAs[String] shouldEqual "Pdf Deleted"
      }
    }


    "Get the content of given id's pdf" in {
      Get("/pdf/getById?id=1") ~> route ~> check {
        responseAs[String] shouldEqual """{"data":"This pdf content will always be available in rest Services test cases","id":1}"""
      }
    }

    "Get All The Pdfs" in {
      Get("/pdf/getAll") ~> route ~> check {
        responseAs[String] shouldEqual """[{"data":"This pdf content will always be available in rest Services test cases","id":1}]"""
      }
    }

  }
}
