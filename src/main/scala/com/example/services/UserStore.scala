package com.example.services

import com.example.models.slick.User
import com.example.models.UserDetails

import scala.concurrent.Future
import scala.util.Try


object UserStore {

  def createUserTable(): Try[Unit] = {
    Try(User.creatTable())
  }

  def initUserTable(): Try[Unit] = {
    Try(User.initTable())
  }

  def addUser(name: String, age: Int): Try[Option[UserDetails]] = {
    Try({
      User.insert(name, age) map {
        ana => UserDetails(ana.id.get, ana.name, ana.age)
      }
    })
  }

  def getUserById(id: Int): Try[Option[User]] = {
    Try({
      User.get(Some(id), None)
    })
  }

  def getUserByName(name: String): Try[Option[User]] = {
    Try(User.get(None, Some(name)))
  }

  def getAllUsers: Try[List[UserDetails]] = {
    Try(User.all() map{
      ana => UserDetails(ana.id.get, ana.name, ana.age)
    })
  }

  def deleteUser(id: Int): Try[Boolean] = {
    Try(User.delete(Some(id), None))
  }

  def deleteUserByName(name: String): Try[Boolean] = {
    Try(User.delete(None, Some(name)))
  }

  def updateUser(id: Int, name: String, age: Int): Try[Boolean] = {
    Try(User.update(id, name, age))
  }
}
