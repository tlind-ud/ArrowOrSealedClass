package arrow.example1

import arrow.ArrowOperationState

class NumberError(vararg val numbers: Int): ArrowOperationState.Error {
    fun message() = buildString {
        append("Errors from ")
        numbers.forEachIndexed { index, i ->
            if (index < numbers.size - 1) {
                append("$i, ")
            } else { append(i) }
        }
    }
}