package jorge.soler.prices.domain


/*
This interface is used to retrieve the price information from an external source
 */
interface HistoricPriceSource {
    suspend fun get(year: Int, month: Int, day: Int) : List<HistoricPrice>
}