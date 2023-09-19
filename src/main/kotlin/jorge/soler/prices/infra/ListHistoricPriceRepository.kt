package jorge.soler.prices.infra

import jorge.soler.prices.domain.HistoricPrice
import jorge.soler.prices.domain.HistoricPriceRepository


class ListHistoricPriceRepository : HistoricPriceRepository {
    private val prices = mutableListOf<HistoricPrice>()

    override fun save(newPrice: HistoricPrice) {
        prices.add(newPrice)
    }

    override fun get(year: Int, month: Int?, day: Int?): List<HistoricPrice> {

        return prices.filter {
            price -> (price.year == year) &&
                (if (month == null) true else price.month == month) &&
                (if (day == null) true else price.day == day)
        }
    }
}