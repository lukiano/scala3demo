import cats.Show
import cats.effect.IO
import cats.effect.kernel.Ref
import cats.effect.std.Console
import munit.Assertions.fail

import java.nio.charset.Charset

class TestConsole private (
                            inputs: Ref[IO, List[String]],
                            outputs: Ref[IO, List[String]]
                          ) extends Console[IO]:

  def printedLines: IO[List[String]] = outputs.get.map(_.reverse)

  override def readLine: IO[String] =
    inputs.modify {
      case x :: xs => (xs, x)
      case Nil     => fail("No more input strings for test console!")
    }

  override def readLineWithCharset(charset: Charset): IO[String] =
    readLine

  override def print[A](a: A)(implicit S: Show[A]): IO[Unit] =
    println(a)

  override def println[A](a: A)(implicit S: Show[A]): IO[Unit] =
    println(S.show(a))

  override def error[A](a: A)(implicit S: Show[A]): IO[Unit] =
    IO.raiseError(new RuntimeException("Not Implemented"))

  override def errorln[A](a: A)(implicit S: Show[A]): IO[Unit] =
    IO.raiseError(new RuntimeException("Not Implemented"))

  def println(s: String): IO[Unit] =
    outputs.update(xs => s :: xs)

object TestConsole:

  def create(inputs: String*): IO[TestConsole] = for
    inputs <- Ref.of[IO, List[String]](inputs.toList)
    outputs <- Ref.of[IO, List[String]](Nil)
  yield TestConsole(inputs, outputs)
