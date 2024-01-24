package sealed.example2

import sealed.SealedResult

data class PlayerNotFoundError(val playerId: Long): SealedResult.Error