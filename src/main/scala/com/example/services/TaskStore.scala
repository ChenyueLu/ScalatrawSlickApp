package com.example.services

import com.example.models.slick.Task
import com.example.models.TaskDetails

object TaskStore {

  def createTaskTable(): Unit = {
    Task.creatTable()
  }

  def initTaskTable(): Unit = {
    Task.initTable()
  }

  def addTask(name: String, action: String): Option[TaskDetails] = {
    Task.insert(name, action) map{
      ana => TaskDetails(ana.id.get, ana.name, ana.action)
    }
  }

  def getTaskById(id: Int): Option[TaskDetails] = {
    Task.get(id) map{
      ana => TaskDetails(ana.id.get, ana.name, ana.action)
    }
  }

  def getTaskByName(name: String): Option[TaskDetails] = {
    Task.getByName(name) map{
      ana => TaskDetails(ana.id.get, ana.name, ana.action)
    }
  }

  def getAllTasks(): List[TaskDetails] = {
    Task.all() map{
      ana => TaskDetails(ana.id.get, ana.name, ana.action)
    }
  }

  def deleteTask(id: Int): Int = {
    Task.delete(id)
  }

}
