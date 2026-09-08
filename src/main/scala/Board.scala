import scala.collection.immutable.ArraySeq

opaque type Board = ArraySeq[ArraySeq[FieldStatus]]

object Board:
  import cats.Show

  def create: Board = ArraySeq.fill(BoardSize)(ArraySeq.fill(BoardSize)(FieldStatus.Empty))

  given Show[Board] with
    def show(f: Board) =
      val numbers = LazyList.from(1).take(f.size).map(String.format("%1$2d", _))
      val letters = LazyList.from('A').take(f.size).map(_.toChar.toString)

      ("   " + letters.mkString(" ") + "\n") + f
        .zip(numbers)
        .map { case (line, number) =>
          (number + " ") + line
            .map {
              case FieldStatus.Empty => " "
              case FieldStatus.Taken(Player.X) => "X"
              case FieldStatus.Taken(Player.O) => "O"
            }
            .mkString("|")
        }
        .mkString("\n   " + "- " * f.size + "\n")

  extension (f: Board)
    def apply(c: Coordinate): Option[FieldStatus] = for
      line <- f.lift(c.y)
      cell <- line.lift(c.x)
    yield cell

    def update(c: Coordinate, fieldStatus: FieldStatus): Option[Board] =
      import c.{x, y}
      for line <- f.lift(y)
      yield f.updated(y, line.updated(x, fieldStatus))
