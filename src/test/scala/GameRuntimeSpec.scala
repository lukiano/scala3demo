import cats.effect.IO
import cats.effect.kernel.Ref
import munit.Assertions._
import cats.effect.std.Console

class GameRuntimeSpec extends munit.CatsEffectSuite:

  val VictoryX: List[String] = List(
    "A1",
    "B1",
    "A2",
    "B2",
    "A3"
  )

  val VictoryO: List[String] = List(
    "A1",
    "B1",
    "A2",
    "B2",
    "C3",
    "B3"
  )

  val Draw: List[String] = List(
    "A1",
    "A2",
    "A3",
    "B1",
    "B3",
    "B2",
    "C2",
    "C3",
    "C1"
  )

  test("should allow playing whole game by player X") {

    for
      console <- TestConsole.create(VictoryX*)
      _ <- {
        implicit val consoleMonad: TestConsole = console
        GameRuntime[IO].run
      }
      lines <- console.printedLines
    yield assert(clue(lines.lastOption).contains("\nPlayer X won the game!\n"))
  }

  test("should allow playing whole game by player O") {

    for
      console <- TestConsole.create(VictoryO*)
      _ <- {
        implicit val consoleMonad: TestConsole = console
        GameRuntime[IO].run
      }
      lines <- console.printedLines
    yield assert(clue(lines.lastOption).contains("\nPlayer O won the game!\n"))
  }

  test("should allow playing whole game with draw") {

    for
      console <- TestConsole.create(Draw*)
      _ <- {
        implicit val consoleMonad: TestConsole = console
        GameRuntime[IO].run
      }
      lines <- console.printedLines
    yield assert(clue(lines.lastOption).contains("\nDraw!\n"))
  }

  test("should reject invalid input while playing") {

    for
      console <- TestConsole.create("bad" +: Draw*)
      _ <- {
        implicit val consoleMonad: TestConsole = console
        GameRuntime[IO].run
      }
      lines <- console.printedLines
    yield assert(clue(lines).contains("Please enter correct coordinate!"))
  }
