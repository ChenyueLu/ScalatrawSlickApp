package com.example.app

import com.example.models.UserDetails
import com.example.models.slick.User
import org.scalatest.FlatSpec
import com.example.services.UserStore


class UserStoreUnitTest extends FlatSpec {

  var userId = 0

  val testUserData = TestUserData("Test", 50)

  "UserStore" should "Add new user in DB and return UserDetail Object" in {
    assert(UserStore.addUser(testUserData.name, testUserData.age).get.get match{
      case UserDetails(id, "Test", 50) => {userId = id; true}
      case _ => false
    })
  }

  it should "Get User by Id and return User Object" in {
    assert(UserStore.getUserById(userId).get.get match{
      case User(userId, "Test", 50) => true
      case _ => false
    })
  }

  it should "Get User by Name and return User Object" in {
    assert(UserStore.getUserByName("Test").get.get match{
      case User(userId, "Test", 50) => true
      case _ => false
    })
  }

  it should "Update User by Id and return Boolean true" in {
    assert(UserStore.updateUser(userId, "Test1", 45).get)
  }

  it should "Delete User by Name and return Boolean true" in {
    assert(UserStore.deleteUserByName("Test1").get)
  }

}

case class TestUserData(name: String, age: Int)