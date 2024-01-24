package sealed.example2

import sealed.SealedResult

data class TeamNotFoundError(val teamId: Long): SealedResult.Error