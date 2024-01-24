package sealed.example2

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sealed.SealedResult
import shared.Sport

class GetSportUseCase(private val sportRepository: SportRepository) {

    operator fun invoke(sportId: Long): Flow<SealedResult<Sport>> = flow {
        emit(SealedResult.Loading)
        emit(sportRepository.getSport(sportId))
    }
}
