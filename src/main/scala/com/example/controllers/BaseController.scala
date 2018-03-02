package com.example.controllers

import org.scalatra.ScalatraServlet
import org.scalatra.json._


abstract class BaseController extends ScalatraServlet with JacksonJsonSupport {

  before() {
    contentType = formats("json")
  }

}
