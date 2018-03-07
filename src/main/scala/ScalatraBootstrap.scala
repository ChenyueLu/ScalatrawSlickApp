import org.scalatra._
import javax.servlet.ServletContext
import com.example.controllers._
import com.example.controllers.swagger._


class ScalatraBootstrap extends LifeCycle {

  implicit val swagger = new UserSwagger

  override def init(context: ServletContext) {
    context.mount(new UserController, "/users", "users")
    context.mount(new ResourcesApp, "/api-docs")

  }
}
