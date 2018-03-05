package com.example.controllers

import org.json4s.{DefaultFormats, Formats}
import org.scalatra._

import scala.concurrent._
import org.slf4j.LoggerFactory
import org.json4s._
import com.example.models.UserData
import com.example.services.UserStore
import com.example.commons.Constants._


class UserController extends BaseController with FutureSupport {

  val logger = LoggerFactory.getLogger(getClass)

  protected implicit val jsonFormats: Formats = DefaultFormats

  protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  get("/") {
    UserStore.getAllUsers
  }

  get("/byId/:userId") {
    params.getAs[Int]("userId") match{
      case Some(userId) => UserStore.getUserById(userId)
      case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))}
  }


  get("/byName/:userName") {
    params.getAs[String]("userName") match{
      case Some(userName) => UserStore.getUserByName(userName)
      case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))
    }
  }

  post("/") {

    parsedBody.extractOpt[UserData] match{
      case Some(userData) => UserStore.addUser(userData.name, userData.age.getOrElse[Int](0))
      case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_POST_DATA_MISSING))
    }
  }

  put("/:userId") {

    params.getAs[Int]("userId") match {

      case Some(userId) => {
        parsedBody.extractOpt[UserData] match {

          case Some(userData) => UserStore.updateUser(userId, userData.name, userData.age.getOrElse(0)) match {

            case true => Ok(Map(MESSAGE_KEY -> UPDATE_SUCCEED))

            case false => Conflict(Map(MESSAGE_KEY -> UPDATE_FAILED))
          }

          case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_POST_DATA_MISSING))
        }
      }

      case None => Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING)
    }
  }

  delete("/byId/:userId") {

    params.getAs[Int]("userId") match {

      case Some(id) => {

        UserStore.deleteUser(id) match {

          case true => Ok(Map(MESSAGE_KEY -> DELETE_SUCCEED))

          case false => Conflict(Map(MESSAGE_KEY -> DELETE_FAILED))
        }
      }

      case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))
    }
  }

  delete("/byName/:userName") {
    params.getAs[String]("userName") match {

      case Some(name) => {

        UserStore.deleteUserByName(name) match {

          case true => Ok(Map(MESSAGE_KEY -> DELETE_SUCCEED))

          case false => Conflict(Map(MESSAGE_KEY -> DELETE_FAILED))
        }
      }

      case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))

    }
  }
}

