package jorge.soler.prices

import jorge.soler.prices.domain.Coin
import jorge.soler.prices.domain.HistoricPrice
import jorge.soler.prices.domain.HistoricPriceSource

class HistoricPriceSourceMock : HistoricPriceSource {
    override suspend fun get(year: Int, month: Int, day: Int): List<HistoricPrice> {

        val resultList = mutableListOf<HistoricPrice>()

        for (i in 0..23){
            resultList.add(
                HistoricPrice(2023,9,19, i, i*100, Coin.EURO_CENTS)
            )
        }

        return resultList
    }

}