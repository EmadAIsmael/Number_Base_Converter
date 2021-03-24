package converter

import java.math.BigInteger
import kotlin.math.pow
import kotlin.system.exitProcess

fun BigInteger.decimalToBaseX(base: Int): String {
    /**
     * convert from base 10 to base x
     */
    val digits = "0123456789" + ('a'..'z').joinToString("")
    val number = mutableListOf<BigInteger>()
    var quotient = this
    while (quotient > BigInteger.ZERO) {
        val remainder = quotient % base.toBigInteger()
        number.add(remainder)
        quotient /= base.toBigInteger()
    }
    return number.reversed().joinToString("") { digits[it.toInt()].toString() }
}

fun basXToDecimal(v: String, base: Int): BigInteger {
    /**
     * convert from base x to base 10.
     */
    val digits = "0123456789" + ('a'..'z').joinToString("")
    var value = BigInteger.ZERO
    for ((i, c) in v.reversed().withIndex()) {
        val temp = (digits.indexOf(c) * base.toDouble().pow(i)).toLong()
        value += BigInteger(temp.toString())
    }
    return value
}

fun readBases(): Pair<Int, Int> {
    var baseFrom = 0
    var baseTo = 0
    var input: String
    while (baseFrom !in 2..36 || baseTo !in 2..36) {
        print("Enter two numbers in format: {source base} {target base} (To quit type /exit) ")
        input = readLine()!!
        if (input == "/exit")
            exitProcess(0)
        val (f, t) = input.split(" ").map { it.toInt() }
        baseFrom = f
        baseTo = t
    }
    return Pair(baseFrom, baseTo)
}

fun readNumber(from: Int, to: Int): String {

    print("Enter number in base $from to convert " +
                "to base $to (To go back type /back) ")

    return readLine()!!
}

fun main() {
    while (true) {
        val (from, to) = readBases()

        ReadNumber@while (true) {
            val n = readNumber(from, to)
            if (n == "/back") break@ReadNumber
            val decimal = basXToDecimal(n, from)
            val converted = decimal.decimalToBaseX(to)
            println("Conversion result: $converted")
        }
    }
}
