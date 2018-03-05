package com.example.models.slick

import com.example.models.slick.slickDB._
import org.slf4j.LoggerFactory
import scala.slick.driver.MySQLDriver.simple._

case class User(id: Option[Int], name: String, age: Int){

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
    db withSession { implicit session =>
      users.ddl.create
    }
  }

  def initTable(): Unit = {
    db withSession { implicit session =>
      users returning users.map(_.id) insertAll(User(None, "Lucas", 24), User(None, "LRS", 24))
    }
  }

  def all(): List[User] = {
    try {
      db withSession { implicit session =>
        users.list
      }
    }
  }

  def insert(name: String, age: Int): Option[User] = {
    try {
      db withSession { implicit session =>
        users returning users.map(_.id) insert (User(None, name, age)) match {
          case id: Int => Some(User(Some(id), name, age))
        }
      }
    }
  }

  def get(id: Option[Int], name: Option[String]): Option[User] = {
    try {
      (id, name) match {

        case (Some(user_id), _) => {
          db withSession { implicit session =>
            users.filter(_.id === user_id).firstOption
          }
        }

        case (_, Some(user_name)) => {
          db withSession { implicit session =>
            users.filter(_.name === user_name).firstOption
          }
        }

        case(_, _) => None
      }
    }
  }

  def delete(id: Option[Int], name: Option[String]): Boolean = {
    try {
      (id, name) match {
        case (Some(user_id), _) => {
          db withSession { implicit session =>
            users.filter(_.id === user_id).delete match {
              case 1 => true
              case _ => false
            }
          }
        }

        case (_, Some(user_name)) => {
          db withSession { implicit session =>
            users.filter(_.name === user_name).delete match {
              case 1 => true
              case _ => false
            }
          }
        }

        case (_, _) => false
      }
    }
  }

  def update(id: Int, name: String, age: Int): Boolean = {
    try {
      db withSession { implicit session =>
        users.filter(_.id === id).map(p => (p.name, p.age)).update((name, age)) match {
          case 1 => true
          case _ => false
        }
      }
    }
  }
}


