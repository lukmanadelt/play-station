package v1.user

import javax.inject.{Inject, Provider}
import play.api.MarkerContext
import play.api.libs.json._

import scala.concurrent.{ExecutionContext, Future}

case class UserResource(id: String, user_full_name: String, user_email: String, user_password: String,
                        user_address: String, user_phone: String)

object UserResource {
  implicit val implicitWrites = new Writes[UserResource] {
    def writes(user: UserResource): JsValue = {
      Json.obj(
        "id" -> user.id,
        "user_full_name" -> user.user_email,
        "user_email" -> user.user_email,
        "user_password" -> user.user_password,
        "user_address" -> user.user_address,
        "user_phone" -> user.user_phone
      )
    }
  }
}

class UserResourceHandler @Inject()(routerProvider: Provider[UserRouter], userRepository: UserRepository)
                                   (implicit ec: ExecutionContext) {

  def create(userInput: UserFormInput)(implicit mc: MarkerContext): Future[UserResource] = {
    val data = UserData(
      1,
      userInput.user_full_name,
      userInput.user_email,
      userInput.user_password,
      userInput.user_address,
      userInput.user_phone
    )

    userRepository.create(data).map { id =>
      createUserResource(data)
      UserResource(id.toString, data.user_full_name, data.user_email, data.user_password, data.user_address,
        data.user_phone)
    }
  }

  def lookup(id: Int): Future[Option[UserResource]] = {
    val userFuture = userRepository.get(id)

    userFuture.map { maybeUserData =>
      maybeUserData.map { userData =>
        createUserResource(userData)
      }
    }
  }

  def find(implicit mc: MarkerContext): Future[Iterable[UserResource]] = {
    userRepository.list().map { userDataList =>
      userDataList.map(userData => createUserResource(userData))
    }
  }

  private def createUserResource(p: UserData): UserResource = {
    UserResource(
      p.id.toString,
      p.user_full_name,
      p.user_email,
      p.user_password,
      p.user_address,
      p.user_phone
    )
  }

  def update(id: Int, userInput: UserFormInput): Future[UserResource] = {
    val data = UserData(id, userInput.user_full_name, userInput.user_email, userInput.user_password,
      userInput.user_address, userInput.user_phone)

    userRepository.update(data).map { id =>
      createUserResource(data)
      UserResource(data.id.toString, data.user_full_name, data.user_email, data.user_password, data.user_address,
        data.user_phone)
    }
  }
}
