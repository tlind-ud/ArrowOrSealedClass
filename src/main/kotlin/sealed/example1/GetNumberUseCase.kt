package sealed.example1

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sealed.SealedResult

class GetNumberUseCase(private val numberRepository: NumberRepository) {

    operator fun invoke(): Flow<SealedResult<Int>> = flow {
        emit(SealedResult.Loading)
        emit(numberRepository.getNumber())
    }
}