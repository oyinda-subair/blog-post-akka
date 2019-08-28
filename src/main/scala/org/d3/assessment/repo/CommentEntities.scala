package org.d3.assessment.repo

import org.d3.assessment.database.DB
import org.d3.assessment.model.CommentEntity

import scala.concurrent.{ExecutionContext, Future}

trait CommentEntities extends PostEntities { this: DB =>
  import driver.api._

  import scala.concurrent.ExecutionContext.Implicits.global
  val postRepo =  new PostRepository
  val Posts = postRepo.Posts

  class CommentTable(tag: Tag) extends Table[CommentEntity](tag, "comment_by_id"){
    def id: Rep[Int] = column[Int]("comment_id", O.PrimaryKey, O.AutoInc)
    def comment: Rep[String] = column[String]("comment")
    def postId: Rep[Int] = column[Int]("post_id")
    def userId: Rep[Int] = column[Int]("user_id")

    def * = (id, comment, postId, userId) <> ((CommentEntity.apply _).tupled, CommentEntity.unapply)

    def users = foreignKey("user_by_id_fk", userId, Users)(_.id, onDelete = ForeignKeyAction.Cascade)
    def posts = foreignKey("post_by_id_fk", postId, Posts)(_.id, onDelete = ForeignKeyAction.Cascade)

  }

  class CommentRepository(implicit ex: ExecutionContext) {
    val Comments = TableQuery[CommentTable]

    def create(comment: String, userId: Int, postId: Int): Future[CommentEntity] = db.run {
      (Comments.map(c ⇒ (c.comment, c.postId, c.userId))
        returning Comments.map(_.id)
        into ((idComment, commentId) ⇒ CommentEntity(commentId, idComment._1, idComment._2, idComment._3))
        ) += (comment, postId, userId)
    }

    def getAllComments: Future[Seq[CommentEntity]] =  db.run (Comments.result)

    def getCommentsByPostId(postId: Int): Future[Seq[CommentEntity]] = db.run {
      Comments.filter(_.postId === postId).result
    }
  }
}
