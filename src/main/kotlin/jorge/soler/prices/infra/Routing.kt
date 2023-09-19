package jorge.soler.prices.infra

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import javax.management.loading.ClassLoaderRepository

fun Application.pricesRoutes(priceRepository: ClassLoaderRepository): Routing {
    return routing {
        route("/prices") {
            get() {
                call.respondText { "Test" }
            }
            get("/{year}/{month}/{day}"){
                call.respondText { "Test" }
            }
        }
    }
}
