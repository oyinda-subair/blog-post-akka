package org.d3.assessment.database

import org.d3.assessment.config.AppConfig
import slick.jdbc.JdbcProfile
//import slick.driver.PostgresDriver.api._

trait DB extends AppConfig {
  val driver: JdbcProfile = slick.jdbc.PostgresProfile

  import driver.api._

  def db = Database.forURL(
    url    = s"jdbc:postgresql://$pgHost/$pgDBName",
    driver = pgDriver
  )

  implicit val session: Session = db.createSession()
}
