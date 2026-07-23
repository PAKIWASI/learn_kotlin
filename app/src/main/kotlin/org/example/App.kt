package org.example

import java.io.File

typealias Line = CharArray
typealias Matrix = Array<Line>

class GameOfLife(
    val width: Int = 100,
    val height: Int = 25,
    val patternFile: String = "./gol.txt",
) {
    private val alive = '█'
    private val dead = ' '

    private var front: Matrix = Array(height) { Line(width) { dead } }
    private var back: Matrix = Array(height) { Line(width) { dead } }

    private val sb = StringBuilder(width * height + height)

    init {
        loadPattern()
    }

    fun getChar(i: Int, j: Int) = front[i][j]
    fun setChar(i: Int, j: Int, c: Char = alive) {
        front[i][j] = c
    }

    private fun loadPattern() {
        val file = File(patternFile)
        if (!file.exists()) {
            seedRandom()
            return
        }
        file.readLines().forEachIndexed { i, line ->
            if (i >= height) return@forEachIndexed
            line.forEachIndexed { j, c ->
                if (j < width && c != ' ' && c != '.') {
                    front[i][j] = alive
                }
            }
        }
    }

    private fun seedRandom() {
        for (i in 0..<height) {
            for (j in 0..<width) {
                front[i][j] = if (Math.random() < 0.2) alive else dead
            }
        }
    }

    private fun countNeighbors(i: Int, j: Int): Int {
        var count = 0
        for (di in -1..1) {
            for (dj in -1..1) {
                if (di == 0 && dj == 0) continue
                // wrap around edges (toroidal grid)
                val ni = (i + di + height) % height
                val nj = (j + dj + width) % width
                if (front[ni][nj] == alive) count++
            }
        }
        return count
    }

    fun step() {
        for (i in 0..<height) {
            for (j in 0..<width) {
                val isAlive = front[i][j] == alive
                val neighbors = countNeighbors(i, j)
                back[i][j] = when {
                    isAlive && (neighbors == 2 || neighbors == 3) -> alive
                    !isAlive && neighbors == 3 -> alive
                    else -> dead
                }
            }
        }
        // swap front/back references only
        val tmp = front
        front = back
        back = tmp
    }

    fun flushBuffer() {
        sb.setLength(0)
        for (i in 0..<height) {
            sb.append(front[i])
            sb.append('\n')
        }
        print(sb)
        System.out.flush()
    }
}

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


