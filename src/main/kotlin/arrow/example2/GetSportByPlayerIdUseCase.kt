package arrow.example2

import arrow.ArrowOperationState
import arrow.core.Either
import arrow.core.raise.either
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import shared.Sport

class GetSportByPlayerIdUseCase(
    private val getPlayerUseCase: GetPlayerUseCase,
    private val getTeamUseCase: GetTeamUseCase,
    private val getSportUseCase: GetSportUseCase
) {

    operator fun invoke(playerId: Long): Flow<Either<ArrowOperationState, Sport>> {
        return getPlayerUseCase.invoke(playerId) chainWith {
            getTeamUseCase(it.teamId)
        } chainWith {
            getSportUseCase(it.sportId)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun invokeWithBoilerplate(playerId: Long): Flow<Either<ArrowOperationState, Sport>> {
        return getPlayerUseCase.invoke(playerId).map { playerResult ->
            playerResult.fold(
                ifLeft = { flowOf(Either.Left(it)) },
                ifRight = {
                    getTeamUseCase.invoke(it.teamId).map { teamResult ->
                        teamResult.fold(
                            ifLeft = { flowOf(Either.Left(it)) },
                            ifRight = {
                                getSportUseCase.invoke(it.sportId)
                            }
                        )
                    }.flattenConcat()
                }
            )
        }.flattenConcat()
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
inline infix fun <T1, T2> Flow<Either<ArrowOperationState, T1>>.chainWith(
    crossinline otherFlow: (T1) -> Flow<Either<ArrowOperationState, T2>>
): Flow<Either<ArrowOperationState, T2>> {
    return map { result ->
        result.fold(
            ifLeft = { flowOf(Either.Left(it)) },
            ifRight = { otherFlow(it) }
        )
    }.flattenConcat()
}