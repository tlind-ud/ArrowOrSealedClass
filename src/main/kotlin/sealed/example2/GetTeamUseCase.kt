package sealed.example2

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sealed.SealedResult
import shared.Team

class GetTeamUseCase(private val teamRepository: TeamRepository) {

    operator fun invoke(teamId: Long): Flow<SealedResult<Team>> = flow {
        emit(SealedResult.Loading)
        emit(teamRepository.getTeam(teamId))
    }
}
