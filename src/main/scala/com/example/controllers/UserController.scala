package com.example.controllers

import org.scalatra._

import scala.concurrent._
import org.slf4j.LoggerFactory
import org.json4s._
import com.example.models.{UserData, UserDetails}
import com.example.services.UserStore
import com.example.commons.Constants._
import com.example.models.slick.User
import org.json4s.{DefaultFormats, Formats}
import org.scalatra.swagger.{Swagger, SwaggerSupport}

import scala.util._


class UserController(implicit val swagger: Swagger) extends BaseController with SwaggerSupport {

  val logger = LoggerFactory.getLogger(getClass)

  protected val applicationDescription = "User Apis"

  protected implicit val jsonFormats: Formats = DefaultFormats

  protected implicit def executor: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

  val getAllUsers = (
    apiOperation[List[UserDetails]]("getAllUsers")
    summary "Show All Users"
  )
  get("/", operation(getAllUsers)) {
    UserStore.getAllUsers
  }

  val getUserById = (
    apiOperation[Option[User]]("getUserById")
    summary "Show User By Id"
    parameter pathParam[Int]("userId")
  )
  get("/byId/:userId", operation(getUserById)) {
    params.getAs[Int]("userId") match{
      case Some(userId) => UserStore.getUserById(userId) match{
        case Success(result) => result
        case Failure(ex) => BadRequest(Map(MESSAGE_KEY -> ex.getMessage))
      }
      case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))}
  }

  val getUserByName = (
    apiOperation[Option[User]]("getUserByName")
    summary "Show User By Name"
    parameter pathParam[String]("userName")
  )
  get("/byName/:userName", operation(getUserByName)) {
    params.getAs[String]("userName") match{
      case Some(userName) => UserStore.getUserByName(userName) match{
        case Success(result) => result
        case Failure(ex) => BadRequest(Map(MESSAGE_KEY -> ex.getMessage))
      }
      case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))
    }
  }

  val addUser = (
    apiOperation[Option[UserDetails]]("addUser")
    summary "Add User"
    parameter bodyParam[UserData]
  )
  post("/", operation(addUser)) {

    parsedBody.extractOpt[UserData] match{
      case Some(userData) => UserStore.addUser(userData.name, userData.age.getOrElse[Int](0)) match{
        case Success(result) => result
        case Failure(ex) => BadRequest(Map(MESSAGE_KEY -> ex.getMessage))
      }
      case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_POST_DATA_MISSING))
    }
  }

  val updateUser = (
    apiOperation[Boolean]("updateUser")
    summary "Update User"
    parameter pathParam[Int]("userId")
  )
  put("/:userId", operation(updateUser)) {

    params.getAs[Int]("userId") match {

      case Some(userId) => {
        parsedBody.extractOpt[UserData] match {

          case Some(userData) => UserStore.updateUser(userId, userData.name, userData.age.getOrElse(0)) match {
            case Success(result) => result match{
              case true => Ok(Map(MESSAGE_KEY -> UPDATE_SUCCEED))
              case false => Conflict(Map(MESSAGE_KEY -> UPDATE_FAILED))
            }
            case Failure(ex) => BadRequest(Map(MESSAGE_KEY -> ex.getMessage))
          }

          case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_POST_DATA_MISSING))
        }
      }

      case None => Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING)
    }
  }

  val deleteUserById = (
    apiOperation[Boolean]("updateUser")
    summary "Delete User By Id"
    parameter pathParam[Int]("userId")
  )
  delete("/byId/:userId", operation(deleteUserById)) {

    params.getAs[Int]("userId") match {

      case Some(id) => {

        UserStore.deleteUser(id) match {
          case Success(result) => result match{
            case true => Ok(Map(MESSAGE_KEY -> DELETE_SUCCEED))
            case false => Conflict(Map(MESSAGE_KEY -> DELETE_FAILED))
          }
          case Failure(ex) => BadRequest(Map(MESSAGE_KEY -> ex.getMessage))
        }
      }

      case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))
    }
  }

  val deleteUserByName = (
    apiOperation[Boolean]("updateUser")
      summary "Delete User By Id"
      parameter pathParam[String]("userName")
    )
  delete("/byName/:userName", operation(deleteUserByName)) {
    params.getAs[String]("userName") match {

      case Some(name) => {

        UserStore.deleteUserByName(name) match {
          case Success(result) => result match{
            case true => Ok(Map(MESSAGE_KEY -> DELETE_SUCCEED))
            case false => Conflict(Map(MESSAGE_KEY -> DELETE_FAILED))
          }
          case Failure(ex) => BadRequest(Map(MESSAGE_KEY -> ex.getMessage))
        }
      }

      case None => BadRequest(Map(MESSAGE_KEY -> MANDATORY_PARAM_IS_MISSING))

    }
  }
}

