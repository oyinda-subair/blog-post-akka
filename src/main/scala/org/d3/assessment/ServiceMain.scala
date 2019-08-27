package org.d3.assessment

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

import scala.io.StdIn
import org.d3.assessment.commands.BlogPostCommands
import org.d3.assessment.repo.{CommentEntities, PostEntities, UserEntities}
import org.d3.assessment.database.DB
import org.d3.assessment.route.BlogPostRoute

import scala.concurrent.ExecutionContextExecutor

object ServiceMain extends UserEntities with PostEntities with CommentEntities with DB {
  def main(args: Array[String]) :Unit = {

    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    lazy val userDb: UserRepository = new UserRepository
    lazy val postDb: PostRepository = new PostRepository
    lazy val commentDb: CommentRepository = new CommentRepository

    lazy val blogPostCommands = new BlogPostCommands(userDb, postDb, commentDb)
    lazy val blogPostRoute = new BlogPostRoute(blogPostCommands)

    lazy val route = blogPostRoute.routes


    val bindingFuture = Http().bindAndHandle(route, "localhost", 5000)

    println(s"Server online at http://localhost:5000/\nPress RETURN to stop...")
    StdIn.readLine() // lets it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
