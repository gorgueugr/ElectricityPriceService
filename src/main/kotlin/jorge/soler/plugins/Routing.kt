package jorge.soler.plugins

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respondText("Hello World!")
        }
        get("/status") {
            call.respondText("Alive")
        }

        // Static plugin. Try to access `/static/index.html`
        staticResources("/static", "static", index = "index.html")

    }
}