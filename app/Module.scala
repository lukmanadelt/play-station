import javax.inject._

import com.google.inject.AbstractModule
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment}
import v1.user._

class Module(environment: Environment, configuration: Configuration) extends AbstractModule with ScalaModule {
  override def configure(): Unit = {
    bind[UserRepository].to[UserRepositoryImpl].in[Singleton]
  }
}
