package org.d3.assessment.model

import play.api.libs.json.{Format, Json}

case class CommentEntity(commentId: Int, comment: String, postId: Int, userId: Int)

object CommentEntity {
  implicit val format: Format[CommentEntity] = Json.format[CommentEntity]
}