import cats.data.State
import cats.~>

class GameRuntimeEmulatedSpec extends munit.FunSuite {

  val VictoryX: List[String] = List(
    "A1",
    "B1",
    "A2",
    "B2",
    "A3"
  )

  test("should allow playing whole game by player X without IO") {
    case class ConsoleState(inputs: List[String], outputs: List[String])
    type TestState[A] = State[ConsoleState, A]

    implicit val consoleMonad: ConsoleInterpreter = new ConsoleInterpreter
    val execution = GameRuntime[Interpreter].run
    val interpreter = new (Interact ~> TestState) {
      def apply[A](operation: Interact[A]): TestState[A] = operation match
        case Read(_) =>
          State { state =>
            state.inputs match
              case input :: remaining => (state.copy(inputs = remaining), input)
              case Nil => fail("No more input strings for test console!")
          }
        case Print(message) =>
          State.modify { state =>
            state.copy(outputs = message :: state.outputs)
          }
    }

    val result = execution
      .foldMap(interpreter)
      .runS(ConsoleState(VictoryX, Nil))
      .value

    assertEquals(result.inputs, Nil)
    assertEquals(result.outputs.headOption, Some("\nPlayer X won the game!\n"))
  }
}
