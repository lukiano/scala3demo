import cats.implicits._

final case class Coordinate(x: Int, y: Int):
  def translate(d: Direction): Option[Coordinate] = d.bimap(_ + x, _ + y) match
    case (nx, ny) =>
      Option.when(nx >= 0 && nx < BoardSize && ny >= 0 && ny < BoardSize)(
        Coordinate(nx, ny)
      )

object Coordinate:
  import scala.collection.immutable.LazyList
  import cats.Show
  private val Letters = LazyList.from('A').take(BoardSize).toVector

  def parse(s: String): Option[Coordinate] =
    s.toCharArray match
      case Array(letter, digit) => (fromLetter(letter), fromDigit(digit)).mapN(Coordinate.apply)
      case _ => None

  private def fromLetter(c: Char): Option[Int] = Letters.indexOf(c.toUpper.toInt) match
    case -1 => None
    case x => Some(x)

  private def fromDigit(s: Char): Option[Int] = Option
    .when(s.isDigit)(s.asDigit - 1)
    .filter(d => d >= 0 && d < BoardSize)

  given Show[Coordinate] = Show.show(c =>
    Letters.get(c.x) match {
      case Some(letter) if c.y >= 0 && c.y < BoardSize =>
        s"${letter.toChar}${c.y + 1}"
      case _ => "--OUT OF BOUNDS--"
    }
  )
