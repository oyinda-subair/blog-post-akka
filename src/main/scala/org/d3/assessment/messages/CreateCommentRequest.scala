package org.d3.assessment.messages

import play.api.libs.json.{Format, Json}

case class CreateCommentRequest(comment: String)

object CreateCommentRequest {
  implicit val format: Format[CreateCommentRequest] = Json.format
}
