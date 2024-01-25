package arrow.example1

import arrow.ArrowOperationState
import arrow.core.Either
import arrow.core.raise.either
import shared.NumberApi

class NumberRepository(private val numberApi: NumberApi) {
    suspend fun getNumber(): Either<ArrowOperationState, Int> = either {
        val numberResponse = numberApi.getNumber()
        if (numberResponse.number % 2 == 0) {
            numberResponse.number
        } else {
            raise(NumberError(numberResponse.number))
        }
    }
}

