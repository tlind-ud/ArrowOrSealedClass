package arrow.example1.improved

import arrow.core.Either
import arrow.core.raise.Raise
import arrow.core.raise.either
import arrow.example1.NumberError
import shared.NumberApi
import sealed.SealedResult

class NumberRepositoryImproved(private val numberApi: NumberApi) {

    context(Raise<NumberError>)
    suspend fun getNumber(): Int {
        val numberResponse = numberApi.getNumber()
        return if (numberResponse.number % 2 == 0) {
            numberResponse.number
        } else {
            raise(NumberError(numberResponse.number))
        }
    }
}


