package jorge.soler.prices.infra

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import jorge.soler.prices.application.HistoricPriceService

fun Application.pricesRoutes(historicPriceService: HistoricPriceService): Routing {
    return routing {
        route("/prices") {
            get() {
                // call.respondText { "Test" }
                call.respond(historicPriceService.list(2023, 9, 20))
            }
            get("/{year}/{month}/{day}"){
                call.respondText { "Test" }
            }
        }
    }
}
