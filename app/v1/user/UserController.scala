package v1.user

import javax.inject.Inject

import play.api.Logger
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class UserFormInput(id: Option[Int], user_full_name: String, user_email: String, user_password: String,
                         user_address: String, user_phone: String)

class UserController @Inject()(cc: UserControllerComponents)(implicit ec: ExecutionContext)
  extends UserBaseController(cc) {

  private val logger = Logger(getClass)

  private val form: Form[UserFormInput] = Form(
    mapping(
      "id" -> optional(number),
      "user_full_name" -> nonEmptyText,
      "user_email" -> nonEmptyText,
      "user_password" -> nonEmptyText,
      "user_address" -> nonEmptyText,
      "user_phone" -> nonEmptyText
    )(UserFormInput.apply)(UserFormInput.unapply)
  )

  def getUsers: Action[AnyContent] = UserAction.async { implicit request =>
    logger.trace("index: ")
    userResourceHandler.getUsers.map { users =>
      if (users.isEmpty) {
        Ok(Json.prettyPrint(Json.obj(
          "success" -> false,
          "users" -> users,
          "message" -> "Users not found"
        )))
      }

      Ok(Json.prettyPrint(Json.obj(
        "success" -> true,
        "users" -> users
      )))
    }
  }

  def createUser: Action[AnyContent] = UserAction.async { implicit request =>
    logger.trace("create: ")
    createJsonPost()
  }

  private def createJsonPost[A]()(implicit request: UserRequest[A]): Future[Result] = {
    def failure(badForm: Form[UserFormInput]) = {
      Future.successful(BadRequest(Json.prettyPrint(Json.obj(
        "success" -> false,
        "message" -> badForm.errorsAsJson
      ))))
    }

    def success(input: UserFormInput) = {
      userResourceHandler.createUser(input).map { user =>
        Created(Json.prettyPrint(Json.obj(
          "success" -> true,
          "users" -> user,
          "message" -> "User registration success"
        )))
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}
