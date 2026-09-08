import cats.effect.IO
import cats.~>

class GameRuntimeInterpretedSpec extends munit.CatsEffectSuite {

  val VictoryX: List[String] = List(
    "A1",
    "B1",
    "A2",
    "B2",
    "A3"
  )

  test("should allow playing whole game by player X") {
    implicit val consoleMonad: ConsoleInterpreter = new ConsoleInterpreter
    val execution = GameRuntime[Interpreter].run
    for
      console <- TestConsole.create(VictoryX*)
      _ <- execution.foldMap(new (Interact ~> IO) {
        def apply[A](operation: Interact[A]): IO[A] = operation match
          case Read(charset) => console.readLineWithCharset(charset)
          case Print(message) => console.println(message)
      })
      lines <- console.printedLines
    yield assertEquals(lines.lastOption, Some("\nPlayer X won the game!\n"))
  }
}
