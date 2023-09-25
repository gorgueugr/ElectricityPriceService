package jorge.soler.prices.infra

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jorge.soler.prices.domain.HistoricPrice
import jorge.soler.prices.domain.HistoricPriceSource
import kotlinx.serialization.Serializable
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
// import io.ktor.serialization.gson.*


import kotlin.io.println
import java.time.LocalDate


//24 hours
@Serializable
data class PrecioDeLaLuzResponseObject(val `00-01`: PrecioDeLaLuzItem, val `01-02`: PrecioDeLaLuzItem, 
val `02-03`: PrecioDeLaLuzItem, val `03-04`: PrecioDeLaLuzItem, val `04-05`: PrecioDeLaLuzItem,
val `05-06`: PrecioDeLaLuzItem, val `06-07`: PrecioDeLaLuzItem, val `07-08`: PrecioDeLaLuzItem,
val `08-09`: PrecioDeLaLuzItem, val `09-10`: PrecioDeLaLuzItem, val `10-11`: PrecioDeLaLuzItem,
val `11-12`: PrecioDeLaLuzItem, val `12-13`: PrecioDeLaLuzItem, val `13-14`: PrecioDeLaLuzItem,
val `14-15`: PrecioDeLaLuzItem, val `15-16`: PrecioDeLaLuzItem, val `16-17`: PrecioDeLaLuzItem,
val `17-18`: PrecioDeLaLuzItem, val `18-19`: PrecioDeLaLuzItem, val `19-20`: PrecioDeLaLuzItem,
val `20-21`: PrecioDeLaLuzItem, val `21-22`: PrecioDeLaLuzItem, val `22-23`: PrecioDeLaLuzItem, val `23-24`: PrecioDeLaLuzItem){
    operator fun iterator(): Iterator<Pair<Int, PrecioDeLaLuzItem>> {
        return listOf(0 to `00-01`, 1 to `01-02`, 2 to `02-03`, 3 to `03-04`, 4 to `04-05`, 5 to `05-06`, 6 to `06-07`, 7 to `07-08`, 8 to `08-09`, 9 to `09-10`, 10 to `10-11`, 11 to `11-12`, 12 to `12-13`, 13 to `13-14`, 14 to `14-15`, 15 to `15-16`, 16 to `16-17`, 17 to `17-18`, 18 to `18-19`, 19 to `19-20`, 20 to `20-21`, 21 to `21-22`, 22 to `22-23`, 23 to `23-24`).iterator()
    }
}

@Serializable
data class PrecioDeLaLuzItem(val date: String, val hour: String, val `is-cheap`: Boolean, val  `is-under-avg`: Boolean, val market: String, val price: Float, val units: String )


    
fun itemConverter(item: PrecioDeLaLuzItem): HistoricPrice {
    val date = item.date.split("-")
    val year = date[2].toInt()
    val month = date[1].toInt()
    val day = date[0].toInt()
    val hour = item.hour.split("-")[0].toInt()
    val price = (item.price /10).toInt() // Convert Mwh to Kwh (x /1000) And then Convert Euro to cents (x * 100) == x / 10

    return HistoricPrice(year, month, day, hour, price)
}
        


class PrecioDeLaLuzHistoricPriceSource: HistoricPriceSource {
    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()        
        }
    }

    override suspend fun get(year: Int, month: Int, day: Int): List<HistoricPrice> {
        // This is intended to get the data from the API: https://api.preciodelaluz.org/v1/prices/all?zone=PCB
        val todayDate = LocalDate.now()
        
        if (year != todayDate.year || month != todayDate.monthValue || day != todayDate.dayOfMonth){
            println("PrecioDeLaLuz API only provides data for today")
            return emptyList()
        }

        try{

            val response: PrecioDeLaLuzResponseObject = client.get("https://api.preciodelaluz.org/v1/prices/all?zone=PCB").body()
            
            val resultList = mutableListOf<HistoricPrice>()
            for((_, value) in response){
                resultList.add(itemConverter(value))
            }
            
            return resultList
        } catch (e: Exception){
            println("Error getting data from PrecioDeLaLuz API")
            return emptyList()
        }
    }
}

