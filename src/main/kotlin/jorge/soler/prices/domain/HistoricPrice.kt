package jorge.soler.prices.domain

import kotlinx.serialization.Serializable


@Serializable
data class HistoricPrice(val year: Int, val month: Int, val day: Int, val hour: Int ,val price: Int, val coin: Coin = Coin.EURO_CENTS ){
    val date = "${year}/${month}/${day}"
}