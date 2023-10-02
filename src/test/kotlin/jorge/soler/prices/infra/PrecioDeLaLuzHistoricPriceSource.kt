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
import kotlinx.serialization.Serializable
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class PrecioDeLaLuzHistoricPriceSourceTest {


    @Test
    fun `get should return empty list when the date is not today`() = runBlocking {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()        
            }
        }        
        
        val source = PrecioDeLaLuzHistoricPriceSource(client)
   
        val result = async{ source.get(0, 0, 0)}.await()

        assertEquals(emptyList(), result)
    }

    @Test
    fun `get should return list of historic prices when API call succeeds`() = runBlocking {
        val client = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()        
            }
        }

        val source = PrecioDeLaLuzHistoricPriceSource(client)

        // Today
        val date = LocalDate.now()

        val result = async{ source.get(date.year, date.monthValue, date.dayOfMonth ) }.await()


        assertEquals(24, result.size)
    }

   
}
