package org.d3.assessment

import akka.actor.ActorSystem
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import org.d3.assessment.commands.BlogPostCommands
import org.d3.assessment.database.DB
import org.d3.assessment.repo.{CommentEntities, PostEntities, UserEntities}
import org.d3.assessment.route.BlogPostRoute

import scala.concurrent.ExecutionContextExecutor

trait testkit extends UserEntities with PostEntities with CommentEntities with DB {
  implicit val system: ActorSystem = ActorSystem("blog-test")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  lazy val userDb: UserRepository = new UserRepository
  lazy val postDb: PostRepository = new PostRepository
  lazy val commentDb: CommentRepository = new CommentRepository

  lazy val blogPostCommands = new BlogPostCommands(userDb, postDb, commentDb)
  lazy val blogPostRoute = new BlogPostRoute(blogPostCommands)

  def blogRoute: Route = blogPostRoute.routes
}
