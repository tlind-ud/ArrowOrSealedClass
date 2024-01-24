package arrow.example2

import arrow.ArrowOperationState
import arrow.core.Either
import arrow.core.raise.either
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import shared.Sport

class GetSportByPlayerIdUseCase(
    private val getPlayerUseCase: GetPlayerUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    private val getSportUseCase: GetSportUseCase
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(playerId: Long): Flow<Either<ArrowOperationState, Sport>> {
        return either {
            getPlayerUseCase.invoke(playerId).map { playerResult ->
                getTeamUseCase.invoke(playerResult.bind().teamId).map { teamResult ->
                    getSportUseCase.invoke(teamResult.bind().sportId)
                }.flattenConcat()
            }.flattenConcat()
        }.fold(
            ifLeft = { flowOf(Either.Left(it)) },
            ifRight = { it }
        )
    }

    fun invokeAbstracted(playerId: Long): Flow<Either<ArrowOperationState, Sport>> {
        return getPlayerUseCase.invoke(playerId) chainWith { player ->
            getTeamUseCase.invoke(player.teamId)
        } chainWith { team ->
            getSportUseCase.invoke(team.sportId)
        }
    }
}

inline infix fun <T1, T2> Flow<Either<ArrowOperationState, T1>>.chainWith(
    crossinline otherFlow: (T1) -> Flow<Either<ArrowOperationState, T2>>
): Flow<Either<ArrowOperationState, T2>> {
    return either {
        this@chainWith.map { otherFlow(it.bind()) }.flattenConcat()
    }.fold(
        ifLeft = { flowOf(Either.Left(it)) },
        ifRight = { it }
    )
}