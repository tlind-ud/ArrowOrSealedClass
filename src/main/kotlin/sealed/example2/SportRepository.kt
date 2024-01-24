package sealed.example2

import sealed.SealedResult
import shared.Sport
import shared.SportApi

class SportRepository(private val sportApi: SportApi) {

    suspend fun getSport(id: Long): SealedResult<Sport> {
        return try {
            val sportResponse = sportApi.getSport(id)
            val sport = Sport(
                name = sportResponse.name,
            )
            SealedResult.Success(sport)
        } catch (e: Exception) {
            SportNotFoundError(id)
        }
    }
}