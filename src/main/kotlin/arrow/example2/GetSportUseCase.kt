package arrow.example2

import arrow.ArrowOperationState
import arrow.core.Either
import arrow.core.raise.either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sealed.SealedResult
import shared.Sport

class GetSportUseCase(private val sportRepository: SportRepository) {

    operator fun invoke(sportId: Long): Flow<Either<ArrowOperationState, Sport>> = flow {
        emit(Either.Left(ArrowOperationState.Loading))
        emit(either { sportRepository.getSport(sportId) })
    }
}
