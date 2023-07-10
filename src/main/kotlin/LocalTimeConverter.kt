import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import kotlinx.datetime.LocalTime


object LocalTimeConverter {
    class Serializer(t: Class<LocalTime?>?): StdSerializer<LocalTime?>(t) {
        override fun serialize(value: LocalTime?, gen: JsonGenerator?, provider: SerializerProvider?) {
            value?.let {
                gen?.writeStartObject()
                gen?.writeNumberField("hour", it.hour)
                gen?.writeNumberField("minute", it.minute)
                gen?.writeNumberField("second", it.second)
                gen?.writeNumberField("nanos", it.nanosecond)
                gen?.writeEndObject()
            } ?: gen?.writeNull()
        }
    }

    class Deserializer(vc: Class<*>? = null): StdDeserializer<LocalTime?>(vc) {
        override fun deserialize(p: JsonParser?, ctxt: DeserializationContext?): LocalTime? {
            val node = p!!.codec.readTree<JsonNode>(p)

            return LocalTime(node["hour"].asInt(), node["minute"].asInt(), node["second"].asInt(), node["nanos"].asInt())
        }
    }
}