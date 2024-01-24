package arrow.example2

import arrow.ArrowOperationState
import sealed.SealedResult

data class SportNotFoundError(val sportId: Long): ArrowOperationState.Error