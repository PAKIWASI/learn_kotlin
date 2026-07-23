package dev.gol


fun main() {
    clearScreen()
    hideCursor()
    Runtime.getRuntime().addShutdownHook(Thread {
        showCursor()
        clearScreen()
    })

    val gol = GameOfLife()
    while (true) {
        resetCursor()
        gol.flushBuffer()
        gol.step()
        Thread.sleep(100)
    }
}

fun clearScreen() = print("\u001B[2J\u001B[H")
fun hideCursor() = print("\u001B[?25l")
fun showCursor() = print("\u001B[?25h")
fun resetCursor() = print("\u001B[H")


