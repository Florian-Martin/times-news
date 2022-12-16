package fr.martinflorian.timesnews.data

/**
 * Data states
 */
sealed class State<T> {
    class Loading<T>: State<T>()
    class Success<T>(val data: T): State<T>()
    class Error<T>(val message: String): State<T>()
}