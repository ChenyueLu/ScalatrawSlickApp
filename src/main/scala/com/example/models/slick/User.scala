package com.example.models.slick

import com.example.models.slick.slickDB._
import org.slf4j.LoggerFactory
import scala.slick.driver.MySQLDriver.simple._

import scala.concurrent.ExecutionContext.Implicits.global


case class User(id: Option[Int], name: String, age: Int=0){

  def toMap = {
    Map("id" -> id, "name" -> name, "age" -> age)
  }

}

class Users(tag: Tag) extends Table[User](tag, "users"){

  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name")
  def age = column[Int]("age")

  def * = (id.?, name, age) <> ((User.apply _).tupled, User.unapply)

}

object User {

  lazy val users = TableQuery[Users]

  val logger = LoggerFactory.getLogger(getClass())

  def creatTable(): Unit = {
    db withSession {implicit session =>
      users.ddl.create
    }
  }

  def initTable(): Unit = {
    db withSession {implicit session =>
      users returning users.map(_.id) insertAll (User(None, "Lucas", 24), User(None, "LRS", 24))
    }
  }

  def all(): List[User] = {
    try{
      db withSession{implicit session =>
        users.list
      }
    }
  }

  def insert(name: String, age: Int): Option[User] = {
    try{
      db withSession{implicit session =>
        users returning users.map(_.id) insert (User(None, name, age)) match{
          case id: Int => Some(User(Some(id), name, age))
        }
      }
    }
  }

  def get(id: Int): Option[User] = {
    try{
      db withSession{implicit session =>
        users.filter(_.id === id).firstOption
      }
    }
  }

  def getByName(name: String): Option[User] = {
    try{
      db withSession{implicit session =>
        users.filter(_.name === name).firstOption
      }
    }
  }

  def delete(id: Int): Boolean = {
    try{
      db withSession{implicit session =>
        users.filter(_.id === id).delete match{
          case 1 => true
          case _ => false
        }
      }
    }
  }

  def deleteByName(name: String): Boolean = {
    try{
      db withSession{implicit session =>
        users.filter(_.name === name).delete match{
          case 1 => true
          case _ => false
        }
      }
    }
  }

  def update(id: Int, name: String, age: Int): Boolean = {
    try{
      db withSession{implicit session =>
        users.filter(_.id === id).map(p => (p.name, p.age)).update((name, age)) match{
          case 1 => true
          case _ => false
        }
      }
    }
  }

  def updateByName(name: String, age: Int): Boolean = {
    try{
      db withSession{implicit session =>
        users.filter(_.name === name).map(_.age).update(age) match{
          case 1 => true
          case _ => false
        }

      }
    }
  }
}

