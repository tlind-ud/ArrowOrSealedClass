package shared

import kotlinx.coroutines.delay

class TeamApi {

    private val teams = listOf(
        GetTeamResponse(
            cityName = "Los Angeles",
            teamName = "Dodgers",
            id = 1,
            sportId = 2
        ),
        GetTeamResponse(
            cityName = "St Louis",
            teamName = "Cardinals",
            id = 2,
            sportId = 2
        )
    )

    suspend fun getTeam(id: Long): GetTeamResponse {
        delay(3000)
        val team = teams.find { it.id == id }
        return team ?: throw Exception("Could not find team")
    }
}