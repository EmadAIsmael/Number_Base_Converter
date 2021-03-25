package converter

import java.math.BigDecimal
import java.math.BigInteger
import kotlin.system.exitProcess


fun BigDecimal.getIntegral() = this.toString().split(".").map { it.toBigInteger() }[0]

fun BigDecimal.getFractional(): BigDecimal {
    var f = ""
    if (this.toString().contains("."))
        f = this.toString().split(".")[1]
    return BigDecimal("0.$f").setScale(5)
}

class Number(private val value: String, private val base: Int) {

    private val integral: String
    private val fractional: String
    private var baseTenIntegral = BigInteger("0")
    private var baseTenFractional = BigDecimal("0.0")
    private var baseXIntegral = ""
    private var baseXFractional = ""
    private val digits = "0123456789" + ('a'..'z').joinToString("")

    init {
        if (value.contains(".")) {
            integral = value.substringBefore(".")
            fractional = "0.${value.substringAfter(".")}"
        }
        else {
            integral = value
            fractional = ""
        }
    }

    fun toBase(xBase: Int): Number {

        when {
            fractional.isBlank() && base != 10 -> {
                baseTenIntegral = baseXIntegralToBaseTen(base)
                baseXIntegral = baseTenIntegralToBaseX(xBase)
                return Number(baseXIntegral, xBase)
            }
            fractional.isBlank() && base == 10 -> {
                baseTenIntegral = BigInteger(integral)
                baseXIntegral = baseTenIntegralToBaseX(xBase)
                return Number(baseXIntegral, xBase)
            }
            fractional.isNotBlank() && base != 10 -> {
                baseTenIntegral = baseXIntegralToBaseTen(base)
                baseXIntegral = baseTenIntegralToBaseX(xBase)

                baseTenFractional = baseXFractionalToBaseTen(base)
                baseXFractional = baseTenFractionalToBaseX(xBase)
                return Number("${baseXIntegral}.${baseXFractional}", xBase)
            }
            fractional.isNotBlank() && base == 10 -> {
                baseTenIntegral = BigInteger(integral)
                baseXIntegral = baseTenIntegralToBaseX(xBase)

                baseTenFractional = baseXFractionalToBaseTen(base)
                baseXFractional = baseTenFractionalToBaseX(xBase)
                return Number("${baseXIntegral}.${baseXFractional}", xBase)
            }
            else -> return Number("0", 10)
        }
    }

    private fun baseXIntegralToBaseTen(xBase: Int): BigInteger {
        /**
         * convert from base x to base 10.
         */
        var value = BigInteger.ZERO
        for ((i, c) in integral.reversed().withIndex()) {
            val temp = (digits.indexOf(c).toBigDecimal() * xBase.toBigDecimal().pow(i)).toBigInteger()
            value += temp
        }
        return value
    }

    private fun baseTenIntegralToBaseX(xBase: Int): String {
        /**
         * convert from base 10 to base x
         */
        val number = mutableListOf<BigInteger>()
        var quotient = baseTenIntegral
        while (quotient > BigInteger.ZERO) {
            val remainder = quotient % xBase.toBigInteger()
            number.add(remainder)
            quotient /= xBase.toBigInteger()
        }
        if (number.isEmpty()) number.add(BigInteger.ZERO)
        return number.reversed().joinToString("") { digits[it.toInt()].toString() }
    }

    private fun baseXFractionalToBaseTen(xBase: Int): BigDecimal {
        var value = BigDecimal.ZERO
        for ((i, c) in fractional.substringAfter(".").withIndex()) {
            val temp = digits.indexOf(c).toBigDecimal().setScale(5) / xBase.toBigDecimal().pow(i + 1)
            value += BigDecimal(temp.toString())
        }
        return value
    }

    private fun baseTenFractionalToBaseX(xBase: Int): String {
        val baseXFraction = mutableListOf<BigInteger>()
        var n = baseTenFractional.setScale(5)
        do {
            n *= BigDecimal(xBase.toString())
            baseXFraction.add(n.getIntegral())
            n = n.getFractional()
        } while (n > BigDecimal.ZERO && baseXFraction.size < 5)
        var fraction = baseXFraction.joinToString("") { digits[it.toInt()].toString() }
        fraction = (fraction + "0".repeat(5)).substring(0, 5)
        return fraction
    }

    override fun toString(): String {
        return value
    }
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
    print(
        "Enter number in base $from to convert " +
                "to base $to (To go back type /back) "
    )
    return readLine()!!
}

fun main() {
    while (true) {
        val (from, to) = readBases()

        ReadNumber@ while (true) {
            val n = readNumber(from, to)
            if (n == "/back") break@ReadNumber
            val theNumber = Number(n, from)
            val converted = theNumber.toBase(to)
            println("Conversion result: $converted")
        }
    }
}
