package com.example.controllers.swagger

import org.scalatra.ScalatraServlet
import org.scalatra.swagger.{ApiInfo, JacksonSwaggerBase, Swagger}

class ResourcesApp(implicit val swagger: Swagger) extends ScalatraServlet with JacksonSwaggerBase


object UserApiInfo extends ApiInfo(
  "The User API",
  "Docs for the User API",
  "http://tookitaki.org",
  "apiteam@tookitaki.org",
  "MIT",
  "http://opensource.org/licenses/MIT")

class UserSwagger extends Swagger(Swagger.SpecVersion, "1.0.0", UserApiInfo)
