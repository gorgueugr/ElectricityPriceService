package jorge.soler.prices.application

import jorge.soler.prices.HistoricPriceSourceMock
import jorge.soler.prices.domain.HistoricPrice
import jorge.soler.prices.infra.ListHistoricPriceRepository
import kotlin.test.Test
import kotlin.test.assertEquals


class HistoricPriceServiceTest {

    private val listHistoricPriceRepository = ListHistoricPriceRepository()
    private val sourceMock = HistoricPriceSourceMock()
    private val historicPriceService = HistoricPriceService(listHistoricPriceRepository, sourceMock)

    @Test
    fun list() {

        val prices = historicPriceService.list(2023,9,19)
        assertEquals(prices.size,24)

    }

    @Test
    fun create() {
        val newPrice = HistoricPrice(2023, 9, 20, 0, 100)
        val prices = historicPriceService.create(listOf(newPrice))
        assertEquals(prices.size,1)
        assertEquals(prices[0], newPrice)


    }
}