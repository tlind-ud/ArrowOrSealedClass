package arrow.example1.improved

import arrow.core.Either
import arrow.core.raise.either
import arrow.ArrowOperationState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetNumberUseCaseImproved(private val numberRepository: NumberRepositoryImproved) {

    operator fun invoke(): Flow<Either<ArrowOperationState, Int>> = flow {
        emit(Either.Left(ArrowOperationState.Loading))
        emit(either { numberRepository.getNumber() })
    }
}