package sealed.example1

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import sealed.SealedResult

/**
 * No error accumulation. Just return the first error
 */
class SumUseCaseNoErrorAccumulation(
    private val getNumberUseCase: GetNumberUseCase
) {

    operator fun invoke(): Flow<SealedResult<Int>> {
        val numberResult1 = getNumberUseCase.invoke()
        val numberResult2 = getNumberUseCase.invoke()
        val numberResult3 = getNumberUseCase.invoke()
        return combine(numberResult1, numberResult2, numberResult3) { result1, result2, result3 ->
            when {
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

    fun invokeHuge(): Flow<SealedResult<Int>> {
        val numberResult1 = getNumberUseCase.invoke()
        val numberResult2 = getNumberUseCase.invoke()
        val numberResult3 = getNumberUseCase.invoke()
        val numberResult4 = getNumberUseCase.invoke()
        val numberResult5 = getNumberUseCase.invoke()
        val numberResult6 = getNumberUseCase.invoke()
        val numberResult7 = getNumberUseCase.invoke()
        val numberResult8 = getNumberUseCase.invoke()
        val numberResult9 = getNumberUseCase.invoke()
        return shared.combine(numberResult1, numberResult2, numberResult3, numberResult4, numberResult5, numberResult6,
            numberResult7, numberResult8, numberResult9
        ) { result1, result2, result3, result4, result5, result6, result7, result8, result9 ->
            when {
                result1 is NumberError -> result1
                result2 is NumberError -> result2
                result3 is NumberError -> result3
                result4 is NumberError -> result4
                result5 is NumberError -> result5
                result6 is NumberError -> result6
                result7 is NumberError -> result7
                result8 is NumberError -> result8
                result9 is NumberError -> result9
                result1 is SealedResult.Success &&
                        result2 is SealedResult.Success &&
                        result3 is SealedResult.Success &&
                        result4 is SealedResult.Success &&
                        result5 is SealedResult.Success &&
                        result6 is SealedResult.Success &&
                        result7 is SealedResult.Success &&
                        result8 is SealedResult.Success &&
                        result9 is SealedResult.Success -> {
                    SealedResult.Success(
                        result1.data +
                                result2.data +
                                result3.data +
                                result4.data +
                                result5.data +
                                result6.data +
                                result7.data +
                                result8.data +
                                result9.data
                    )
                }
                else -> SealedResult.Loading
            }
        }
    }
}