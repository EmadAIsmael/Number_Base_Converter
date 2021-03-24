package converter

import java.math.BigInteger
import kotlin.math.pow

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
    val digits = "0123456789" + ('a'..'z').joinToString("")
    var value = BigInteger.ZERO
    for ((i, c) in v.reversed().withIndex()) {
        val temp = (digits.indexOf(c) * base.toDouble().pow(i)).toString()
        value += BigInteger(temp)
    }
    return value
}

fun readCommand(): Int {
    val commands = arrayOf(
        "/from",
        "/to",
        "/exit"
    )
    var command = ""
    while (command !in commands) {
        print("Do you want to convert /from decimal or /to decimal? (To quit type /exit) ")
        command = readLine()!!
    }
    return commands.indexOf(command)
}

fun convertToDecimal() {
    print("Enter source number: ")
    val n = readLine()!!

    print("Enter source base: ")
    val b = readLine()!!.toInt()

    println("Conversion to decimal result: ${basXToDecimal(n, b)}")
}

fun convertFromDecimal() {
    print("Enter number in decimal system: ")
    val n = readLine()!!.toBigInteger()

    print("Enter target base: ")
    val b = readLine()!!.toInt()

    println("Conversion result: ${n.decimalToBaseX(b)}")
}

fun main() {

    while (true) {
        when (readCommand()) {
            0 -> convertFromDecimal()
            1 -> convertToDecimal()
            2 -> break
        }
    }
}
