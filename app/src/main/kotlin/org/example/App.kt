package org.example


typealias Line = Array<Char>
typealias Matrix = Array<Line>

class GameOfLife(
    val width: Int = 100,
    val height: Int = 25,
    val patternFile: String = "./gol.txt",
) {

    // matrix that has `height` rows and `width` columns
    private val buffer: Matrix = Array(height) { Line(width) { 'x' } }

    fun getBuffer() = buffer

    fun getChar(i: Int, j: Int) = buffer[i][j]
    fun setChar(i: Int, j: Int, c: Char = ' ') {
        buffer[i][j] = c
    }


    fun flushBuffer() {
        for (i in 0..<height) {
            for (j in 0..<width) {
                print(buffer[i][j])
            }
            print('\n')
        }
        System.out.flush()
    }
}



fun main() {
    val gol = GameOfLife()
    hideCursor()
    clearScreen()
    while (true) {
        resetCursor()
        gol.flushBuffer()
    }

    showCursor()
}


fun clearScreen() = print("\u001B[2J\u001B[H")
fun hideCursor() = print("\u001B[?25l")
fun showCursor() = print("\u001B[?25h")
fun resetCursor() = print("\u001B[H")