/*

PrecioDeLaLuz Response
{
"00-01": {
"date": "20-09-2023",
"hour": "00-01",
"is-cheap": false,
"is-under-avg": true,
"market": "PVPC",
"price": 143.04,
"units": "€/MWh"
},
"01-02": {
"date": "20-09-2023",
"hour": "01-02",
"is-cheap": true,
"is-under-avg": true,
"market": "PVPC",
"price": 131.01,
"units": "€/MWh"
},
"02-03": {
"date": "20-09-2023",
"hour": "02-03",
"is-cheap": false,
"is-under-avg": true,
"market": "PVPC",
"price": 143.95,
"units": "€/MWh"
},
"03-04": {
"date": "20-09-2023",
"hour": "03-04",
"is-cheap": false,
"is-under-avg": true,
"market": "PVPC",
"price": 138.38,
"units": "€/MWh"
},
"04-05": {
"date": "20-09-2023",
"hour": "04-05",
"is-cheap": false,
"is-under-avg": true,
"market": "PVPC",
"price": 136.84,
"units": "€/MWh"
},
"05-06": {
"date": "20-09-2023",
"hour": "05-06",
"is-cheap": true,
"is-under-avg": true,
"market": "PVPC",
"price": 136.41,
"units": "€/MWh"
},
"06-07": {
"date": "20-09-2023",
"hour": "06-07",
"is-cheap": false,
"is-under-avg": true,
"market": "PVPC",
"price": 145.08,
"units": "€/MWh"
},
"07-08": {
"date": "20-09-2023",
"hour": "07-08",
"is-cheap": false,
"is-under-avg": true,
"market": "PVPC",
"price": 149.51,
"units": "€/MWh"
},
"08-09": {
"date": "20-09-2023",
"hour": "08-09",
"is-cheap": false,
"is-under-avg": false,
"market": "PVPC",
"price": 175.62,
"units": "€/MWh"
},
"09-10": {
"date": "20-09-2023",
"hour": "09-10",
"is-cheap": false,
"is-under-avg": false,
"market": "PVPC",
"price": 175.22,
"units": "€/MWh"
},
"10-11": {
"date": "20-09-2023",
"hour": "10-11",
"is-cheap": false,
"is-under-avg": false,
"market": "PVPC",
"price": 209.68,
"units": "€/MWh"
},
"11-12": {
"date": "20-09-2023",
"hour": "11-12",
"is-cheap": false,
"is-under-avg": false,
"market": "PVPC",
"price": 185.14,
"units": "€/MWh"
},
"12-13": {
"date": "20-09-2023",
"hour": "12-13",
"is-cheap": false,
"is-under-avg": false,
"market": "PVPC",
"price": 182.83,
"units": "€/MWh"
},
"13-14": {
"date": "20-09-2023",
"hour": "13-14",
"is-cheap": false,
"is-under-avg": false,
"market": "PVPC",
"price": 177.13,
"units": "€/MWh"
},
"14-15": {
"date": "20-09-2023",
"hour": "14-15",
"is-cheap": true,
"is-under-avg": true,
"market": "PVPC",
"price": 112.51,
"units": "€/MWh"
},
"15-16": {
"date": "20-09-2023",
"hour": "15-16",
"is-cheap": true,
"is-under-avg": true,
"market": "PVPC",
"price": 89.64,
"units": "€/MWh"
},
"16-17": {
"date": "20-09-2023",
"hour": "16-17",
"is-cheap": true,
"is-under-avg": true,
"market": "PVPC",
"price": 84.05,
"units": "€/MWh"
},
"17-18": {
"date": "20-09-2023",
"hour": "17-18",
"is-cheap": true,
"is-under-avg": true,
"market": "PVPC",
"price": 115.42,
"units": "€/MWh"
},
"18-19": {
"date": "20-09-2023",
"hour": "18-19",
"is-cheap": false,
"is-under-avg": false,
"market": "PVPC",
"price": 187.71,
"units": "€/MWh"
},
"19-20": {
"date": "20-09-2023",
"hour": "19-20",
"is-cheap": false,
"is-under-avg": false,
"market": "PVPC",
"price": 221.77,
"units": "€/MWh"
},
"20-21": {
"date": "20-09-2023",
"hour": "20-21",
"is-cheap": false,
"is-under-avg": false,
"market": "PVPC",
"price": 221.76,
"units": "€/MWh"
},
"21-22": {
"date": "20-09-2023",
"hour": "21-22",
"is-cheap": false,
"is-under-avg": false,
"market": "PVPC",
"price": 222,
"units": "€/MWh"
},
"22-23": {
"date": "20-09-2023",
"hour": "22-23",
"is-cheap": false,
"is-under-avg": false,
"market": "PVPC",
"price": 172.37,
"units": "€/MWh"
},
"23-24": {
"date": "20-09-2023",
"hour": "23-24",
"is-cheap": false,
"is-under-avg": true,
"market": "PVPC",
"price": 153.46,
"units": "€/MWh"
}
}
 */