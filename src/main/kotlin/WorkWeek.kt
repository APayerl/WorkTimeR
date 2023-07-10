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
)