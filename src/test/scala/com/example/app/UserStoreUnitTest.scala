package com.example.app

import org.scalatest.FlatSpec
import com.example.services.UserStore

import scala.util.{Failure, Success}


class UserStoreUnitTest extends FlatSpec {

  var userId = 0

  val testUserData = TestUserData("Test", 50)

  "UserStore" should "Add new user in DB and return UserDetail Object" in {
    val isAddSucced = UserStore.addUser(testUserData.name, testUserData.age) match{
      case Success(userDetailsOpt) =>
        userDetailsOpt match{
          case Some(userDetails) => {userId = userDetails.id; true}
          case _ => false
        }
      case Failure(_) => false
    }
    assert(isAddSucced)
  }

  it should "Get User by Id and return User Object" in {
    val isUserAvaliable = UserStore.getUserById(userId) match{
      case Success(userOpt) =>
        userOpt match{
          case Some(user) => {
            if(user.id.equals(userId) && user.name.equals("Test") && user.age.equals(50)) true
            else false
          }
          case _ => false
        }
      case Failure(_) => false
    }
    assert(isUserAvaliable)
  }

  it should "Get User by Name and return User Object" in {
    val isUserAvailable = UserStore.getUserByName("Test") match {
      case Success(userOpt) =>
        userOpt match {
          case Some(user) =>
              if (user.name.equals("Test") && user.age == 50) {
                true
              } else {
                false
              }
          case None => false
        }
      case Failure(_) => false
    }
    assert(isUserAvailable)
  }

  it should "Update User by Id and return Boolean true" in {
    val isUpdateAvaliable = UserStore.updateUser(userId, "Test1", 45) match{
      case Success(bool) => bool
      case Failure(_) => false
      }
    assert(isUpdateAvaliable)
  }

  it should "Delete User by Name and return Boolean true" in {
    val isDeleteAvaliable = UserStore.deleteUserByName("Test1") match{
      case Success(bool) => bool
      case Failure(_) => false
    }
    assert(isDeleteAvaliable)
  }

}

case class TestUserData(name: String, age: Int)