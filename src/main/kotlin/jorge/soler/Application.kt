package jorge.soler

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import jorge.soler.plugins.*
import jorge.soler.prices.infra.pricesRoutes

fun main() {

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    configureHTTP()
    configureSerialization()
//    configureDatabases()
    configureRouting()
    // pricesRoutes()

}
