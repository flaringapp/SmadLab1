package com.flaringapp.smadlab1.app.utils

fun<T> Array<T>.swap(from: Int, to: Int) {
    val temp = this[from]
    this[from] = this[to]
    this[to] = temp
}

fun String.isDigitsOrDot() = find { it != '.' && !it.isDigit() } == null