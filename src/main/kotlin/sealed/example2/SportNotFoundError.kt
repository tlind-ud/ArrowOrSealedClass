package sealed.example2

import sealed.SealedResult

data class SportNotFoundError(val sportId: Long): SealedResult.Error