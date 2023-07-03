package connectfour

fun main() {
    val validBoard = Regex("""\s*([5-9])(\s*[xX]\s*)([5-9])\s*""")
    println("Connect Four")
    println("First player's name:")
    val player1 = readln()
    println("Second player's name:")
    val player2 = readln()
    var gameBoard: String
    do {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        gameBoard = readln()
        if (gameBoard.isEmpty()) gameBoard = "6x7"
        if (!validBoard.matches(gameBoard)) println(getBoardErrorMessage(gameBoard))
    } while (!validBoard.matches(gameBoard))
    val (rows, _, cols) = validBoard.find(gameBoard)!!.destructured
    val connect4 = Connect4(player1, player2, rows.toInt(), cols.toInt())
    println("$player1 VS $player2")
    println("${connect4.rows} X ${connect4.columns} board")
    println(connect4.getBoard())
}

fun getBoardErrorMessage(boardString: String): String {
    val (rows, sep, cols) = Regex("""\s*(\d+)\s*(.?)\s*(\d+)\s*""").find(boardString)?.destructured ?: return "Invalid Input"
    return when {
        !Regex("[xX]").matches(sep) -> "Invalid Input"
        rows.toInt() !in 5..9 -> "Board rows should be from 5 to 9"
        cols.toInt() !in 5..9 -> "Board columns should be from 5 to 9"
        else -> "Invalid Input"
    }
}