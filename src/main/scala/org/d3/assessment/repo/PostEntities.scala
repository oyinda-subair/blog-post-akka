package org.d3.assessment.repo

import org.d3.assessment.database.DB
import org.d3.assessment.model.PostEntity

import scala.concurrent.{ExecutionContext, Future}

trait PostEntities extends UserEntities { this: DB =>
  import driver.api._

  import scala.concurrent.ExecutionContext.Implicits.global
  val userRepo = new UserRepository
  val Users = userRepo.Users

  class PostTable(tag: Tag) extends Table[PostEntity](tag, "post_by_id"){
    def id: Rep[Int] = column[Int]("post_id", O.PrimaryKey, O.AutoInc)
    def title: Rep[String] = column[String]("title")
    val index1 = index("idx_title", title)
    def content: Rep[String] = column[String]("content")
    def userId: Rep[Int] = column[Int]("user_id")

    def * = (id, title, content, userId) <> ((PostEntity.apply _).tupled, PostEntity.unapply)

    def users = foreignKey("user_by_id_fk", userId, Users)(_.id, onDelete = ForeignKeyAction.Cascade)
  }

  class PostRepository(implicit ex: ExecutionContext) {
    val Posts = TableQuery[PostTable]

    def create(title: String, content: String, userId: Int): Future[PostEntity] = db.run {
      (Posts.map(u ⇒ (u.title, u.content, u.userId))
        returning Posts.map(_.id)
        into ((idPost, postId) ⇒ PostEntity(postId, idPost._1, idPost._2, idPost._3))
        ) += (title, content, userId)
    }

    def getAllPosts: Future[Seq[PostEntity]] =  db.run (Posts.result)

    def getPostsByUserId(userId: Int): Future[Seq[PostEntity]] = db.run {
      Posts.filter(_.userId === userId).result
    }
  }
}
