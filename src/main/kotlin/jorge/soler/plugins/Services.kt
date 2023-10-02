
import io.ktor.server.application.*
import jorge.soler.prices.application.HistoricPriceService
import jorge.soler.prices.infra.ListHistoricPriceRepository
import jorge.soler.prices.infra.MockHistoricPriceSource
import jorge.soler.prices.infra.RandomHistoricPriceSource
import jorge.soler.prices.infra.PrecioDeLaLuzHistoricPriceSource
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.Serializable
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

fun configureServices()  : HistoricPriceService {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()        
        }
    }

    val historicPriceRepository = ListHistoricPriceRepository()
    // val historicPriceSource = MockHistoricPriceSource()
    // val historicPriceSource = RandomHistoricPriceSource()
    val historicPriceSource = PrecioDeLaLuzHistoricPriceSource(client)

    val historicPriceService = HistoricPriceService(historicPriceRepository, historicPriceSource)
    


    return historicPriceService
}