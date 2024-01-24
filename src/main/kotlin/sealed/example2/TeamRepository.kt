package sealed.example2

import sealed.SealedResult
import shared.Team
import shared.TeamApi

class TeamRepository(private val teamApi: TeamApi) {

    suspend fun getTeam(id: Long): SealedResult<Team> {
        return try {
            val teamResponse = teamApi.getTeam(id)
            val team = Team(
                name = "${teamResponse.cityName} ${teamResponse.teamName}",
                sportId = teamResponse.sportId
            )
            SealedResult.Success(team)
        } catch (e: Exception) {
            TeamNotFoundError(id)
        }
    }
}