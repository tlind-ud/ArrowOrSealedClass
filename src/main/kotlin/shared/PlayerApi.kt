package shared

import kotlinx.coroutines.delay

class PlayerApi {

    private val players = listOf(
        GetPlayerResponse(
            firstName = "Vince",
            lastName = "Kao",
            id = 1,
            teamId = 1
        ),
        GetPlayerResponse(
            firstName = "Mitchell",
            lastName = "Jackson",
            id = 2,
            teamId = 2
        ),
    )

    suspend fun getPlayer(id: Long): GetPlayerResponse {
        delay(3000)
        val player = players.find { it.id == id }
        return player ?: throw Exception("Could not find player")
    }
}