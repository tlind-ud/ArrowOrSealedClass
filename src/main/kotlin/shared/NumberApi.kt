package shared

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlin.random.Random

class NumberApi {

    suspend fun getNumber(): GetNumberResponse {
        delay(3000)
        return GetNumberResponse(
            number = Random.nextInt(),
            timestamp = System.currentTimeMillis()
        )
    }
}

inline fun <reified A, reified B, reified C, reified D, reified E, reified F, reified G, reified H, reified I, Combined> combine(
    a: Flow<A>,
    b: Flow<B>,
    c: Flow<C>,
    d: Flow<D>,
    e: Flow<E>,
    f: Flow<F>,
    g: Flow<G>,
    h: Flow<H>,
    i: Flow<I>,
    crossinline combineBlock: (A, B, C, D, E, F, G, H, I) -> Combined
): Flow<Combined> {
    return kotlinx.coroutines.flow.combine(a, b, c, d, e, f, g, h, i) {
        combineBlock(
            it[0] as A,
            it[1] as B,
            it[2] as C,
            it[3] as D,
            it[4] as E,
            it[5] as F,
            it[6] as G,
            it[7] as H,
            it[8] as I
        )
    }
}