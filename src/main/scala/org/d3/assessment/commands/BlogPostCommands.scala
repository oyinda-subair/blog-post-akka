package org.d3.assessment.commands

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCode, StatusCodes}
import org.d3.assessment.config.AppConfig
import org.d3.assessment.messages.{CreateUserRequest, ErrorResponse}
import org.d3.assessment.model.UserEntity
import org.d3.assessment.repo.UserEntities

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class BlogPostCommands(userRepo: UserEntities#UserRepository)(implicit val system: ActorSystem) extends AppConfig{

  // User commands
  def createUser(request: CreateUserRequest): Future[UserEntity] = {
    for {
      entity <- userRepo.create(request.copy(password = hashPassword(request.password)))
    } yield entity
  }

  def getAllUsers: Future[Seq[UserEntity]] = {
    for {
      users <- userRepo.getAllUsers
    } yield users
  }

  def getuserByEmail(email: String): Future[Option[UserEntity]] = {
    for {
      user <- userRepo.getUserByEmail(email)
    } yield user
  }

}
