package connectfour

class Connect4(val player1: String, val player2: String, val rows: Int, val columns: Int) {
    fun getBoard(): String {
        return buildString {
            append((1..columns).joinToString(separator = " ", prefix = " ", postfix = " "))
            repeat(rows) {
                appendLine()
                repeat(columns) { append("║ ") }
                append("║")
            }
            appendLine()
            append("╚")
            repeat(columns - 1) {append("═╩")}
            append("═╝")
        }
    }
}