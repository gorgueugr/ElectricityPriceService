package jorge.soler.prices.infra

import jorge.soler.prices.domain.Coin
import jorge.soler.prices.domain.HistoricPrice
import java.time.Instant
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ListHistoricPriceRepositoryTest {

    private val repository : ListHistoricPriceRepository = ListHistoricPriceRepository()

    @Test
    fun listRepo() {
        val price = HistoricPrice(
            2023,9,19,
            0,
            10000,
            Coin.EURO_CENTS
        )

        repository.save(price)
        var priceList = repository.get(2023,9,19)
        assertEquals(priceList.size, 1)
        assertEquals(price, priceList[0])

        val price2 = HistoricPrice(
            2023,9,18,
            0,
            10000,
            Coin.EURO_CENTS
        )
        repository.save(price2)

        priceList = repository.get(2023,9)
        assertEquals(priceList.size, 2)
        assertEquals(price, priceList[0])
        assertEquals(price2, priceList[1])

    }

}