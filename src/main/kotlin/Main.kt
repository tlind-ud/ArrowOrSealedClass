import arrow.ArrowViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import sealed.SealedViewModel

class Main {

    companion object {

        val sealedViewModel = SealedViewModel()
        val arrowViewModel = ArrowViewModel()

        @JvmStatic
        fun main(args: Array<String>) {

            val playerId = 2L

            runBlocking {
                launch {
                    arrowViewModel.viewStateFlow.collect {
                        when(it) {
                            is ArrowViewModel.ViewState.Error -> {
                                println(it.message)
                                cancel()
                            }
                            is ArrowViewModel.ViewState.SportName -> {
                                println("Player $playerId plays ${it.sport}")
                                cancel()
                            }
                            is ArrowViewModel.ViewState.Sum -> {
                                println("The sum is ${it.sum}")
                                cancel()
                            }
                            else -> println("Loading...")
                        }
                    }
                }
                arrowViewModel.getSportNameByPlayer(playerId)
            }
        }
    }
}