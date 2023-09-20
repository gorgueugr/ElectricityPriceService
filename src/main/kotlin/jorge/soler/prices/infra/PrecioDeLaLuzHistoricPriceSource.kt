package jorge.soler.prices.infra

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jorge.soler.prices.domain.HistoricPrice
import jorge.soler.prices.domain.HistoricPriceSource
import kotlinx.serialization.Serializable

@Serializable
data class PrecioDeLaLuzResponseList(val items: Map<String, PrecioDeLaLuzItem> )
@Serializable
data class PrecioDeLaLuzItem(val date: String, val hour: String, val `is-cheap`: Boolean, val  `is-under-avg`: Boolean, val price: Float, val units: String )

class PrecioDeLaLuzHistoricPriceSource: HistoricPriceSource {
    override fun get(year: Int, month: Int, day: Int): List<HistoricPrice> {

//        val client = HttpClient(CIO)
//        val response: HttpResponse = client.get("https://api.preciodelaluz.org/v1/prices/all?zone=PCB").also {
//
//            val body = it.body<PrecioDeLaLuzResponseList>()
//        }
//
//
//
//        client.close()



        TODO("Not yet implemented")
    }
}

/*
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