package org.d3.assessment.config

import org.d3.assessment.messages.UserClaim
import pdi.jwt.{Jwt, JwtAlgorithm}
import play.api.libs.json.Json

import scala.util.{Failure, Success, Try}

trait JwtTokenGeneratorServices {
  def generateToken(userId : Int) : String
  def verifyToken(token : String) : Try[(String,String,String)]
  def verifyJwtToken(token : String) : Option[UserClaim]
}

class JwtTokenGenerator extends AppConfig with JwtTokenGeneratorServices {

  override def generateToken(userId : Int): String = Jwt.encode(Json.toJson(UserClaim(userId)).toString(), secret, JwtAlgorithm.HS256)
  override def verifyToken(token : String): Try[(String,String,String)] = Jwt.decodeRawAll(token, secret, Seq(JwtAlgorithm.HS256))

  override def verifyJwtToken(token: String): Option[UserClaim] = {
    val jwt = Jwt.decode(token, secret, Seq(JwtAlgorithm.HS256))
    jwt match {
      case Success(x) => Json.parse(x.content).asOpt[UserClaim]
      case Failure(exception) => None
    }
  }
}
