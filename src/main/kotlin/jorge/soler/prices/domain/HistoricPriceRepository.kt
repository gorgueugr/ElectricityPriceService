package jorge.soler.prices.domain


interface HistoricPriceRepository {
    fun save(newPrice : HistoricPrice)
    fun get(year: Int, month: Int? = null, day: Int? = null) : List<HistoricPrice>
}