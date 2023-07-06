package connectfour

class Connect4(val player1: String, val player2: String, val rows: Int, val columns: Int) {
    private val gameState = MutableList(rows) { MutableList(columns) { ' ' } }

    fun gameBoard(): String {
        return buildString {
            append((1..columns).joinToString(separator = " ", prefix = " ", postfix = " "))
            repeat(rows) {row ->
                appendLine()
                repeat(columns) { col -> append("║${gameState[row][col]}") }
                append("║")
            }
            appendLine()
            append("╚")
            repeat(columns - 1) {append("═╩")}
            append("═╝")
        }
    }

    fun updateBoard(token: Char, column: Int): String {
        val row = nextFreeRow(column)
        gameState[row][column - 1] = token
        return gameBoard()
    }

    private fun nextFreeRow(column: Int): Int {
        for (row in  rows - 1 downTo 0 ) {
            if (gameState[row][column - 1] == ' ') return row
        }
        return -1
    }

    fun validInput(input: String): String = when  {
        input == "end" -> input
        input.any { !it.isDigit() } -> "Incorrect column number"
        input.toInt() !in 1..columns -> "The column number is out of range (1 - $columns)"
        isColumnFull(input.toInt()) -> "Column ${input.toInt()} is full"
        else -> input
    }

    private fun isColumnFull(column: Int): Boolean = gameState.first()[column - 1] != ' '

    fun isBoardFull(): Boolean = (1..columns).map { isColumnFull(it) }.all { it }

    private fun fourInRow(token: Char, row: Int): Boolean {
        var count = 0
        if (row < 0 || row > rows - 1) return false
        for (column in 0 until columns) {
            if (gameState[row][column] == token) count++ else count = 0
            if (count == 4) return true
        }
        return false
    }

    private fun fourInRows(token: Char): Boolean = gameState.indices.map { fourInRow(token, it) }.any { it }

    private fun fourInColumn(token: Char, column: Int): Boolean {
        var count = 0
        if (column < 0 || column > columns - 1) return false
        for (row in 0 until rows) {
            if (gameState[row][column] == token) count++ else count = 0
            if (count == 4) return true
        }
        return false
    }

    private fun fourInColumns(token: Char): Boolean = gameState.first().indices.map { fourInColumn(token, it) }.any { it }

    private fun fourDiagLeftToRight(token: Char, startRow: Int, startColumn: Int): Boolean {
        var count = 0
        repeat(4) {
            if (startRow - count < 0 || startColumn + count >= columns) return false
            if (gameState[startRow - count][startColumn + count] == token) count++ else return false
            if (count == 4) return true
        }
        return false
    }

    private fun fourDiagLeftToRightAny(token: Char): Boolean =
        gameState.indices
            .flatMap { row -> (0 until columns)
                .map { col -> fourDiagLeftToRight(token, row, col) } }.any { it }

    private fun fourDiagRightToLeft(token: Char, startRow: Int, startColumn: Int): Boolean {
        var count = 0
        repeat(4) {
            if (startRow - count < 0 || startColumn - count < 0) return false
            if (gameState[startRow - count][startColumn - count] == token) count++ else return false
            if (count == 4) return true
        }
        return false
    }

    private fun fourDiagRightToLeftAny(token: Char): Boolean =
        gameState.indices
            .flatMap { row -> (0 until columns)
                .map { col -> fourDiagRightToLeft(token, row, col) } }.any { it }

    fun hasWon(token: Char): Boolean =
        listOf(fourInRows(token), fourInColumns(token), fourDiagLeftToRightAny(token), fourDiagRightToLeftAny(token)).any { it }
}
