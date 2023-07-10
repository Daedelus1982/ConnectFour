package connectfour

fun main() {
    val validBoard = Regex("""\s*([5-9])(\s*[xX]\s*)([5-9])\s*""")
    println("Connect Four")
    println("First player's name:")
    val player1Name = readln()
    println("Second player's name:")
    val player2Name = readln()
    val player1 = Player(player1Name, 'o')
    val player2 = Player(player2Name, '*')
    var gameBoard: String
    do {
        println("Set the board dimensions (Rows x Columns)")
        println("Press Enter for default (6 x 7)")
        gameBoard = readln()
        if (gameBoard.isEmpty()) gameBoard = "6x7"
        if (!validBoard.matches(gameBoard)) println(getBoardErrorMessage(gameBoard))
    } while (!validBoard.matches(gameBoard))
    val (rows, _, cols) = validBoard.find(gameBoard)!!.destructured
    val gameCount = getValidGames()
    val connect4 = Connect4(player1, player2, rows.toInt(), cols.toInt(), gameCount)
    println("${connect4.player1.name} VS ${connect4.player2.name}")
    println("${connect4.rows} X ${connect4.columns} board")
    println(if (connect4.gameCount == 1) "Single game" else "Total ${connect4.gameCount} games")
    match(connect4)
    println("Game over!")
}

fun getValidGames(): Int {
    val regex = Regex("""\s*[1-9]\d*\s*""")

    do {
        println("Do you want to play single or multiple games?")
        println("For a single game, input 1 or press Enter")
        println("Input a number of games:")
        val games = readln()
        if (games == "" || games == "1") return 1
        if (regex.matches(games)) return games.trim().toInt()
        println("Invalid Input")
    } while (!regex.matches(games))

    return -1
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

fun match(connect4: Connect4) {
    do {
        val (p1, p2) = if (connect4.gameNumber % 2 == 0) connect4.player2 to connect4.player1 else connect4.player1 to connect4.player2
        if (!play(p1, p2, connect4)) return
    } while (connect4.gameNumber <= connect4.gameCount)
}

// return true only if the game finished properly. i.e. player didnt type "end"
fun play(player1: Player, player2: Player, connect4: Connect4): Boolean {
    if (connect4.gameCount > 1) println("Game #${connect4.gameNumber}")
    println(connect4.gameBoard())
    do {
        val input1 = getValid(player1, connect4)
        if (input1 == "end") return false else println(connect4.updateBoard(player1.token, input1.toInt()))
        if (endGame(connect4, player1)) return true
        val input2 = getValid(player2, connect4)
        if (input2 == "end") return false else println(connect4.updateBoard(player2.token, input2.toInt()))
        if (endGame(connect4, player2)) return true
    } while (true)
}

fun endGame(board: Connect4, player: Player): Boolean {
    if (board.hasWon(player.token)) {
        println("Player ${player.name} won")
        board.gameNumber++
        if (board.gameCount > 1) {
            player.score += 2
            board.clearBoard()
            println("Score\n${board.player1.name}: ${board.player1.score} ${board.player2.name}: ${board.player2.score}")
        }
        return true
    }
    if (board.isBoardFull()) {
        println("It is a draw")
        board.gameNumber++
        if (board.gameCount > 1) {
            board.player1.score += 1
            board.player2.score += 1
            board.clearBoard()
            println("Score\n${board.player1.name}: ${board.player1.score} ${board.player2.name}: ${board.player2.score}")
        }
        return true
    }
    return false
}

fun getValid(player: Player, connect4: Connect4): String {
    do {
        println("${player.name}'s turn:")
        var input = readln()
        input = if (input == "end") return input else connect4.validInput(input)
        if (input.any { !it.isDigit() }) println(input) else return input
    } while (input.any { !it.isDigit() })
    return "end"
}