import kotlinx.serialization.Serializable

@Serializable
data class Config(val default: WorkWeek, val special: List<WorkWeek>)