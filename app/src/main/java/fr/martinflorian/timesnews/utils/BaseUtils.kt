package fr.martinflorian.timesnews.utils

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.text.SimpleDateFormat
import java.util.*

fun String.capitalizeFirstChar(): String {
    return this.replaceFirstChar { it.uppercaseChar() }
}

/**
 * Capitalize all words' first chars of a [String]
 */
fun String.capitalizeAllFirstChars(): String {
    return this.split(" ")
        .joinToString(" ") { word ->
            word.lowercase().capitalizeFirstChar()
        }
}

/**
 *
 */
fun String.parseDate(): String {
    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(parser.parse(this))
}

fun Any.logDebug(message: String) {
    Log.d(getTag(this), message)
}

fun Any.logError(message: String) {
    Log.e(getTag(this), message)
}

/**
 * Returns the class name as a tag
 */
fun getTag(any: Any): String {
    return any.javaClass.simpleName ?: any.javaClass.name
}

/**
 * Either shows or removes a view depending on isShown param value
 *
 * @param view : The view to hide or show
 * @param isShown : Boolean to figure out whether to hide or show the view
 */
fun showView(view: View, isShown: Boolean) {
    view.visibility = when (isShown) {
        true -> View.VISIBLE
        else -> View.GONE
    }
}

/**
 * Displays a [Toast] on UI
 *
 * @param context: The context of the view where to display the [Toast]
 * @param message: The message that has to be displayed to the user by the [Toast]
 */
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

/**
 * Hides or shows the [SwipeRefreshLayout] loading spinner depending on isShown param value
 *
 * @param spinner: The mentioned [SwipeRefreshLayout] loading spinner
 * @param isShown: Boolean to figure out whether to hide or show the spinner
 */
fun showSwipeToRefreshSpinner(spinner: SwipeRefreshLayout, isShown: Boolean) {
    spinner.isRefreshing = isShown
}

/**
 * Hides or shows custom & [SwipeRefreshLayout] loading spinners depending on isShow param value
 *
 * @param spinner: The mentioned [SwipeRefreshLayout] loading spinner
 * @param imageView: A loading animation
 * @param isShown: Boolean to figure out whether to hide or show the spinners
 */
fun showLoadingSpinners(spinner: SwipeRefreshLayout, imageView: ImageView, isShown: Boolean) {
    showSwipeToRefreshSpinner(spinner, isShown)
    showView(imageView, false)
}