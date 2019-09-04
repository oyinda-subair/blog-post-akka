package org.d3.assessment.config

import akka.http.scaladsl.server._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import org.d3.assessment.messages.UserClaim
import org.d3.assessment.route.{JwtTokenGenerator, JwtTokenGeneratorServices}

trait DirectiveConfig extends AppConfig {
  val jwtTokenGenerator: JwtTokenGeneratorServices = new JwtTokenGenerator

  def getUserId(userClaim: UserClaim): Int = userClaim.userId

  def authenticate(token: String): Directive1[Int] = {
    jwtTokenGenerator.verifyJwtToken(token) match {
      case Some(user) => provide(getUserId(user))
      case None =>
        logger.error("Invalid authorization credential type")
        reject(AuthorizationFailedRejection)
    }
  }

  def authenticatedWithHeader: Directive1[Int] = {
    extractCredentials.flatMap {
      case Some(OAuth2BearerToken(token))  => authenticate(token)
      case _ =>
        logger.error("Invalid authorization credential type")
        reject(AuthorizationFailedRejection)
    }
  }

}
