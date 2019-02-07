package v1.user

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.PostgresProfile

import scala.concurrent.Future

final case class UserData(id: Int, user_full_name: String, user_email: String, user_password: String,
                          user_address: String, user_phone: String)

trait UserRepository {
  def get(): Future[Iterable[UserData]]

  def create(data: UserData): Future[Int]
}

@Singleton
class UserRepositoryImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends UserRepository {
  private val dbConfig = dbConfigProvider.get[PostgresProfile]

  import dbConfig._
  import profile.api._

  private val Users = TableQuery[UserTable]

  private class UserTable(tag: Tag) extends Table[UserData](tag, "users") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def user_full_name = column[String]("user_full_name")

    def user_email = column[String]("user_email")

    def user_password = column[String]("user_password")

    def user_address = column[String]("user_address")

    def user_phone = column[String]("user_phone")

    override def * =
      (id, user_full_name, user_email, user_password, user_address, user_phone) <> (UserData.tupled, UserData.unapply)
  }

  override def get(): Future[Seq[UserData]] = {
    dbConfig.db.run(Users.result)
  }

  override def create(data: UserData): Future[Int] = {
    val action = (Users returning Users.map(_.id)) += data
    dbConfig.db.run(action)
  }
}
