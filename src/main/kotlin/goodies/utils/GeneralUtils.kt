package goodies.utils

import java.lang.StringBuilder
import java.nio.charset.StandardCharsets
import java.security.MessageDigest

fun <T> sha512(input: T): String {
    val md = MessageDigest.getInstance("SHA-512")
    val bytes =
        when (input) {
            is String -> input.toByteArray(StandardCharsets.UTF_8)
            is ByteArray -> input
            is Number -> input.toString().toByteArray(StandardCharsets.UTF_8)
            is List<*> -> input.toString().toByteArray(StandardCharsets.UTF_8)
            else -> throw IllegalArgumentException("Unsupported input Type")
        }
    val digest = md.digest(bytes)
    val hexString = StringBuilder()
    for (byte in digest) {
        hexString.append(String.format("%02x", byte))
    }
    return hexString.toString()
}
