package edu.hcu.rest_api

import slick.driver.MySQLDriver.api._

object Connection {

  def db = {
    Database.forURL("jdbc:mysql://localhost:3306/regex_assignment",
      driver = "com.mysql.jdbc.Driver",
      user = "root", password = "root")
  }

}
