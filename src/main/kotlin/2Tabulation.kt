// summary:
// visualize the problem as a table
// size the table based on the input
// initialize the table with default values
// seed the trivial answer into the table
// iterate through the table
// fill further positions based on the current position

fun main(args: Array<String>) {
    println(fib(50))
    println(calculate(GridDimension(18, 18)))
    println(canSum(300, listOf(7, 14)))
    println(howSum(300, listOf(100, 200, 8)))
    println(bestSum(100, listOf(1, 2, 5, 25)))
    println(canConstruct("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeef",
        listOf("e", "ee", "eee", "eeee", "eeeee", "eeeeee")))
    println(countConstruct("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeef",
        listOf("e", "ee", "eee", "eeee", "eeeee", "eeeeee")))
    println(allConstruct("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeef",
        listOf("e", "ee", "eee", "eeee", "eeeee", "eeeeee")))
}

// fibonacci calculation
// fibonacci number: sum of the previous two numbers
// 1 1 2 3 5 8 13 21 ...

private fun fib(n: Int): Long {
    // this concept is called tabulation
    val table = Array<Long>(n + 1) { if (it == 0) 1 else 0 }
    for (i in table.indices) {
        val plusOneIndex = i + 1
        if (table.size > plusOneIndex) {
            table[plusOneIndex] += table[i]
        }
        val plusTwoIndex = i + 2
        if (table.size > plusTwoIndex) {
            table[plusTwoIndex] += table[i]
        }
    }
    return table[n]
}

// given: a grid, n x m
// question: how many ways from top left to bottom right?
// limitation: only move down and right

private fun calculate(gridDimension: GridDimension): Long {
    val table = Array<Array<Long>>(gridDimension.m + 1) { m ->
        Array(gridDimension.n + 1) { n ->
            if (n == 1 && m == 1) 1 else 0
        }
    }
    for (m in table.indices) {
        val row = table[m]
        for (n in row.indices) {
            val dimensionDown = GridDimension(m + 1, n)
            if (table.size > dimensionDown.m) {
                table[dimensionDown.m][dimensionDown.n] += row[n]
            }
            val dimensionRight = GridDimension(m, n + 1)
            if (row.size > dimensionRight.n) {
                row[dimensionRight.n] += row[n]
            }
        }
    }
    return table[gridDimension.m][gridDimension.n]
}

// given: a target number and an array of numbers
// question: can the target being built, out of the ones in the array
// limitation: just use addition to build the number

private fun canSum(target: Int, input: List<Int>): Boolean {
    val table = Array(target + 1) { it == 0 }
    for (i in table.indices) {
        if (table[i]) {
            input.forEach {
                val mutatingIndex = i + it
                if (table.size > mutatingIndex) {
                    table[mutatingIndex] = true
                }
            }
        }
    }
    return table[target]
}

// given: a target number and an array of numbers
// question: return any combination of the given numbers, that are summed up equals the target number
// limitation: return null if there is no combination

private fun howSum(target: Int, input: List<Int>): List<Int>? {
    val table = Array<List<Int>?>(target + 1) { if (it == 0) emptyList() else null }
    for (i in table.indices) {
        table[i]?.let { numberList ->
            input.forEach {
                val mutatingIndex = i + it
                if (table.size > mutatingIndex) {
                    table[mutatingIndex] = numberList + it
                }
            }
        }
    }
    return table[target]
}

// given: a target number an array of numbers
// question: return the smallest combination of the given numbers, that are summed up equals the target number
// limitation: return null if there is no combination

private fun bestSum(target: Int, input: List<Int>): List<Int>? {
    val table = Array<List<Int>?>(target + 1) { if (it == 0) emptyList() else null }
    for (i in table.indices) {
        table[i]?.let { numberList ->
            input.forEach {
                val mutatingIndex = i + it
                val newNumberList = numberList + it
                if (table.size > mutatingIndex
                    && (table[mutatingIndex]?.size ?: Int.MAX_VALUE) > newNumberList.size) {
                    table[mutatingIndex] = newNumberList
                }
            }
        }
    }
    return table[target]
}

// given: a target word and a list with words
// question: is it possible to build the target word out of the given words?
// limitation: build the target with concatenating the words

private fun canConstruct(target: String, wordBank: List<String>): Boolean {
    val table = Array(target.length + 1) { it == 0 }
    for (i in table.indices) {
        if (table[i]) {
            wordBank.forEach {
                val endIndex = i + it.length
                if (table.size > endIndex && target.substring(i, endIndex).startsWith(it)) {
                    table[endIndex] = true
                }
            }
        }
    }
    return table[target.length]
}

// given: a target word and a list with words
// question: How many solutions are there to build the target word out of the given words?
// limitation: build the target with concatenating the words

private fun countConstruct(target: String, wordBank: List<String>): Int {
    val table = Array(target.length + 1) { if (it == 0) 1 else 0 }
    for (i in table.indices) {
        if (table[i] > 0 ) {
            wordBank.forEach {
                val endIndex = i + it.length
                if (table.size > endIndex && target.substring(i, endIndex).startsWith(it)) {
                    table[endIndex] += table[i]
                }
            }
        }
    }
    return table[target.length]
}

// given: a target word and a list with words
// question: What are the solutions to build the target word out of the given words?
// limitation: build the target with concatenating the words

private fun allConstruct(target: String, wordBank: List<String>): List<List<String>> {
    val table = Array<List<List<String>>>(target.length + 1) { if (it == 0) listOf(emptyList()) else emptyList() }
    for (i in table.indices) {
        if (table[i].isNotEmpty()) {
            wordBank.forEach { word ->
                val endIndex = i + word.length
                if (table.size > endIndex && target.substring(i, endIndex).startsWith(word)) {
                    // !! we're creating a O(n^m) time and space complexity !!
                    // can't be optimized with tabulation
                    table[endIndex] += table[i].map { listOf(*it.toTypedArray(), word) }
                }
            }
        }
    }
    return table[target.length]
}