package edu.hcu.rest_api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.http.scaladsl.unmarshalling.Unmarshaller._
import org.mockito.Mockito._
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Future

class RestServicesTest extends WordSpec with Matchers with ScalatestRouteTest with MockitoSugar {

  val mockedPdfRepository = mock[PdfRepository]

  val restService = new RestServices {
    override val pdfObject: PdfRepository = mockedPdfRepository
  }


  "Rest service " should {

    "save record to pdf repository " in {
      val requestJson = """{"data":"This is test case data"}"""
      when(mockedPdfRepository.create(FileData("This is test case data"))) thenReturn (Future(1))
      Post("/pdf/save", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> restService.route ~> check {
        responseAs[String] shouldEqual "Pdf created"
      }
    }

    "update record of pdf " in {
      val requestJson = """{"data":"This content is for update test case","id": 1}"""
      when(mockedPdfRepository.update(FileData("This content is for update test case", Some(1)))) thenReturn (Future(1))
      Post("/pdf/update", HttpEntity(ContentTypes.`application/json`, requestJson)) ~> restService.route ~> check {
        responseAs[String] shouldEqual "Pdf updated"
      }
    }

    "delete the record of given id" in {
      when(mockedPdfRepository.delete(1)) thenReturn (Future(1))
      Post("/pdf/delete?id=1") ~> restService.route ~> check {
        responseAs[String] shouldEqual "Pdf Deleted"
      }
    }


    "Get the content of given id's pdf" in {
      when(mockedPdfRepository.getById(1)) thenReturn Future(Some(FileData("This pdf content will always be available in rest Services test cases", Some(1))))
      Get("/pdf/getById?id=1") ~> restService.route ~> check {
        responseAs[String] shouldEqual """{"data":"This pdf content will always be available in rest Services test cases","id":1}"""
      }
    }

    "Get All The Pdfs" in {
      when(mockedPdfRepository.getAll()) thenReturn Future(List(FileData("This pdf content will always be available in rest Services test cases", Some(1)),
        FileData("This pdf content is for the testCase of method getAll", Some(2))))

      Get("/pdf/getAll") ~> restService.route ~> check {
        responseAs[String] shouldEqual
          """[{"data":"This pdf content will always be available in rest Services test cases","id":1},{"data":"This pdf content is for the testCase of method getAll","id":2}]"""
      }
    }


  }

}
