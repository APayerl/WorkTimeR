import kotlinx.datetime.LocalTime
import kotlinx.serialization.Serializable
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@Serializable
data class Config(val default: WorkWeek, val special: List<WorkWeek>)

@Serializable
data class WorkDay(val start: LocalTime, val end: LocalTime) {
    override fun toString(): String {
        return "$start - $end"
    }
}

@Serializable
@JsonPropertyOrder("validBetweenWeeks")
class WorkWeek(
    val monday: WorkDay?,
    val tuesday: WorkDay?,
    val wednesday: WorkDay?,
    val thursday: WorkDay?,
    val friday: WorkDay?,
    val saturday: WorkDay?,
    val sunday: WorkDay?,
    val validBetweenWeeks: List<Int> = listOf()
) {
    override fun toString(): String {
        fun rowFormatting(workday: WorkDay?): String {
            return workday?.toString() ?: "Off"
        }

        return "Monday: " + rowFormatting(monday) + "\n" +
                "Tuesday: " + rowFormatting(tuesday) + "\n" +
                "Wednesday: " + rowFormatting(wednesday) + "\n" +
                "Thursday: " + rowFormatting(thursday) + "\n" +
                "Friday: " + rowFormatting(friday) + "\n" +
                "Saturday: " + rowFormatting(saturday) + "\n" +
                "Sunday: " + rowFormatting(sunday)
    }
}