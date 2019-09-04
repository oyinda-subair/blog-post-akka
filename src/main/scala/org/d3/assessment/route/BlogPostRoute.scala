package org.d3.assessment.route

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import org.d3.assessment.commands.BlogPostCommands
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import org.d3.assessment.config.DirectiveConfig
import org.d3.assessment.config.PasswordConfig.confirmPassword
import org.d3.assessment.messages._

class BlogPostRoute(command: BlogPostCommands) extends PlayJsonSupport with DirectiveConfig {

  // User Routes
  protected val createUser: Route =
    path("users") {
      post {
        entity(as[CreateUserRequest]) { request =>
          val checkEmail = command.getUserByEmail(request.email)
          val errorResponse = ErrorResponse(409, "CONFLICT", "Email exists already").toStrEntity
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
    path("user" / "posts") {
      post {
        authenticatedWithHeader { userId =>
          entity(as[CreatePostRequest]) { request =>
            complete(StatusCodes.Created, command.createPost(request, userId))
          }
        }
      }
    }

  protected val getAllPosts: Route =
    path("posts") {
      get {
        complete(StatusCodes.OK, command.getAllPosts)
      }
    }

  protected val getUserPosts: Route =
    path("user" / "posts") {
      get {
        authenticatedWithHeader { userId =>
          val errorResponse = ErrorResponse(404, "NOT FOUND", "User does not exists").toStrEntity
          onSuccess(command.getUserById(userId)) {
            case Some(_) => complete(StatusCodes.OK, command.getPostByUserId(userId))
            case None => complete(HttpResponse(StatusCodes.NotFound, entity = errorResponse))
          }
        }
      }
    }

  protected val createComment: Route =
    path("user" / "post" / IntNumber) { postId =>
      post {
        authenticatedWithHeader { userId =>
          entity(as[CreateCommentRequest]) { request =>
            val errorResponse = ErrorResponse(404, "NOT FOUND", "User does not exists").toStrEntity
            onSuccess(command.getUserById(userId)) {
              case Some(_) => complete(StatusCodes.Created, command.createComment(request, userId, postId))
              case None =>
                logger.error("User does not exists")
                complete(HttpResponse(StatusCodes.NotFound, entity = errorResponse))
            }
          }
        }
      }
    }

  protected val login: Route =
    path("login") {
      post{
        entity(as[LoginRequest]) { request =>
          val generateToken = new JwtTokenGenerator
          onSuccess(command.getUserByEmail(request.email)) {
            case Some(user) =>
              val errorResponse = ErrorResponse(StatusCodes.Unauthorized.intValue, "Unauthorized", "Incorrect Password").toStrEntity
              if(confirmPassword(request.password, user.password)) {
                complete(StatusCodes.OK, UserToken(generateToken.generateToken(user.userId)))
              } else {
                logger.error("Incorrect Password")
                complete(HttpResponse(StatusCodes.Unauthorized, entity = HttpEntity(ContentTypes.`application/json`, errorResponse)))
              }

            case None =>
              val errorResponse = ErrorResponse(StatusCodes.Unauthorized.intValue, "NotFound", "Incorrect email").toStrEntity
              logger.error("Incorrect email")
              complete(HttpResponse(StatusCodes.Unauthorized, entity = HttpEntity(ContentTypes.`application/json`, errorResponse)))
          }
        }
      }
    }

  val routes: Route =
    createUser ~
    allUsers ~
    createPost ~
    getAllPosts ~
    getUserPosts ~
    createComment ~
    login
}
