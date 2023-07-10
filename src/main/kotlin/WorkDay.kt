import com.fasterxml.jackson.databind.annotation.JsonSerialize
import kotlinx.datetime.LocalTime
import kotlinx.datetime.serializers.LocalTimeIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class WorkDay(val start: LocalTime, val end: LocalTime)