package com.example.models.slick

import com.example.models.slick.SlickDB._
import org.slf4j.LoggerFactory
import profile.simple._

import scala.slick.lifted.SimpleFunction

case class FileLocation(id: Option[Int], inputLocation: String, outputLocation: String)

class FileLocations(tag: Tag) extends Table[FileLocation](tag, "locations"){

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def inputLocation = column[String]("input_location", O.DBType("VARCHAR(1024)"))
  def outputLocation = column[String]("output_location", O.DBType("VARCHAR(1024)"))

  def * = (id.?, inputLocation, outputLocation) <> ((FileLocation.apply _).tupled, FileLocation.unapply)
}

object FileLocation {

  lazy val fileLocations = TableQuery[FileLocations]

  val logger = LoggerFactory.getLogger(getClass)

  def createTable(): Unit = {
    db withSession {implicit session =>
      fileLocations.ddl.create
    }
  }

  def initTable(): Unit = {
    db withSession {implicit session =>
      fileLocations returning fileLocations.map(_.id) insertAll(
        FileLocation(None,
        "s3n://com.tookitaki.preprod.product-11/447/scenario_outputs/test_walmart.csv/2016-03-09T12-05-43",
        "s3n://com.tookitaki.preprod.product-11/.meta/2016-03-09T12-05-43"),
      FileLocation(None,
      "s3n://com.tookitaki.preprod.product-11/498/scenario_outputs/FILTER_Telstra_Test_New_1861/2016-03-29T11-41-54",
      "s3n://com.tookitaki.preprod.product-11/.meta/2016-03-29T11-41-54_1875"))
    }
  }

  def updateLocationPath(prefix: String): Unit = {
    logger.info(s"Update All Location Path to ${prefix}")
    db withSession {implicit session =>
      fileLocations foreach{current => {
        val id = current.id
        val inputLocation = current.inputLocation
        val outputLocation = current.outputLocation

        fileLocations.filter(_.id === id).map(p => (p.inputLocation, p.outputLocation))
          .update((pathChanger(prefix, inputLocation), pathChanger(prefix, outputLocation)))
      }
      }
      logger.info("Update Succeed!")

    }

  }

  def pathChanger(prefix: String, path: String): String = {
          val suffix = path.split("com.tookitaki.preprod.")(1)
          prefix + "com.tookitaki.preprod." + suffix

  }



}
