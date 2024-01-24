package sealed.example2

import sealed.SealedResult
import shared.Player
import shared.PlayerApi

class PlayerRepository(private val playerApi: PlayerApi) {

    suspend fun getPlayer(id: Long): SealedResult<Player> {
        return try {
            val playerResponse = playerApi.getPlayer(id)
            val player = Player(
                name = "${playerResponse.firstName} ${playerResponse.lastName}",
                teamId = playerResponse.teamId
            )
            SealedResult.Success(player)
        } catch (e: Exception) {
            PlayerNotFoundError(id)
        }
    }
}