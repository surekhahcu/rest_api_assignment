package edu.hcu.rest_api


import slick.driver.MySQLDriver.api._

import scala.concurrent.Future


class PdfRepository {


  def create(files: FileData): Future[Int] =
    Connection.db.run {
      (filesTableAutoInc += files)

    }

  def delete(id: Int): Future[Int] = Connection.db.run {
    filesTableQuery.filter(_.id === id).delete
  }

  def update(fileData: FileData): Future[Int] = Connection.db.run {
    filesTableQuery.filter(_.id === fileData.id.get).update(fileData)
  }

  def getById(id: Int): Future[Option[FileData]] =
    Connection.db.run {
    filesTableQuery.filter(_.id === id).result.headOption
  }

  def getAll(): Future[List[FileData]] = Connection.db.run {
    filesTableQuery.to[List].result
  }


  class Files(tag: Tag) extends Table[FileData](tag, "FileData") {
    def data = column[String]("data")

    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def * = (data, id.?) <> (FileData.tupled, FileData.unapply)
  }

  val filesTableQuery = TableQuery[Files]

  def filesTableAutoInc = filesTableQuery returning filesTableQuery.map(_.id)


}

case class FileData(data: String, id: Option[Int] = None)


