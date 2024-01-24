package arrow.example2

import arrow.ArrowOperationState
import sealed.SealedResult

data class PlayerNotFoundError(val playerId: Long): ArrowOperationState.Error