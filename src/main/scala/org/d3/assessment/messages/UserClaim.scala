package org.d3.assessment.messages

import play.api.libs.json.{Format, Json}

case class UserClaim(userId: Int)

object UserClaim {
  implicit val format: Format[UserClaim] = Json.format[UserClaim]
}
