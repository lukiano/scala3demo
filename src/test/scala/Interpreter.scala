import cats.{Comonad, Show}
import cats.effect.std.Console
import cats.free.Free

import java.nio.charset.Charset

sealed trait Interact[A]
// case class Read(prompt: String) extends Interact[String]
// case class Print(msg: String) extends Interact[Unit]
case class Pure[A](a: A) extends Interact[A]

type Interpreter[A] = Free[Interact, A]

final class ConsoleInterpreter extends Console[Interpreter] {
  override def readLineWithCharset(charset: Charset): Interpreter[String] = Free.liftF(Pure("test")) // Free.liftInject[Interpreter]

  override def print[A](a: A)(implicit S: Show[A]): Interpreter[Unit] = Free.liftF(Pure(S.show(a)))

  override def println[A](a: A)(implicit S: Show[A]): Interpreter[Unit] = print(a)

  override def error[A](a: A)(implicit S: Show[A]): Interpreter[Unit] = print(a)

  override def errorln[A](a: A)(implicit S: Show[A]): Interpreter[Unit] = print(a)
}

final class CoMonadInteract extends Comonad[Interact] {
  override def extract[A](x: Interact[A]): A = x match
    // case Read(prompt) => prompt
    // case Print(msg) => ()
    case Pure(a) => a  

  override def coflatMap[A, B](fa: Interact[A])(f: Interact[A] => B): Interact[B] = Pure(f(fa))

  override def map[A, B](fa: Interact[A])(f: A => B): Interact[B] = fa match
    case Pure(a) => Pure(f(a))
    // case Read(prompt) => Read(prompt)
    // case Print(msg) => Print(msg)
}


