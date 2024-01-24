package arrow

sealed interface ArrowOperationState {
    object Loading: ArrowOperationState
    interface Error: ArrowOperationState
}

