package org.d3.assessment.config

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

trait AppConfig {

  lazy val className: String = if(this.getClass.getCanonicalName != null)
    this.getClass.getCanonicalName else "none"

  val config = ConfigFactory.load()

  val pgDriver: String     = config.getString("slick-postgres.db.driver")
  val pgUrl: String = config.getString("slick-postgres.db.url")
  val pgPassword: String = config.getString("slick-postgres.db.password")
  val pgUser: String = config.getString("slick-postgres.db.user")
  val pgHost: String = config.getString("slick-postgres.db.host")
  val pgDBName: String = config.getString("slick-postgres.db.name")

  val secret: String = System.getenv("JWT_SECRET")

  val logger = LoggerFactory.getLogger(className)
}
