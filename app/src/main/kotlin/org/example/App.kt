package org.example

class App {
    val greeting: String
        get() = "ehrke"

    fun hello() {


    }
}


fun foo(
    a: Int,
    b: Int
) {
    println(a + b)

    println(
        Math.PI

    )
}

fun main() {
    var a = App()

    println(a.greeting)

    foo(
        5,
        10
    )

    println("what the fuc")
}
