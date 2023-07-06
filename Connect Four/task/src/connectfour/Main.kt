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
    println(connect4.gameBoard())
    play(connect4)
    println("Game over!")
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

fun play(connect4: Connect4) {
    do {
        val input1 = getValid(connect4.player1, connect4)
        if (input1 == "end") return else println(connect4.updateBoard('o', input1.toInt()))
        if (endGame(connect4, connect4.player1,'o')) return
        val input2 = getValid(connect4.player2, connect4)
        if (input2 == "end") return else println(connect4.updateBoard('*', input2.toInt()))
        if (endGame(connect4,connect4.player2, '*')) return
    } while (true)
}

fun endGame(board: Connect4, player: String, token: Char): Boolean {
    val str = if (board.hasWon(token)) "Player $player won" else if (board.isBoardFull()) "It is a draw" else "continue"
    if (str != "continue") {
        println(str)
        return true
    }
    return false
}

fun getValid(playerName: String, connect4: Connect4): String {
    do {
        println("$playerName's turn:")
        var input = readln()
        input = if (input == "end") return input else connect4.validInput(input)
        if (input.any { !it.isDigit() }) println(input) else return input
    } while (input.any { !it.isDigit() })
    return "end"
}