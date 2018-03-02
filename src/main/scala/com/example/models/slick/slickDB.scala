package com.example.models.slick

import com.mchange.v2.c3p0.ComboPooledDataSource
import org.slf4j.LoggerFactory
import com.example.commons.AppConfig

import slick.driver.{JdbcProfile, MySQLDriver}

object slickDB {

  val logger = LoggerFactory.getLogger(getClass)

  val cpds = new ComboPooledDataSource

  // Get configuration params from configuration file
  val source = getConfString("conf.source")
  val driver = getConfString(s"conf.${source}.driver")
  val jdbcUrl = getConfString(s"conf.${source}.url")
  val dbUser = getConfString(s"conf.${source}.user")
  val dbPass = getConfString(s"conf.${source}.password")


  val profile: JdbcProfile = source match{
    case _ => MySQLDriver
  }

  // Create connection for connection pool
  cpds.setDriverClass(driver)
  cpds.setJdbcUrl(jdbcUrl)
  cpds.setUser(dbUser)
  cpds.setPassword(dbPass)

  cpds.setMinPoolSize(3)
  cpds.setAcquireIncrement(3)
  cpds.setMaxPoolSize(30)
  cpds.setMaxIdleTime(600)
  cpds.setTestConnectionOnCheckout(true)

  import profile.simple._

  val db = Database.forDataSource(cpds)
  logger.info("Created c3p0 connection pool")
  logger.info("Creating tables if not exist")

  private def getConfString(key: String): String = {
    AppConfig.conf.getString(key)
  }

  private def closeDbConnection() {
    logger.info("Closing c3po connection pool")
    cpds.close
  }
}
