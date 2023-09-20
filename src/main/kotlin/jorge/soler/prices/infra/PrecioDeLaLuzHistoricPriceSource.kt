package jorge.soler.prices.infra

import jorge.soler.prices.domain.HistoricPrice
import jorge.soler.prices.domain.HistoricPriceSource

class PrecioDeLaLuzHistoricPriceSource: HistoricPriceSource {
    override fun get(year: Int, month: Int, day: Int): List<HistoricPrice> {
        TODO("Not yet implemented")
    }
}