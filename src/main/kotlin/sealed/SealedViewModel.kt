package sealed

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.map
import sealed.SealedResult
import sealed.example1.GetNumberUseCase
import sealed.example1.NumberError
import sealed.example1.NumberRepository
import sealed.example1.SumUseCaseErrorAccumulation
import sealed.example2.*
import shared.*

class SealedViewModel {

    private val sumUseCase = SumUseCaseErrorAccumulation(GetNumberUseCase(NumberRepository(NumberApi())))
    private val getSportByPlayerIdUseCase = GetSportByPlayerIdUseCase(
        GetPlayerUseCase(PlayerRepository(PlayerApi())),
        GetTeamUseCase(TeamRepository(TeamApi())),
        GetSportUseCase(SportRepository(SportApi()))
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

fun SealedResult<Int>.toViewState(): SealedViewModel.ViewState {
    return when {
        this is SealedResult.Success -> SealedViewModel.ViewState.Sum(data)
        this is NumberError -> SealedViewModel.ViewState.Error(message())
        else -> SealedViewModel.ViewState.Loading
    }
}

fun SealedResult<Sport>.anotherToViewState(): SealedViewModel.ViewState {
    return when {
        this is SealedResult.Success  -> SealedViewModel.ViewState.SportName(data.name)
        this is PlayerNotFoundError -> SealedViewModel.ViewState.Error("Could not find player $playerId")
        this is TeamNotFoundError -> SealedViewModel.ViewState.Error("Could not find team $teamId")
        this is SportNotFoundError -> SealedViewModel.ViewState.Error("Could not find sport")
        else -> SealedViewModel.ViewState.Loading
    }
}