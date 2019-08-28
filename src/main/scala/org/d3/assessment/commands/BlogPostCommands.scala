package org.d3.assessment.commands

import akka.actor.ActorSystem
import org.d3.assessment.config.AppConfig
import org.d3.assessment.messages._
import org.d3.assessment.model._
import org.d3.assessment.repo._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class BlogPostCommands(
                        userRepo: UserEntities#UserRepository,
                        postRepo: PostEntities#PostRepository,
                        commentRepo: CommentEntities#CommentRepository)(implicit val system: ActorSystem) extends AppConfig{

  // User commands
  def createUser(request: CreateUserRequest): Future[UserEntity] = {
    for {
      entity <- userRepo.create(request.copy(password = hashPassword(request.password)))
    } yield entity
  }

  def getAllUsers: Future[Seq[UserResponse]] = {
    for {
      users <- userRepo.getAllUsers
    } yield users.map(user => UserResponse(user.name, user.email))
  }

  def getuserByEmail(email: String): Future[Option[UserEntity]] = {
    for {
      user <- userRepo.getUserByEmail(email)
    } yield user
  }

  // Post Commands
  def createPost(request: CreatePostRequest, userId: Int): Future[PostEntity] = {
    for{
      postEntity <- postRepo.create(request.title, request.content, userId)
    } yield postEntity
  }

  def getAllPosts: Future[Seq[PostResponse]] = {
    val response = for {
      posts <- postRepo.getAllPosts
      postResponse = Future.sequence {
        posts.map { post =>
          userRepo.getUserById(post.userId).map { userOpt =>
            PostResponse(
              post.title,
              post.content,
              userOpt.map(user => UserResponse(user.name, user.email))
            )
          }
        }
      }

    } yield postResponse

    response.flatMap(res => res)
  }

  def getPostByUserId(userId: Int): Future[Seq[PostEntity]] = {
    for {
      postEntity <- postRepo.getPostsByUserId(userId)
    } yield postEntity
  }

  // Comments Commands
  def createComment(request: CreateCommentRequest, userId: Int, postId: Int): Future[CommentEntity] = {
    for {
      comment <- commentRepo.create(request.comment, userId, postId)
    } yield comment
  }

  def getPostComments(postId: Int): Future[Seq[PostCommentResponse]] = {
    val response = for {
      comments <- commentRepo.getCommentsByPostId(postId)
      commentResponse = Future.sequence {
        comments.map { comment =>
          getUserById(comment.userId).map { userOpt =>
            PostCommentResponse(comment.comment, userOpt.map(_.name))
          }
        }
      }
    } yield commentResponse

    response.flatMap(result => result)
  }

  def getUserById(userId: Int): Future[Option[UserResponse]] = {
    for {
      userOpt <- userRepo.getUserById(userId)
    } yield userOpt.map(user => UserResponse(user.name, user.email))
  }
}
