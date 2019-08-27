package org.d3.assessment.messages

import play.api.libs.json._

case class CreateUserRequest(name: String, email: String, password: String)

object CreateUserRequest {
  implicit val format: Format[CreateUserRequest] = Json.format
}
