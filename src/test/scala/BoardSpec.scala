import cats.syntax.all._

class BoardSpec extends munit.FunSuite:

  test("should correctly show fields") {

    val board = Board.create

    val updated = (for
      first <- board.update(Coordinate(0, 0), FieldStatus.Taken(Player.X))
      second <- first.update(Coordinate(1, 0), FieldStatus.Taken(Player.X))
      third <- second.update(Coordinate(2, 0), FieldStatus.Taken(Player.X))
      fourth <- third.update(Coordinate(0, 1), FieldStatus.Taken(Player.O))
      fifth <- fourth.update(Coordinate(1, 1), FieldStatus.Taken(Player.O))
      sixth <- fifth.update(Coordinate(2, 1), FieldStatus.Taken(Player.O))
    yield sixth).get

    val expected = """|   A B C
      | 1 X|X|X
      |   - - - 
      | 2 O|O|O
      |   - - - 
      | 3  | | """.stripMargin

    assert(clue(updated.show) == expected)

  }
