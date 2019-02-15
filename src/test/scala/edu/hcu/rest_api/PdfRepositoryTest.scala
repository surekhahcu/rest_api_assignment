package edu.hcu.rest_api


import org.scalatest.{BeforeAndAfter, FunSuite}
import slick.driver.MySQLDriver.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.control.NonFatal


class PdfRepositoryTest extends FunSuite with BeforeAndAfter {

  val pdf = new PdfRepository


  before {
    try {
      Await.result(Connection.db.run(pdf.filesTableQuery.schema.create), 10 seconds)
      pdf.create(FileData("This pdf content will always be available in all the test cases", Some(1)))
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

  test("Save pdf data") {
    val resultFuture: Future[Int] = pdf.create(FileData("This content is for creating pdf test case"))
    val result: Int = Await.result(resultFuture, 10 seconds)
    assert(result === 2)
  }


  test("Update Pdf") {
    val resultFuture: Future[Int] = pdf.update(FileData("This content is for updating the existing pdf data", Some(1)))
    val result: Int = Await.result(resultFuture, 10 seconds)
    assert(result === 1)
  }

  test("Delete Pdf") {
    val resultFuture: Future[Int] = pdf.delete(1)
    val result: Int = Await.result(resultFuture, 10 seconds)
    assert(result === 1)

  }

  test(" Get Pdf By Id") {
    val resultFuture: Future[Option[FileData]] = pdf.getById(1)
    val result: Option[FileData] = Await.result(resultFuture, 10 seconds)
    assert(result === Some(FileData("This pdf content will always be available in all the test cases", Some(1))))
  }

  test("Get All The Pdfs") {
    val resultFuture: Future[List[FileData]] = pdf.getAll()
    val result = Await.result(resultFuture, 10 seconds)
    assert(result === List(FileData("This pdf content will always be available in all the test cases", Some(1))))
  }


}
