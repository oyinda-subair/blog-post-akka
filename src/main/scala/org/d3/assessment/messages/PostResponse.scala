package org.d3.assessment.messages

import play.api.libs.json.{Format, Json}

case class UserResponse(name: String, email: String)
object UserResponse {
  implicit val format: Format[UserResponse] = Json.format
}


case class PostResponse(title: String, content: String, author: Option[UserResponse] = None, comments: Option[Seq[PostCommentResponse]] = None)
object PostResponse {
  implicit val format: Format[PostResponse] = Json.format
}


case class PostCommentResponse(comment: String, author: Option[String])
object PostCommentResponse {
  implicit val format: Format[PostCommentResponse] = Json.format
}
