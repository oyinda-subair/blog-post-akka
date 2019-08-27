package org.d3.assessment.commands

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCode, StatusCodes}
import org.d3.assessment.config.AppConfig
import org.d3.assessment.messages._
import org.d3.assessment.model.{PostEntity, UserEntity}
import org.d3.assessment.repo._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class BlogPostCommands(
                        userRepo: UserEntities#UserRepository,
                        postRepo: PostEntities#PostRepository)(implicit val system: ActorSystem) extends AppConfig{

  // User commands
  def createUser(request: CreateUserRequest): Future[UserResponse] = {
    for {
      entity <- userRepo.create(request.copy(password = hashPassword(request.password)))
    } yield UserResponse(entity.name, entity.email)
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
  def createPost(request: CreatePostRequest, userId: String): Future[PostEntity] = {
    for{
      postEntity <- postRepo.create(request.title, request.content, userId.toInt)
    } yield postEntity
  }

  def getAllPost: Future[Seq[PostResponse]] = {
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

  def getPostByUserId(userId: String): Future[Seq[PostEntity]] = {
    for {
      postEntity <- postRepo.getPostsByUserId(userId.toInt)
    } yield postEntity
  }
}
