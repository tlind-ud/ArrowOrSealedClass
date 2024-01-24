package sealed.example2

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sealed.SealedResult
import shared.Player

class GetPlayerUseCase(private val playerRepository: PlayerRepository) {

    operator fun invoke(playerId: Long): Flow<SealedResult<Player>> = flow {
        emit(SealedResult.Loading)
        emit(playerRepository.getPlayer(playerId))
    }
}
