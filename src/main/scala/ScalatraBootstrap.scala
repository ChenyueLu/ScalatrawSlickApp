import org.scalatra._
import javax.servlet.ServletContext
import com.example.controllers._

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new UserController, "/users")
    context.mount(new UserController, "/tasks")
  }
}
