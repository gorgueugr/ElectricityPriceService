package jorge.soler.prices.application

import jorge.soler.prices.domain.Coin
import jorge.soler.prices.domain.HistoricPrice
import jorge.soler.prices.domain.HistoricPriceRepository
import jorge.soler.prices.domain.HistoricPriceSource

class HistoricPriceService(private val historicPriceRepository: HistoricPriceRepository, private val historicPriceSource: HistoricPriceSource) {

    fun list(year: Int, month: Int, day: Int): List<HistoricPrice> {
        val prices = historicPriceRepository.get(year,month,day)
        return prices
//        TODO("If there is no prices get them from the api")
    }

    fun create(historicPrices: List<HistoricPrice>): List<HistoricPrice> {

        historicPrices.forEach {
            price -> historicPriceRepository.save(price)
        }

        return historicPrices
    }

}