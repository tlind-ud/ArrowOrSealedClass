package sealed

interface SealedResult<out T> {
    object Loading: SealedResult<Nothing>
    class Success<T>(val data: T): SealedResult<T>
    interface Error: SealedResult<Nothing>
}