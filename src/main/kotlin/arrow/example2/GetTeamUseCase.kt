package arrow.example2

import arrow.ArrowOperationState
import arrow.core.Either
import arrow.core.raise.either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sealed.SealedResult
import shared.Team

class GetTeamUseCase(private val teamRepository: TeamRepository) {

    operator fun invoke(teamId: Long): Flow<Either<ArrowOperationState, Team>> = flow {
        emit(Either.Left(ArrowOperationState.Loading))
        emit(either { teamRepository.getTeam(teamId) })
    }
}
