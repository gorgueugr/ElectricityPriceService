
import io.ktor.server.application.*
import jorge.soler.prices.application.HistoricPriceService
import jorge.soler.prices.infra.ListHistoricPriceRepository
import jorge.soler.prices.infra.MockHistoricPriceSource
import jorge.soler.prices.infra.RandomHistoricPriceSource
import jorge.soler.prices.infra.PrecioDeLaLuzHistoricPriceSource


fun configureServices()  : HistoricPriceService {
    val historicPriceRepository = ListHistoricPriceRepository()
    // val historicPriceSource = MockHistoricPriceSource()
    // val historicPriceSource = RandomHistoricPriceSource()
    val historicPriceSource = PrecioDeLaLuzHistoricPriceSource()

    val historicPriceService = HistoricPriceService(historicPriceRepository, historicPriceSource)
    


    return historicPriceService
}