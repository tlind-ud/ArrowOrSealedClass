package arrow.example1

import arrow.ArrowOperationState
import arrow.core.Either
import arrow.core.raise.either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class SumUseCaseNoErrorAccumulation(private val getNumberUseCase: GetNumberUseCase) {

    operator fun invoke(): Flow<Either<ArrowOperationState, Int>> {
        val numberResult1 = getNumberUseCase.invoke()
        val numberResult2 = getNumberUseCase.invoke()
        val numberResult3 = getNumberUseCase.invoke()
        return combine(numberResult1, numberResult2, numberResult3) { result1, result2, result3 ->
            either {
                val n1 = result1.bind()
                val n2 = result2.bind()
                val n3 = result3.bind()
                n1 + n2 + n3
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
                result1.bind() +
                        result2.bind() +
                        result3.bind() +
                        result4.bind() +
                        result5.bind() +
                        result6.bind() +
                        result7.bind() +
                        result8.bind() +
                        result9.bind()
            }
        }
    }
}