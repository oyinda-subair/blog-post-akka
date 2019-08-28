package org.d3.assessment.commands

import akka.http.scaladsl.server.Directives
import org.d3.assessment.messages.{CreateCommentRequest, CreatePostRequest, CreateUserRequest}
import org.d3.assessment.testkit
import org.d3.assessment.Util._
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

class BlogPostCommandSpec extends WordSpec with Matchers with Directives with testkit with ScalaFutures {

  implicit val pConfig: PatienceConfig = PatienceConfig(timeout = scaled(Span(3, Seconds)))


  "Blog Post Command" when {
    "User Commands" should {
      "create user and return user entity" in {
        val post = CreateUserRequest(s"$string10 name", s"$string10@email.com", s"$string10-word")

        whenReady(blogPostCommands.createUser(post)) { response =>
          response.name shouldEqual post.name
          response.email shouldEqual post.email
        }
      }

      "return all users" in {
        val post = CreateUserRequest(s"$string10 all", s"$string10@email.com", s"$string10-word")

        Await.result(blogPostCommands.createUser(post), 10.second)

        whenReady(blogPostCommands.getAllUsers) { response =>
          response.size should be > 0
          response.nonEmpty shouldBe true
        }
      }

      "return user with valid email" in {
        val post = CreateUserRequest(s"$string10 email", s"$string10@email.com", s"$string10-word")

        Await.result(blogPostCommands.createUser(post), 10.second)

        whenReady(blogPostCommands.getUserByEmail(post.email)) { response =>
          response.isDefined shouldBe true
          response.get.name shouldEqual post.name
        }
      }
    }

    "Post Command" should {
      "create post for user" in {
        val user = CreateUserRequest(s"$string10 post", s"$string10@email.com", s"$string10-word")

        whenReady(blogPostCommands.createUser(user)) { response =>
          val post = CreatePostRequest(s"$string10 title", s"$string10 $string10")
          whenReady(blogPostCommands.createPost(post, response.userId)) { postResponse =>
            postResponse.title shouldEqual post.title
            postResponse.content shouldEqual post.content
            postResponse.userId shouldEqual response.userId
          }
        }
      }

      "all post" in {
        whenReady(blogPostCommands.getAllPosts) { response =>
          response.nonEmpty shouldBe true
        }
      }

      "get user post" in {
        val user = CreateUserRequest(s"$string10 post", s"$string10@email.com", s"$string10-word")

        whenReady(blogPostCommands.createUser(user)) { response =>
          val post = CreatePostRequest(s"$string10 title", s"$string10 $string10")
          whenReady(blogPostCommands.createPost(post, response.userId)) { postResponse =>
            whenReady(blogPostCommands.getPostByUserId(response.userId)) { userPost =>
              userPost.nonEmpty shouldBe true
              userPost.size should be > 0
            }
          }
        }
      }
    }

    "Comment Command" should {
      "create user comment on post" in {
        val user = CreateUserRequest(s"$string10 post", s"$string10@email.com", s"$string10-word")

        whenReady(blogPostCommands.createUser(user)) { response =>
          val post = CreatePostRequest(s"$string10 title", s"$string10 $string10")
          whenReady(blogPostCommands.createPost(post, response.userId)) { postResponse =>
            val comment = CreateCommentRequest("i love it")
            whenReady(blogPostCommands.createComment(comment, response.userId, postResponse.postId)) { commentResponse =>
              commentResponse.userId shouldBe response.userId
              commentResponse.postId shouldBe postResponse.postId
            }
          }
        }
      }

      "return comment for post" in {
        val user = CreateUserRequest(s"$string10 post", s"$string10@email.com", s"$string10-word")

        whenReady(blogPostCommands.createUser(user)) { response =>
          val post = CreatePostRequest(s"$string10 title", s"$string10 $string10")
          whenReady(blogPostCommands.createPost(post, response.userId)) { postResponse =>
            val comment = CreateCommentRequest("i love it")
            whenReady(blogPostCommands.createComment(comment, response.userId, postResponse.postId)) { commentResponse =>
              whenReady(blogPostCommands.getPostComments(postResponse.postId)) { postComments =>
                postComments.nonEmpty shouldBe true
                postComments.head.author shouldBe Some(user.name)
              }
            }
          }
        }
      }
    }
  }
}
