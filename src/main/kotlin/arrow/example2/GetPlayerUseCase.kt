package arrow.example2

import arrow.ArrowOperationState
import arrow.core.Either
import arrow.core.raise.either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sealed.SealedResult
import shared.Player

class GetPlayerUseCase(private val playerRepository: PlayerRepository) {

    operator fun invoke(playerId: Long): Flow<Either<ArrowOperationState, Player>> = flow {
        emit(Either.Left(ArrowOperationState.Loading))
        emit(either { playerRepository.getPlayer(playerId) })
    }
}
