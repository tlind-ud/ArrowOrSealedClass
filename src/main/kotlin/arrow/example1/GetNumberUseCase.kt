package arrow.example1

import arrow.ArrowOperationState
import arrow.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNumberUseCase(private val numberRepository: NumberRepository) {

    operator fun invoke(): Flow<Either<ArrowOperationState, Int>> = flow {
        emit(Either.Left(ArrowOperationState.Loading))
        emit(numberRepository.getNumber())
    }
}