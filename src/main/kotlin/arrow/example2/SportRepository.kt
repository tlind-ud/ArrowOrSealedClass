package arrow.example2

import arrow.core.raise.Raise
import sealed.SealedResult
import shared.Sport
import shared.SportApi

class SportRepository(private val sportApi: SportApi) {

    context(Raise<SportNotFoundError>)
    suspend fun getSport(id: Long): Sport {
        return try {
            val sportResponse = sportApi.getSport(id)
            Sport(
                name = sportResponse.name,
            )
        } catch (e: Exception) {
            raise(SportNotFoundError(id))
        }
    }
}