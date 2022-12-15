package fr.martinflorian.timesnews.utils

import java.text.SimpleDateFormat
import java.util.*

fun String.capitalizeFirstChar(): String {
    return this.replaceFirstChar { it.uppercaseChar() }
}

fun String.capitalizeAllFirstChars(): String {
    return this.split(" ")
        .joinToString(" ") { word ->
            word.lowercase().capitalizeFirstChar()
        }
}

fun String.parseDate(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(parser.parse(this))
}