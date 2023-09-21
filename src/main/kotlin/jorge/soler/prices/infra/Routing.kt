package jorge.soler.prices.infra

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.HttpStatusCode
import jorge.soler.prices.application.HistoricPriceService
import java.util.Date
import java.util.GregorianCalendar
import java.util.Calendar
import java.util.Locale
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.time.LocalDate
import java.time.temporal.Temporal
import java.time.temporal.TemporalField
import java.time.Year
import java.text.SimpleDateFormat

fun Application.pricesRoutes(historicPriceService: HistoricPriceService): Routing {
    return routing {
        route("/prices/{year?}/{month?}/{day?}") {
            get() {
                // Calculate the actual date
                var date = LocalDate.now()

                var year = call.parameters["year"]?.toInt()
                var month = call.parameters["month"]?.toInt()
                var day = call.parameters["day"]?.toInt()

                // Validate the date
                if (year != null && month != null && day != null) {
                    try{
                        date = LocalDate.of(year, month, day)
                    } catch (e: Exception) {
                        call.respondText("Invalid date format", status = HttpStatusCode.BadRequest)
                        return@get
                    }
                }
                                
                year = date.getYear()
                month = date.getMonthValue()
                day = date.getDayOfMonth()

                call.respond(historicPriceService.list(year, month, day))
            }

        }
    }
}
