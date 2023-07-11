import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import java.io.File
import java.time.temporal.WeekFields

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.dataformat.toml.TomlFactory
import com.fasterxml.jackson.dataformat.toml.TomlMapper
import kotlinx.datetime.LocalTime

fun main(vararg args: String) {
    val useYAML: Boolean = !(args.isNotEmpty() && args[0].equals("toml", true))

    val worktimerPath = File("${System.getProperty("user.home")}/.worktimer")
    if(!worktimerPath.exists()) println("Created directory: ${worktimerPath.mkdir()}")

    val worktimerConfig =
        if(!useYAML)
            File("${worktimerPath.absolutePath}${File.separatorChar}config.toml")
        else
            File("${worktimerPath.absolutePath}${File.separatorChar}config.yaml")

    if (!worktimerConfig.exists()) println("Created config: ${worktimerConfig.createNewFile()}")

    val factory: JsonFactory =
        if(useYAML)
            YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
        else TomlFactory()

//    val tomlMapper = TomlMapper()
    val mapper: ObjectMapper = ObjectMapper(factory).apply {
        findAndRegisterModules()
        registerModule(SimpleModule().apply {
            addSerializer(LocalTime::class.java, LocalTimeConverter.Serializer(null))
            addDeserializer(LocalTime::class.java, LocalTimeConverter.Deserializer(null))
        })
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    val config2: Config = Config(
        WorkWeek(
            WorkDay(LocalTime(8, 0), LocalTime(17, 0)),
            WorkDay(LocalTime(8, 0), LocalTime(17, 0)),
            WorkDay(LocalTime(8, 0), LocalTime(17, 0)),
            WorkDay(LocalTime(8, 0), LocalTime(17, 0)),
            WorkDay(LocalTime(8, 0), LocalTime(17, 0)),
            null,
            null
        ),
        special = listOf(
            WorkWeek(
                WorkDay(LocalTime(8, 0), LocalTime(16, 45)),
                WorkDay(LocalTime(8, 0), LocalTime(16, 45)),
                WorkDay(LocalTime(8, 0), LocalTime(16, 45)),
                WorkDay(LocalTime(8, 0), LocalTime(16, 45)),
                WorkDay(LocalTime(8, 0), LocalTime(15, 0)),
                null,
                null,
                listOf(21, 36)
            )
        )
    )
    mapper.writeValue(worktimerConfig, config2)

    val config: Config = mapper.readValue(worktimerConfig, Config::class.java)
    val weekNumber = java.time.LocalDate.now().get(WeekFields.ISO.weekOfYear())
    val weekSchedule = config.special.find {
        (weekNumber == it.validBetweenWeeks[0]) || ((weekNumber >= it.validBetweenWeeks[0]) && (weekNumber <= it.validBetweenWeeks[1]))
    } ?: config.default

    println("This weeks work hours: \n$weekSchedule")
}