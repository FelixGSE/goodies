package goodies.utils

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.util.Locale

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
        hexString.append("%02x".format(Locale.ROOT, byte))
    }
    return hexString.toString()
}
