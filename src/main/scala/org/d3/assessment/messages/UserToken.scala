package org.d3.assessment.messages

import play.api.libs.json.{Format, Json}

case class UserToken(token: String)

object UserToken {
  implicit val format: Format[UserToken] = Json.format[UserToken]
}