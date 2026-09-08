enum Player {
  case X
  case O

  def next: Player = this match
    case X => O
    case O => X  
}

object Player:
  import cats.kernel.Eq
  import cats.Show
  given Show[Player] = Show.fromToString
  given Eq[Player] = Eq.fromUniversalEquals