package org.d3.assessment.route

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import org.d3.assessment.commands.BlogPostCommands
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import org.d3.assessment.messages.{CreateUserRequest, ErrorResponse}

class BlogPostRoute(command: BlogPostCommands) extends PlayJsonSupport {

  protected val createUser: Route =
    path("users") {
      post {
        entity(as[CreateUserRequest]) { request =>
          val checkEmail = command.getuserByEmail(request.email)
          val errorResponse = ErrorResponse(409, "Email exists already").toStrEntity
          onSuccess(checkEmail) {
            case Some(_) => complete(HttpResponse(StatusCodes.Conflict, entity = errorResponse))
            case None => complete(StatusCodes.Created, command.createUser(request))
          }
        }
      }
    }

  protected val allUsers: Route =
    path("users") {
      get {
        complete(command.getAllUsers)
      }
    }


  val routes: Route =
    createUser ~
    allUsers
}
