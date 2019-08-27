package org.d3.assessment

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io.StdIn
import akka.stream.alpakka.slick.scaladsl._
import akka.stream.scaladsl._
import org.d3.assessment.commands.BlogPostCommands
import org.d3.assessment.repo.UserEntities
import org.d3.assessment.database.DB
import org.d3.assessment.route.BlogPostRoute
import slick.basic.DatabaseConfig
import slick.jdbc.{GetResult, JdbcProfile}

import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}

object ServiceMain extends UserEntities with DB {
  def main(args: Array[String]) :Unit = {

    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    lazy val userDb: UserRepository = new UserRepository
    lazy val blogPostCommands = new BlogPostCommands(userDb)
    lazy val blogPostRoute = new BlogPostRoute(blogPostCommands)

    lazy val route = blogPostRoute.routes


    val bindingFuture = Http().bindAndHandle(route, "localhost", 5000)

    println(s"Server online at http://localhost:5000/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}
