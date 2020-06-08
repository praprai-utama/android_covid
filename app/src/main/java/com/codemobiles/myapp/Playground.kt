package com.codemobiles.myapp

// constant, compile time
const val name = "Tanakorn"

fun main() {
    // 1. variable

    // Mutable (Implicit)
    var tmp1 = "Codemobiles"
    var tmp2 = 1234
    var tmp3 = true
    var tmp4 = 4.5f // Default double. float by f or F
    var tmp5 = 1.5

    tmp1 = "tanakorn"

    // Mutable (Explicit)
    var tmpA: String = "Codemobiles"
    var tmpB: Int = 1234
    var tmpC: Boolean = true
    var tmpD: Float = 4.5f // Default double. float by f or F
    var tmpE: Double = 1.5


    // Immutable - read only variable (final), runtime
    val tmp6 = 1_000_000
    // tmp6 = 1_000

    print("tmp1: $tmp1, tmp2: $tmp2, tmp3: $tmp3")

    // 2. function
    normalFun()
    argumentFun(a2 = true, a1 = "cat")
    argumentFun(a2 = false)
    println(returnFun("tanakorn"))
}

fun normalFun() {
    println("normalFun")
}

fun argumentFun(a1: String = "none", a2: Boolean) {
    println("a1: $a1, a2: $a2")
}

fun returnFun(name: String): String {
    return "mr. $name"
}




