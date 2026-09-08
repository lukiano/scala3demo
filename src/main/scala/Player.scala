enum Player {
  case X
  case O

  def next: Player = this match
    case X => O
    case O => X
}

object Player:
  import cats.Show
  import cats.kernel.Eq
  given Show[Player] = Show.fromToString
  given Eq[Player] = Eq.fromUniversalEquals
