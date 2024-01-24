package sealed.example2

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import sealed.SealedResult
import shared.Player
import shared.Sport
import shared.Team

class GetSportByPlayerIdUseCase(
    private val getPlayerUseCase: GetPlayerUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    private val getSportUseCase: GetSportUseCase
) {

    operator fun invoke(playerId: Long): Flow<SealedResult<Sport>> {
        return getPlayerUseCase.invoke(playerId).transform { playerResult ->
            when(playerResult) {
                is SealedResult.Success<Player> -> {
                    val player = playerResult.data
                    getTeamUseCase.invoke(player.teamId).transform { teamResult ->
                        when(teamResult) {
                            is SealedResult.Success<Team> -> {
                                val team = teamResult.data
                                emitAll(getSportUseCase.invoke(team.sportId))
                            }
                            is TeamNotFoundError -> emit(teamResult)
                            else -> emit(SealedResult.Loading)
                        }
                    }
                }
                is PlayerNotFoundError -> emit(playerResult)
                else -> emit(SealedResult.Loading)
            }
        }
    }
}