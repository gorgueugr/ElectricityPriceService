package jorge.soler.prices.infra

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.async
import java.time.LocalDate

class PrecioDeLaLuzHistoricPriceSourceTest {

    @Test
    fun `get should return empty list when API call fails`() = runBlocking {

        val source = PrecioDeLaLuzHistoricPriceSource()
   
        val result = async{ source.get(0, 0, 0)}.await()

        assertEquals(emptyList(), result)
    }

    @Test
    fun `get should return list of historic prices when API call succeeds`() = runBlocking {

        val source = PrecioDeLaLuzHistoricPriceSource()

        // Today
        val date = LocalDate.now()

        val result = async{ source.get(date.year, date.monthValue, date.dayOfMonth ) }.await()


        assertEquals(24, result.size)
    }

   
}
