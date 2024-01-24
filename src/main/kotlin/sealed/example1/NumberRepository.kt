package sealed.example1

import shared.NumberApi
import sealed.SealedResult

class NumberRepository(private val numberApi: NumberApi) {

    suspend fun getNumber(): SealedResult<Int> {
        val numberResponse = numberApi.getNumber()
        return if (numberResponse.number % 2 == 0) {
            SealedResult.Success(numberResponse.number)
        } else {
            NumberError(numberResponse.number)
        }
    }
}

class NumberError(vararg val numbers: Int): SealedResult.Error {
    fun message() = buildString {
        append("Errors from ")
        numbers.forEachIndexed { index, i ->
            if (index < numbers.size - 1) {
                append("$i, ")
            } else { append(i) }
        }
    }
}
