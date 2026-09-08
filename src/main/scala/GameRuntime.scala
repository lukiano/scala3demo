import cats.Monad
import cats.effect.std.Console

final class GameRuntime[F[_]: Monad: Console]:
  import cats.implicits._

  private def readLoop(nextPlayer: Player): F[Coordinate] = for
    _ <- Console[F].println(show"Player $nextPlayer please make a move: ")
    line <- Console[F].readLine
    result <- Coordinate.parse(line) match
      case Some(result) => result.pure[F] // pure just wraps pure value into context of F
      case None =>
        Console[F].println("Please enter correct coordinate!") >> readLoop(nextPlayer)
  yield result

  private def loop(game: Game): F[Unit] = for
    _ <- Console[F].println(show"\n${game.fields}\n")
    _ <- game.status match
      case GameStatus.Drawn => Console[F].println("\nDraw!\n")
      case GameStatus.Won(player) =>
        Console[F].println(show"\nPlayer ${player} won the game!\n")
      case GameStatus.Ongoing(nextPlayer) =>
        for
          _ <- Console[F].println("")
          move <- this.readLoop(nextPlayer)
          _ <- game.move(move, nextPlayer) match
            case Right(updated) => loop(updated)
            case Left(error) =>
              Console[F].println(error.getMessage) >> loop(game)
        yield ()
  yield ()

  val run: F[Unit] =
    Console[F].println("\n-- Starting a new game --\n") >> loop(Game.create)
