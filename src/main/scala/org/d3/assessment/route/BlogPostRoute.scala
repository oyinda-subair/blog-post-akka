package org.d3.assessment.route

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import org.d3.assessment.commands.BlogPostCommands
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import org.d3.assessment.messages.{CreatePostRequest, CreateUserRequest, ErrorResponse}

class BlogPostRoute(command: BlogPostCommands) extends PlayJsonSupport {

  // User Routes
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
        complete(StatusCodes.OK, command.getAllUsers)
      }
    }


  // Post routes
  protected val createPost: Route =
    path("posts") {
      post {
        entity(as[CreatePostRequest]) { request =>
          // user constant id until token is implemented
          complete(StatusCodes.Created, command.createPost(request, "1"))
        }
      }
    }

  protected val getAllPosts: Route =
    path("posts") {
      get {
        complete(StatusCodes.OK, command.getAllPost)
      }
    }

  protected val getUserPosts: Route =
    path("user"/"posts"){
      get {
        // user constant id until token is implemented
        complete(StatusCodes.OK, command.getPostByUserId("1"))
      }
    }


  val routes: Route =
    createUser ~
    allUsers ~
    createPost ~
    getAllPosts ~
    getUserPosts
}
