package sealed

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transform

sealed interface SealedResult<out T> {
    object Loading : SealedResult<Nothing>
    class Success<out T>(val data: T) : SealedResult<T>
    interface Error : SealedResult<Nothing>
    class GenericError(val message: String): Error


   companion object {

       inline fun <reified T> resolve(
           vararg results: SealedResult<*>,
           onErrorsBlock: (List<Error>) -> SealedResult<T> = { it[0] },
           onSuccessBlock: (List<Success<*>>) -> SealedResult<T>
       ): SealedResult<T> {
           return results.all { it is Success }.takeIf { it }?.run {
               onSuccessBlock(results.map { it as Success })
           } ?: results.find {
               it is Loading
           } as? Loading ?: results.filterIsInstance<Error>().takeIf {
               it.isNotEmpty()
           }?.let {
               onErrorsBlock(it)
           } ?: genericError()
       }

       fun genericError(message: String = "Something went wrong") = GenericError(message)
   }
}

inline fun <reified T, reified S> SealedResult<T>.unwrap(
    ifSuccess: (T) -> S,
    ifError: (SealedResult.Error) -> S,
    ifLoading: () -> S
): S {
    return when(this) {
        is SealedResult.Success<T> -> ifSuccess(data)
        is SealedResult.Error -> ifError(this)
        is SealedResult.Loading -> ifLoading()
    }
}

inline fun <reified T, reified S> SealedResult<T>.map(
    transform: (T) -> S
): SealedResult<S> {
    return SealedResult.resolve(
        results = arrayOf(this),
        onSuccessBlock = { SealedResult.Success(transform(it[0].data as T)) }
    )
}

inline fun <reified T, reified S> SealedResult<T>.flatMap(
    transform: (T) -> SealedResult<S>
): SealedResult<S> {
    return SealedResult.resolve(
        results = arrayOf(this),
        onSuccessBlock = { transform(it[0].data as T) }
    )
}

inline infix fun<reified T1, reified T2> Flow<SealedResult<T1>>.chainWith(
    crossinline flow2: (T1) -> Flow<SealedResult<T2>>
): Flow<SealedResult<T2>> {
    return transform { result1 ->
        when(result1) {
            is SealedResult.Error -> emit(result1)
            is SealedResult.Loading -> emit(result1)
            is SealedResult.Success<T1> -> emitAll(flow2(result1.data))
        }
    }
}

inline fun <reified T1, reified T2, reified Combined> SealedResult.Companion.resolve(
    result1: SealedResult<T1>,
    result2: SealedResult<T2>,
    onErrorsBlock: (List<SealedResult.Error>) -> SealedResult.Error = { it[0] },
    onSuccessBlock: (T1, T2) -> SealedResult.Success<Combined>
): SealedResult<Combined> {
    return resolve(
        results = arrayOf(result1, result2),
        onErrorsBlock = onErrorsBlock,
        onSuccessBlock = {
            onSuccessBlock(it[0] as T1, it[1] as T2)
        }
    )
}

inline fun <reified T1, reified T2, reified T3, reified Combined> SealedResult.Companion.resolve(
    result1: SealedResult<T1>,
    result2: SealedResult<T2>,
    result3: SealedResult<T3>,
    onErrorsBlock: (List<SealedResult.Error>) -> SealedResult.Error = { it[0] },
    onSuccessBlock: (T1, T2, T3) -> SealedResult.Success<Combined>
): SealedResult<Combined> {
    return resolve(
        results = arrayOf(result1, result2, result3),
        onErrorsBlock = onErrorsBlock,
        onSuccessBlock = {
            onSuccessBlock(it[0] as T1, it[1] as T2, it[2] as T3)
        }
    )
}






