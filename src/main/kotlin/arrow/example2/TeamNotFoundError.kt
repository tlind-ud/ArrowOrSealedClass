package arrow.example2

import arrow.ArrowOperationState
import sealed.SealedResult

data class TeamNotFoundError(val teamId: Long): ArrowOperationState.Error