package com.example.models.slick

import com.example.models.slick.slickDB._
import org.slf4j.LoggerFactory
import scala.slick.driver.MySQLDriver.simple._

case class Task(id: Option[Int], name: String, action: String){

  def toMap = {
    Map("id" -> id, "name" -> name, "action" -> action)
  }

}

class Tasks(tag: Tag) extends Table[Task](tag, "tasks") {

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def action = column[String]("action")

  def * = (id.?, name, action) <> ((Task.apply _).tupled, Task.unapply)

}

object Task{

  lazy val tasks = TableQuery[Tasks]

  val logger = LoggerFactory.getLogger(getClass())

  def creatTable(): Unit = {
    db withSession {implicit session =>
      tasks.ddl.create
    }
  }

  def initTable(): Unit = {
    db withSession {implicit session =>
      tasks returning tasks.map(_.id) insertAll (Task(None, "Lucas", "Eating"), Task(None, "LRS", "Dreaming"))
    }
  }

  def all(): List[Task] = {
    try{
      db withSession{implicit session =>
        tasks.list
      }
    }
  }

  def insert(name: String, action: String): Option[Task] = {
    try{
      db withSession{implicit session =>
        tasks returning tasks.map(_.id) insert (Task(None, name, action)) match{
          case id: Int => Some(Task(Some(id), name, action))
        }
      }
    }
  }

  def get(id: Int): Option[Task] = {
    try{
      db withSession{implicit session =>
        tasks.filter(_.id === id).firstOption
      }
    }
  }

  def getByName(name: String): Option[Task] = {
    try{
      db withSession{implicit session =>
        tasks.filter(_.name === name).firstOption
      }
    }
  }

  def delete(id: Int): Int = {
    try{
      db withSession{implicit session =>
        tasks.filter(_.id === id).delete
      }
    }
  }

}
