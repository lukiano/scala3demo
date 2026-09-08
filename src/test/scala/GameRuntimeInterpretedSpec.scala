import cats.free.Free

class GameRuntimeInterpretedSpec extends munit.FunSuite {

  val VictoryX: List[String] = List(
    "A1",
    "B1",
    "A2",
    "B2",
    "A3"
  )

  test("Foo") {
    val obtained = 42
    val expected = 42
    assertEquals(obtained, expected)
  }

  test("should allow playing whole game by player X") {
    implicit val consoleMonad: ConsoleInterpreter = new ConsoleInterpreter
    implicit val comonad = new CoMonadInteract
    val execution = GameRuntime[Interpreter].run
    execution.run
    // assertEquals(execution.run, "\nPlayer X won the game!\n")
  }
}
