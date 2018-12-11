package com.example.admin.padometer

public fun DoubleArray.maxIndex(): Int {
    var max = this[0]
    var maxIndex = 0
    for (i in 1..lastIndex) {
        val e = this[i]
        if (max < e) {
            max = e
            maxIndex = i
        }
    }
    return maxIndex
}