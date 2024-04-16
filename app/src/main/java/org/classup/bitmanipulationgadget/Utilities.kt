package org.classup.bitmanipulationgadget

const val INVALID_TEXT = "Invalid input!"

fun convertToBinary(input: String): String {
    var result = INVALID_TEXT

    when {
        input.startsWith("0b") -> {
            var binaryString = input.substring(2)

            if (binaryString.isNotEmpty()) {
                if (binaryString.all { it == '0' }) {
                    result = "0"
                }
                else if (binaryString.all { it == '0' || it == '1' }) {
                    binaryString = binaryString.trimStart('0')
                    if ( binaryString.length < 65) {
                        result = binaryString
                    }
                }
            }
        }
        input.startsWith("0x") -> {
            try {
                val hexString = input.substring(2)

                if (hexString.length < 17) {
                    val hexValue = hexString.toBigInteger(16)
                    result = hexValue.toString(2)
                }
            } catch (_: Exception) {}
        }
        else -> {
            try {
                val decimalValue = input.toLong()
                result = java.lang.Long.toBinaryString(decimalValue)
            } catch (_: Exception) {}
        }
    }

    return result
}

fun isValid(bmgText: String): Boolean {
    return bmgText != INVALID_TEXT
}