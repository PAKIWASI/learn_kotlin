package dev.gol


import java.io.File

typealias Line = CharArray
typealias Matrix = Array<Line>

class GameOfLife(
    // primary constructor
    val width: Int = 150,
    val height: Int = 30,
    val patternFile: String = "./gol.txt",
) {
    private val alive = 'o' // █
    private val dead = ' '

    // trailing lambda (if lambda is last param)
    private var front: Matrix = Array(height) { Line(width) { dead } }
    private var back: Matrix = Array(height) { Line(width) { dead } }

    private val sbFront = StringBuilder(width * height + height + 10)

    // function that runs with the construction process
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
        // lambda func is the only arg to forEachIndexed so we can drop the () entirely. trailing lamda after
        file.readLines().forEachIndexed { i, line ->
            if (i >= height) return@forEachIndexed  // bare return would return whole loadPattern func
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
        sbFront.setLength(0)
        for (i in 0..<height) {
            sbFront.append(front[i])
            sbFront.append('\n')
        }
        print(sbFront)
        System.out.flush()
    }
}
