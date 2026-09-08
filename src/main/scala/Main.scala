import cats.effect.IO
import cats.effect.IOApp

object Main extends IOApp.Simple:
  override val run = GameRuntime[IO]().run.foreverM
