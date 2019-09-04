package org.d3.assessment.config

import org.mindrot.jbcrypt.BCrypt

object PasswordConfig {
  def hashPassword(password: String): String = {
    BCrypt.hashpw(password, BCrypt.gensalt())
  }

  def confirmPassword(password: String, hashPassword: String): Boolean = {
    BCrypt.checkpw(password, hashPassword)
  }
}
