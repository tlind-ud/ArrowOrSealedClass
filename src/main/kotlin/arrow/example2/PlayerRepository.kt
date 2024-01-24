package arrow.example2

import arrow.core.raise.Raise
import sealed.SealedResult
import shared.Player
import shared.PlayerApi

class PlayerRepository(private val playerApi: PlayerApi) {

    context(Raise<PlayerNotFoundError>)
    suspend fun getPlayer(id: Long): Player {
        return try {
            val playerResponse = playerApi.getPlayer(id)
            Player(
                name = "${playerResponse.firstName} ${playerResponse.lastName}",
                teamId = playerResponse.teamId
            )
        } catch (e: Exception) {
            raise(PlayerNotFoundError(id))
        }
    }
}