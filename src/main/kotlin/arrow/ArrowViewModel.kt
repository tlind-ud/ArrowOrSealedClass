package arrow

import arrow.ArrowOperationState
import arrow.core.Either
import arrow.example1.GetNumberUseCase
import arrow.example1.NumberError
import arrow.example1.NumberRepository
import arrow.example1.SumUseCaseErrorAccumulation
import arrow.example2.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import shared.*

class ArrowViewModel {
    private val sumUseCase = SumUseCaseErrorAccumulation(GetNumberUseCase(NumberRepository(NumberApi())))
    private val getSportByPlayerIdUseCase = GetSportByPlayerIdUseCase(
        GetPlayerUseCase(PlayerRepository(PlayerApi())),
        GetTeamUseCase(TeamRepository(TeamApi())),
        GetSportUseCase((SportRepository(SportApi())))
    )

    private val _viewStateFlow = MutableStateFlow<ViewState>(ViewState.NotStarted)
    val viewStateFlow = _viewStateFlow.asStateFlow()

    suspend fun getSum() {
        _viewStateFlow.emitAll(sumUseCase.invoke().map { it.toViewState() })
    }

    suspend fun getSportNameByPlayer(playerId: Long) {
        _viewStateFlow.emitAll(getSportByPlayerIdUseCase.invoke(playerId).map { it.anotherToViewState() })
    }

    sealed interface ViewState {
        object NotStarted: ViewState
        object Loading: ViewState
        class Sum(val sum: Int): ViewState
        class SportName(val sport: String): ViewState
        class Error(val message: String): ViewState
    }
}

fun Either<ArrowOperationState, Int>.toViewState(): ArrowViewModel.ViewState {
    return fold(
        ifLeft = {
            if (it is NumberError) {
                ArrowViewModel.ViewState.Error(it.message())
            } else {
                ArrowViewModel.ViewState.Loading
            }
        },
        ifRight = {
            ArrowViewModel.ViewState.Sum(it)
        }
    )
}

fun Either<ArrowOperationState, Sport>.anotherToViewState(): ArrowViewModel.ViewState {
    return fold(
        ifLeft = {
            when (it) {
                is PlayerNotFoundError -> ArrowViewModel.ViewState.Error("Could not find player ${it.playerId}")
                is TeamNotFoundError -> ArrowViewModel.ViewState.Error("Could not find team ${it.teamId}")
                is SportNotFoundError -> ArrowViewModel.ViewState.Error("Could not find sport ${it.sportId}")
                else -> ArrowViewModel.ViewState.Loading
            }
        },
        ifRight = {
            ArrowViewModel.ViewState.SportName(it.name)
        }
    )
}