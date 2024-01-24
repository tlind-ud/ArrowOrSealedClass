package arrow.example2

import arrow.core.raise.Raise
import sealed.SealedResult
import shared.Team
import shared.TeamApi

class TeamRepository(private val teamApi: TeamApi) {

    context(Raise<TeamNotFoundError>)
    suspend fun getTeam(id: Long): Team {
        return try {
            val teamResponse = teamApi.getTeam(id)
            Team(
                name = "${teamResponse.cityName} ${teamResponse.teamName}",
                sportId = teamResponse.sportId
            )
        } catch (e: Exception) {
            raise(TeamNotFoundError(id))
        }
    }
}