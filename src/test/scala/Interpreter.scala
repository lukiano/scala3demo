import cats.Show
import cats.effect.std.Console
import cats.free.Free
import java.nio.charset.Charset

sealed trait Interact[A]
case class Read(charset: Charset) extends Interact[String]
case class Print(msg: String) extends Interact[Unit]

// We don't need pure as it is provided by Free: `Free.pure[Interact, A]()`.
// case class Pure[A](a: A) extends Interact[A]

type Interpreter[A] = Free[Interact, A]

final class ConsoleInterpreter extends Console[Interpreter] {
  override def readLineWithCharset(charset: Charset): Interpreter[String] =
    Free.liftF(Read(charset))

  override def print[A](a: A)(implicit S: Show[A]): Interpreter[Unit] =
    Free.liftF(Print(S.show(a)))

  override def println[A](a: A)(implicit S: Show[A]): Interpreter[Unit] = print(a)

  override def error[A](a: A)(implicit S: Show[A]): Interpreter[Unit] = print(a)

  override def errorln[A](a: A)(implicit S: Show[A]): Interpreter[Unit] = print(a)
}
