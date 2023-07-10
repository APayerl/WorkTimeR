import com.fasterxml.jackson.annotation.JsonPropertyOrder
import kotlinx.serialization.Serializable

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
            return if(workday != null) "${workday.start} - ${workday.end}" else "Off"
        }

        return "This weeks work hours: \n" +
                "Monday: " + rowFormatting(monday) + "\n" +
                "Tuesday: " + rowFormatting(tuesday) + "\n" +
                "Wednesday: " + rowFormatting(wednesday) + "\n" +
                "Thursday: " + rowFormatting(thursday) + "\n" +
                "Friday: " + rowFormatting(friday) + "\n" +
                "Saturday: " + rowFormatting(saturday) + "\n" +
                "Sunday: " + rowFormatting(sunday)
    }
}