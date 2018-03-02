package com.example.services

import com.example.models.slick.User
import com.example.models.UserDetails

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object UserStore {

  def createUserTable(): Unit = {
    User.creatTable()
  }

  def initUserTable(): Unit = {
    User.initTable()
  }

  def addUser(name: String, age: Int): Option[UserDetails] = {
    User.insert(name, age) map{
      ana => UserDetails(ana.id.get, ana.name, ana.age)
    }
  }

  def getUserById(id: Int): Option[User] = {
    User.get(id)
  }

  def getUserByName(name: String): Option[UserDetails] = {
    User.getByName(name) map{
      ana => UserDetails(ana.id.get, ana.name, ana.age)
    }
  }

  def getAllUsers: List[UserDetails] = {
    User.all() map{
      ana => UserDetails(ana.id.get, ana.name, ana.age)
    }
  }

  def deleteUser(id: Int): Boolean = {
    User.delete(id)
  }

  def deleteUserByName(name: String): Boolean = {
    User.deleteByName(name)
  }

  def updateUser(id: Int, name: String, age: Int): Boolean = {
    User.update(id, name, age)
  }

  def updateUserByName(name: String, age: Int): Boolean = {
    User.updateByName(name, age)
  }

}
