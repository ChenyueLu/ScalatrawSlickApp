package com.example.services

import com.example.models.slick.User
import com.example.models.UserDetails


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
    User.get(Some(id), None)
  }

  def getUserByName(name: String): Option[User] = {
    User.get(None, Some(name))
  }

  def getAllUsers: List[UserDetails] = {
    User.all() map{
      ana => UserDetails(ana.id.get, ana.name, ana.age)
    }
  }

  def deleteUser(id: Int): Boolean = {
    User.delete(Some(id), None)
  }

  def deleteUserByName(name: String): Boolean = {
    User.delete(None, Some(name))
  }

  def updateUser(id: Int, name: String, age: Int): Boolean = {
    User.update(id, name, age)
  }
}
