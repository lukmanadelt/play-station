package v1.user

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.PostgresDriver.api._
import slick.driver.JdbcProfile

import scala.concurrent.Future

final case class UserData(id: Int, user_full_name: String, user_email: String, user_password: String,
                          user_address: String, user_phone: String)

trait UserRepository {
  def create(data: UserData): Future[Int]

  def list(): Future[Iterable[UserData]]

  def get(id: Int): Future[Option[UserData]]

  def update(data: UserData): Future[Int]
}

@Singleton
class UserRepositoryImpl @Inject()(dbConfigProvider: DatabaseConfigProvider) extends UserRepository {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  private val Users = TableQuery[UserTable]

  class UserTable(tag: Tag) extends Table[UserData](tag, "users") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def user_full_name = column[String]("user_full_name")

    def user_email = column[String]("user_email")

    def user_password = column[String]("user_password")

    def user_address = column[String]("user_address")

    def user_phone = column[String]("user_phone")

    override def * =
      (id, user_full_name, user_email, user_password, user_address, user_phone) <> (UserData.tupled, UserData.unapply)
  }

  override def list(): Future[Seq[UserData]] = {
    dbConfig.db.run(Users.result)
  }

  override def get(id: Int): Future[Option[UserData]] = {
    val action = Users.filter(_.id === id).result.headOption
    dbConfig.db.run(action)
  }

  def create(data: UserData): Future[Int] = {
    val action = (Users returning Users.map(_.id)) += data
    dbConfig.db.run(action)
  }

  def update(data: UserData): Future[Int] = {
    val action = Users.filter(_.id === data.id).update(data)
    dbConfig.db.run(action)
  }
}
