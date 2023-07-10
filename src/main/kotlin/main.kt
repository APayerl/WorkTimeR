import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.io.File
import java.time.format.TextStyle
import java.time.temporal.ChronoField
import java.time.temporal.WeekFields
import java.util.*
import java.util.stream.Stream


fun main() {

    val worktimerPath = File("${System.getProperty("user.home")}/.worktimer")
    if(!worktimerPath.exists()) println("Created directory: ${worktimerPath.mkdir()}")
    val worktimerTomlConfig = File("${worktimerPath.absolutePath}${File.separatorChar}config.toml")
    if(!worktimerTomlConfig.exists()) println("Created toml config: ${worktimerTomlConfig.createNewFile()}")
    val worktimerYamlConfig = File("${worktimerPath.absolutePath}${File.separatorChar}config.yaml")
    if(!worktimerYamlConfig.exists()) println("Created yaml config: ${worktimerYamlConfig.createNewFile()}")

    val mapper = ObjectMapper(YAMLFactory().disable(Feature.WRITE_DOC_START_MARKER)).apply {
        findAndRegisterModules()
        registerModule(SimpleModule().apply {
            addSerializer(LocalTime::class.java, LocalTimeConverter.Serializer(null))
            addDeserializer(LocalTime::class.java, LocalTimeConverter.Deserializer(null))
        })
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
    };

//    val config2: Config = Config(
//        WorkWeek(
//            WorkDay(LocalTime(8, 0), LocalTime(17, 0)),
//            WorkDay(LocalTime(8, 0), LocalTime(17, 0)),
//            WorkDay(LocalTime(8, 0), LocalTime(17, 0)),
//            WorkDay(LocalTime(8, 0), LocalTime(17, 0)),
//            WorkDay(LocalTime(8, 0), LocalTime(17, 0)),
//            null,
//            null
//        ),
//        special = listOf(
//            WorkWeek(
//                WorkDay(LocalTime(8, 0), LocalTime(16, 45)),
//                WorkDay(LocalTime(8, 0), LocalTime(16, 45)),
//                WorkDay(LocalTime(8, 0), LocalTime(16, 45)),
//                WorkDay(LocalTime(8, 0), LocalTime(16, 45)),
//                WorkDay(LocalTime(8, 0), LocalTime(15, 0)),
//                null,
//                null,
//                listOf(21, 36)
//            )
//        )
//    )
//    mapper.writeValue(worktimerYamlConfig, config2)

    val config: Config = mapper.readValue(worktimerYamlConfig, Config::class.java)
    val weekNumber = java.time.LocalDate.now().get(WeekFields.ISO.weekOfYear())
    val weekSchedule = config.special.find {
        (weekNumber == it.validBetweenWeeks[0]) || ((weekNumber >= it.validBetweenWeeks[0]) && (weekNumber <= it.validBetweenWeeks[1]))
    } ?: config.default

    println(weekSchedule)
}