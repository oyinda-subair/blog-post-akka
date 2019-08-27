package org.d3.assessment.messages

import play.api.libs.json.{Format, Json}

case class CreatePostRequest(title: String, content: String)

object CreatePostRequest {
  implicit val format: Format[CreatePostRequest] = Json.format
}
