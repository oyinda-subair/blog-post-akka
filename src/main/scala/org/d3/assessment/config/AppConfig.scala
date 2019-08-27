package org.d3.assessment.config

import com.typesafe.config.ConfigFactory
import org.mindrot.jbcrypt.BCrypt

trait AppConfig {

  val config = ConfigFactory.load()

  val pgDriver: String     = config.getString("slick-postgres.db.driver")
  val pgUrl: String = config.getString("slick-postgres.db.url")
  val pgPassword: String = config.getString("slick-postgres.db.password")
  val pgUser: String = config.getString("slick-postgres.db.user")
  val pgHost: String = config.getString("slick-postgres.db.host")
  val pgDBName: String = config.getString("slick-postgres.db.name")

  def hashPassword(password: String): String = {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }

  def confirmPassword(password: String, hashPassword: String): Boolean = {
    BCrypt.checkpw(password, hashPassword)
  }
}
