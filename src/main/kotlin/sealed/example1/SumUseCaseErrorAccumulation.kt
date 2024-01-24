package sealed.example1

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import sealed.SealedResult

/**
 * Error accumulation. When we get multiple errors we need to combine them in a deterministic way
 */
class SumUseCaseErrorAccumulation(
    private val getNumberUseCase: GetNumberUseCase
) {
    operator fun invoke(): Flow<SealedResult<Int>> {
        val numberResult1 = getNumberUseCase.invoke()
        val numberResult2 = getNumberUseCase.invoke()
        val numberResult3 = getNumberUseCase.invoke()
        return combine(numberResult1, numberResult2, numberResult3) { result1, result2, result3 ->
            /**
             * If I were to add a 4th result here, we would need to at least 5 more branches to this when statement. For
             * the sealed class approach we will always have (N choose N) + (N choose N-1) + ... + (N choose 1) branches.
             */
            when {
                result1 is NumberError && result2 is NumberError && result3 is NumberError -> {
                    NumberError(*result1.numbers, *result2.numbers, *result3.numbers)
                }
                result1 is NumberError && result2 is NumberError -> {
                    NumberError(*result1.numbers, *result2.numbers)
                }
                result1 is NumberError && result3 is NumberError -> {
                    NumberError(*result1.numbers, *result3.numbers)
                }
                result2 is NumberError && result3 is NumberError -> {
                    NumberError(*result2.numbers, *result3.numbers)
                }
                result1 is NumberError -> result1
                result2 is NumberError -> result2
                result3 is NumberError -> result3
                result1 is SealedResult.Success && result2 is SealedResult.Success && result3 is SealedResult.Success -> {
                    SealedResult.Success(result1.data + result2.data + result3.data)
                }
                else -> SealedResult.Loading
            }
        }
    }

    fun invokeHuge() {
        TODO("Not even going to try and implement this with 9 Flows cuz that would take forever")
        /**
         * Number of when branches
         * 9 choose 9 = 1
         * 9 choose 8 = 9
         * 9 choose 7 = 36
         * 9 choose 6 = 84
         * 9 choose 5 = 126
         * 9 choose 4 = 126
         * 9 choose 3 = 84
         * 9 choose 2 = 36
         * 9 choose 1 = 9
         * 9 choose 0 = 1
         *
         * We would need 512 branches in our when statement here LOL
         */
    }
}