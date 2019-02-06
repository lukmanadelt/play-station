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

  def index: Action[AnyContent] = UserAction.async { implicit request =>
    logger.trace("index: ")
    userResourceHandler.find.map { users =>
      Ok(Json.toJson(users))
    }
  }

  def create: Action[AnyContent] = UserAction.async { implicit request =>
    logger.trace("create: ")
    createJsonPost()
  }

  def show(id: Int): Action[AnyContent] = UserAction.async { implicit request =>
    logger.trace(s"show: id = $id")
    userResourceHandler.lookup(id).map { user =>
      Ok(Json.toJson(user))
    }
  }

  def update(id: Int): Action[AnyContent] = UserAction.async { implicit request =>
    logger.trace(s"update: id = $id")
    updateJsonPost(id)
  }

  private def createJsonPost[A]()(implicit request: UserRequest[A]): Future[Result] = {
    def failure(badForm: Form[UserFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: UserFormInput) = {
      userResourceHandler.create(input).map { user =>
        Created(Json.toJson(user)).withHeaders(LOCATION -> user.user_full_name)
      }
    }

    form.bindFromRequest().fold(failure, success)
  }

  private def updateJsonPost[A](id: Int)(implicit request: UserRequest[A]): Future[Result] = {
    def failure(badForm: Form[UserFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: UserFormInput) = {
      userResourceHandler.update(id, input).map { user =>
        Created(Json.toJson(user)).withHeaders(LOCATION -> user.user_full_name)
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}
