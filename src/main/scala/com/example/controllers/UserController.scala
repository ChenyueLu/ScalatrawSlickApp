package com.example.controllers

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._

import scala.concurrent._
import org.slf4j.LoggerFactory
import org.json4s._
import com.example.models.UserData
import com.example.services.UserStore
import com.example.commons.JsonResponse
import com.example.commons.Constants._

import scala.util.Try
import scala.util.{ Success, Failure }

class UserController extends BaseController with FutureSupport {

  val logger = LoggerFactory.getLogger(getClass)

  protected implicit val jsonFormats: Formats = DefaultFormats
  protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  get("/users"){
    UserStore.getAllUsers
  }

  get("/users/byId/:userId"){
    val userId: Int = params.get("userId").map(_.toInt).getOrElse(0)

    if(userId > 0) {
      UserStore.getUserById(userId)
    }

    else{
      BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))
    }
  }

  get("/users/byName/:userName"){
    val userName: String = params.get("userName").map(_.toString).getOrElse("")

    if(userName.isEmpty){
      BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))
    }

    else{
      UserStore.getUserByName(userName)
    }

  }

  post("/users"){

    val userDataOpt = parsedBody.extractOpt[UserData]

    userDataOpt.map{userData =>

      UserStore.addUser(userData.name, userData.age)

    }.getOrElse(BadRequest(Map(MESSAGE_KEY -> MANDATORY_POST_DATA_MISSING)))

  }

  put("/users/:userId"){

    val userId = params.get("userId").map(_.toInt).getOrElse(0)

    val userDataOpt = parsedBody.extractOpt[UserData]

    if(userId > 0) {
      userDataOpt.map { userData =>

        UserStore.updateUser(userId, userData.name, userData.age) match{
          case true => Ok(Map(MESSAGE_KEY -> UPDATE_SUCCEED))
          case false => Conflict(Map(MESSAGE_KEY -> UPDATE_FAILED))
        }

      }.getOrElse(BadRequest(Map(MESSAGE_KEY -> MANDATORY_POST_DATA_MISSING)))

    }
    else
      BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))
  }

  delete("/users/byId/:userId"){

    val userId = params.get("userId").map(_.toInt).getOrElse(0)

    if(userId > 0){
      UserStore.deleteUser(userId) match{
        case true => Ok(Map(MESSAGE_KEY -> DELETE_SUCCEED))
        case false => Conflict(Map(MESSAGE_KEY -> DELETE_FAILED))
      }
    }

    else
      BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))

  }

  delete("/users/byName/:userName"){
    val userName = params.get("userName").map(_.toString).getOrElse("")

    if(!userName.isEmpty){
      UserStore.deleteUserByName(userName) match{
        case true => Ok(Map(MESSAGE_KEY -> DELETE_SUCCEED))
        case false => Conflict(Map(MESSAGE_KEY -> DELETE_FAILED))
      }
    }

    else
      BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))
  }
}
