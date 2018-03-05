package com.example.models

case class UserDetails(id: Int, name: String, age: Int)
case class TaskDetails(id: Int, name: String, action: String)

case class UserData(name: String, age: Option[Int])
{
  def toMap():Map[String, Any]={
    Map("name" -> name, "age" -> age)
  }
}

//object UserData{
//  def fromMap(map:Map[String, Any]):UserData={
//    UserData(Try(map("name").asInstanceOf[String]).getOrElse(""), Try(map("age").asInstanceOf[Option[Int]]).getOrElse(None))
//
//  }
//}


