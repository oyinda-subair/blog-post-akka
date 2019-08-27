package org.d3.assessment.commands

import akka.http.scaladsl.server.Directives
import org.d3.assessment.messages.CreateUserRequest
import org.d3.assessment.testkit
import org.d3.assessment.Util._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}

class BlogPostCommandSpec extends WordSpec with Matchers with Directives with testkit with ScalaFutures {

  "Blog Post Command" when {
    "User Commands" should {
      "create user and return user entity" in {
        val post = CreateUserRequest(s"$string10 name", s"$string10@email.com", s"$string10-word")

        whenReady(blogPostCommands.createUser(post)) { response =>
          response.name shouldEqual post.name
          response.email shouldEqual post.email
        }
      }
    }
  }
}
