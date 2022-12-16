package fr.martinflorian.timesnews.data

sealed class Resource<T> {
    class Success<T>(val data: T) : Resource<T>()
    class Failed<T>(val message: String) : Resource<T>()

    companion object {
        fun <T>Resource<T>.toState(): State<T> {
            return when (this) {
                is Success<T> -> State.Success(this.data)
                is Failed<T> -> State.Error(this.message)
            }
        }
    }
}