package sealed.example1

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import sealed.SealedResult
import sealed.resolve
import shared.combine

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
            SealedResult.resolve(
                result1 = result1,
                result2 = result2,
                result3 = result3,
                onErrorsBlock = {
                    val nonNumberErrors = it.filterNot { it is NumberError }
                    val numberErrors = it.filterIsInstance<NumberError>()
                    when {
                        numberErrors.isNotEmpty() -> {
                            NumberError(*numberErrors.flatMap { it.numbers.toList() }.toIntArray())
                        }
                        nonNumberErrors.isNotEmpty() -> nonNumberErrors[0]
                        else -> SealedResult.genericError()
                    }
                },
                onSuccessBlock = { n1, n2, n3 ->
                    SealedResult.Success(n1 + n2 + n3)
                }
            )
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
        return combine(numberResult1, numberResult2, numberResult3, numberResult4, numberResult5, numberResult6,
            numberResult7, numberResult8, numberResult9
        ) { result1, result2, result3, result4, result5, result6, result7, result8, result9 ->

            val results = arrayOf(result1 ,result2, result3, result4, result5, result6, result7, result8, result9)
            SealedResult.resolve(
                results = results,
                onErrorsBlock = {
                    val nonNumberErrors = it.filterNot { it is NumberError }
                    val numberErrors = it.filterIsInstance<NumberError>()
                    when {
                        numberErrors.isNotEmpty() -> {
                            NumberError(*numberErrors.flatMap { it.numbers.toList() }.toIntArray())
                        }
                        nonNumberErrors.isNotEmpty() -> nonNumberErrors[0]
                        else -> SealedResult.genericError()
                    }
                },
                onSuccessBlock = {
                    SealedResult.Success(
                        (it[0].data as Int) +
                                (it[1].data as Int) +
                                (it[2].data as Int) +
                                (it[3].data as Int) +
                                (it[4].data as Int) +
                                (it[5].data as Int) +
                                (it[6].data as Int) +
                                (it[7].data as Int) +
                                (it[8].data as Int))
                }

            )
        }
    }
}