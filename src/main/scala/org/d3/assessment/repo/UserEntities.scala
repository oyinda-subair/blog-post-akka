package org.d3.assessment.repo

import org.d3.assessment.database.DB
import org.d3.assessment.messages.CreateUserRequest
import org.d3.assessment.model.UserEntity

import scala.concurrent.{ExecutionContext, Future}

trait UserEntities { this: DB =>
  import driver.api._

  class UserTable(tag: Tag) extends Table[UserEntity](tag, "user_by_id"){
    def id: Rep[Int] = column[Int]("user_id", O.PrimaryKey, O.AutoInc)
    def name: Rep[String] = column[String]("name")
    val index1 = index("index_name", name)
    def email: Rep[String] = column[String]("email", O.Unique)
    val index2 = index("index_email", email)
    def password: Rep[String] = column[String]("password")

    def * = (id, name, email, password) <> ((UserEntity.apply _).tupled, UserEntity.unapply)
  }

  class UserRepository(implicit ex: ExecutionContext) {
    val Users = TableQuery[UserTable]

    def create(entity: CreateUserRequest): Future[UserEntity] = db.run {
      (Users.map(u ⇒ (u.name, u.email, u.password))
        returning Users.map(_.id)
        into ((idName, userId) ⇒ UserEntity(userId, idName._1, idName._2, idName._3))
        ) += (entity.name, entity.email, entity.password)
    }

    def getAllUsers: Future[Seq[UserEntity]] =  db.run (Users.result)

    def getUserByEmail(email: String): Future[Option[UserEntity]] = db.run {
      Users.filter(_.email === email).result.headOption
    }
  }
}
