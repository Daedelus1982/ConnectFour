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
}