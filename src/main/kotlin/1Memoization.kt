// summary:
// think of the problem as a tree
// implement brute force with recursion
// introduce a memory to overcome duplicated tree calculations

fun main(args: Array<String>) {
    println(fib(50).first)
    println(calculate(GridDimension(18, 18)).first)
    println(canSum(300, listOf(7, 14)).first)
    println(howSum(300, listOf(7, 14)).first)
    println(bestSum(100, listOf(1, 2, 5, 25)).first)
}

// fibonacci calculation
// fibonacci number: sum of the previous two numbers
// 1 1 2 3 5 8 13 21 ...

// concept is called memoization
typealias FibMemory = Map<Long, Long>

fun fib(n: Long, fibMemory: FibMemory = emptyMap()): Pair<Long, FibMemory> {
    return if (n <= 2) 1L to (fibMemory + (n to 1))
    else fibMemory[n]?.let { it to fibMemory } ?: run {
        val nCalculation1 = n - 1
        val (fibMinus1, calculationFibMemory) = fib(nCalculation1, fibMemory)
        val nCalculation2 = n - 2
        val (fibMinus2, calculation2FibMemory) = fib(nCalculation2, calculationFibMemory + (nCalculation1 to fibMinus1))
        val newFibMemory = calculation2FibMemory + (nCalculation2 to fibMinus2)
        val result = fibMinus1 + fibMinus2
        result to (newFibMemory + (n to result))
    }
}

// given: a grid, n x m
// question: how many ways from top left to bottom right?
// limitation: only move down and right

// concept: think of the recursive calls like a tree and think of the problem like a tree.
// In the end, this problem similar to the fibonacci problem

typealias GridMemory = Map<GridDimension, Long>

fun calculate(gridDimension: GridDimension, gridMemory: GridMemory = emptyMap()): Pair<Long, GridMemory> {
    return if (gridDimension.m <= 0 || gridDimension.n <= 0) 0L to gridMemory
    else if (gridDimension.m == 1 && gridDimension.n == 1) 1L to gridMemory
    else gridMemory[gridDimension]?.let { it to gridMemory } ?: run {
        val wayOne = calculate(gridDimension.copy(m = gridDimension.m - 1), gridMemory)
        val wayTwo = calculate(gridDimension.copy(n = gridDimension.n - 1), wayOne.second)
        val result = wayOne.first + wayTwo.first
        return result to wayTwo.second + (gridDimension to result)
    }
}

data class GridDimension(val m: Int, val n: Int)

// given: a target number and an array of numbers
// question: can the target being built, out of the ones in the array
// limitation: just use addition to build the number

typealias CanSumMemory = List<Int>

fun canSum(target: Int, input: List<Int>, memory: CanSumMemory = emptyList()): Pair<Boolean, CanSumMemory> {
    return if (target == 0) true to memory
    else if (target < 0) false to memory
    else if (memory.contains(target)) false to memory
    else false to (input.scan(memory) { memory, element ->
        val result = canSum(target - element, input, memory)
        if (result.first) {
            return true to memory
        }
        result.second + element
    }.last() + target)
}

// given: a target number and an array of numbers
// question: return any combination of the given numbers, that are summed up equals the target number
// limitation: return null if there is no combination

typealias HowSumMemory = List<Int>

fun howSum(target: Int, input: List<Int>, memory: HowSumMemory = emptyList()): Pair<List<Int>?, HowSumMemory> {
    return if (target == 0) emptyList<Int>() to memory
    else if (target < 0) null to memory
    else if (memory.contains(target)) null to memory
    else null to (input.scan(memory) { memory, element ->
        val result = howSum(target - element, input, memory)
        result.first?.let { return (it + element) to  memory}
        result.second + element
    }.last() + target)
}

// given: a target number an array of numbers
// question: return the smallest combination of the given numbers, that are summed up equals the target number
// limitation: return null if there is no combination

typealias BestSumMemory = Map<Int, List<Int>?>

fun bestSum(target: Int, input: List<Int>, memory: BestSumMemory = emptyMap()): Pair<List<Int>?, BestSumMemory> {
    return if (target == 0) emptyList<Int>() to memory
    else if (target < 0) null to memory
    else memory[target]?.let { it to memory} ?: run {
        val possibilities = input.scan(emptyList<List<Int>>() to memory) { acc, element ->
            val result = bestSum(target - element, input, acc.second)
            result.first?.let {
                val possibleCombination = it + element
                (listOf(*acc.first.toTypedArray(), possibleCombination)) to result.second
            } ?: (acc.first to result.second)
        }.last()
        if (possibilities.first.isEmpty()) {
            return null to possibilities.second
        }
        val best = if (possibilities.first.isEmpty()) null else possibilities.first.minByOrNull { it.size }
        best to (possibilities.second + ( target to best ))
    }
}