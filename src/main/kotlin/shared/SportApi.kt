package shared

import kotlinx.coroutines.delay

class SportApi {

    private val sports = listOf(
        GetSportResponse(
            id = 1,
            name = "Basketball"
        ),
        GetSportResponse(
            id = 2,
            name = "Baseball"
        )
    )

    suspend fun getSport(id: Long): GetSportResponse {
        delay(3000)
        val sport = sports.find { it.id == id }
        return sport ?: throw Exception("Could not find sport")
    }
}