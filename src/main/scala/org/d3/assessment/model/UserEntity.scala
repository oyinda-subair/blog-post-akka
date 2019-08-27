package org.d3.assessment.model

import play.api.libs.json._

case class UserEntity(userId: Int, name: String, email: String, password: String)

object UserEntity {
  implicit  val format: Format[UserEntity] = Json.format
}
