package arrow.example1

import arrow.ArrowOperationState
import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.zipOrAccumulate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SumUseCaseErrorAccumulation(private val getNumberUseCase: GetNumberUseCase) {

    operator fun invoke(): Flow<Either<ArrowOperationState, Int>> {
        val numberResult1 = getNumberUseCase.invoke()
        val numberResult2 = getNumberUseCase.invoke()
        val numberResult3 = getNumberUseCase.invoke()
        return combine(numberResult1, numberResult2, numberResult3) { result1, result2, result3 ->
            either {
                zipOrAccumulate(
                    action1 = { result1.bind() },
                    action2 = { result2.bind() },
                    action3 = { result3.bind() },
                    combine = { someError, anotherError ->
                        when {
                            someError is NumberError && anotherError is NumberError -> {
                                NumberError(*someError.numbers, *anotherError.numbers)
                            }
                            someError is NumberError -> someError
                            anotherError is NumberError -> anotherError
                            else -> ArrowOperationState.Loading
                        }
                    },
                    block = { num1, num2, num3 -> num1 + num2 + num3}
                )
            }
        }
    }

    fun invokeHuge(): Flow<Either<ArrowOperationState, Int>> {
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
            either {
                zipOrAccumulate(
                    action1 = { result1.bind() },
                    action2 = { result2.bind() },
                    action3 = { result3.bind() },
                    action4 = { result4.bind() },
                    action5 = { result5.bind() },
                    action6 = { result6.bind() },
                    action7 = { result7.bind() },
                    action8 = { result8.bind() },
                    action9 = { result9.bind() },
                    combine = { someError, anotherError ->
                        when {
                            someError is NumberError && anotherError is NumberError -> {
                                NumberError(*someError.numbers, *anotherError.numbers)
                            }
                            someError is NumberError -> someError
                            anotherError is NumberError -> anotherError
                            else -> ArrowOperationState.Loading
                        }
                    },
                    block = { num1, num2, num3, num4, num5, num6, num7, num8, num9 ->
                        num1 + num2 + num3+ num4+ num5+ num6+ num7+ num8+ num9
                    }
                )
            }
        }
    }
}