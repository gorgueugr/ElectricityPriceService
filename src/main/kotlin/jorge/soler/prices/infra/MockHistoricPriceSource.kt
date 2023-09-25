
package jorge.soler.prices.infra

import jorge.soler.prices.domain.HistoricPriceSource
import jorge.soler.prices.domain.HistoricPrice

class MockHistoricPriceSource: HistoricPriceSource {

    override suspend fun get(year: Int, month: Int, day: Int): List<HistoricPrice> { 

        val prices = mutableListOf<HistoricPrice>()
        for (i in 0..23) {
            prices.add(
                HistoricPrice(
                year = year,
                month = month,
                day = day,
                hour = i,
                price = i * 100
            ))
        }
        return prices

    }

}
