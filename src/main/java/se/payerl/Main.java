package se.payerl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

import com.fasterxml.jackson.core.JsonFactory;

public class Main {
    public static void main(String[] args) throws IOException {
        File worktimerPath = new File(System.getProperty("user.home") + File.separator + ".worktimer");
        if (!worktimerPath.exists()) System.out.println("Created directory: " + worktimerPath.mkdir());

        File worktimerConfig = new File(worktimerPath.getAbsolutePath() + File.separator + "config.yaml");

        if (!worktimerConfig.exists()) System.out.println("Created config: " + worktimerConfig.createNewFile());

        JsonFactory factory = new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER);

        ObjectMapper mapper = new ObjectMapper(factory)
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

//    val config2: Config = Config(
//        WorkWeek(
//            WorkDay(LocalTime.of(8, 0), LocalTime.of(17, 0)),
//            WorkDay(LocalTime.of(8, 0), LocalTime.of(17, 0)),
//            WorkDay(LocalTime.of(8, 0), LocalTime.of(17, 0)),
//            WorkDay(LocalTime.of(8, 0), LocalTime.of(17, 0)),
//            WorkDay(LocalTime.of(8, 0), LocalTime.of(17, 0)),
//            null,
//            null
//        ),
//        listOf(
//            WorkWeek(
//                WorkDay(LocalTime.of(8, 0), LocalTime.of(16, 45)),
//                WorkDay(LocalTime.of(8, 0), LocalTime.of(16, 45)),
//                WorkDay(LocalTime.of(8, 0), LocalTime.of(16, 45)),
//                WorkDay(LocalTime.of(8, 0), LocalTime.of(16, 45)),
//                WorkDay(LocalTime.of(8, 0), LocalTime.of(15, 0)),
//                null,
//                null,
//                listOf(21, 36)
//            )
//        )
//    )
//    mapper.writeValue(worktimerConfig, config2)

        Config config = mapper.readValue(worktimerConfig, Config.class);
        int weekNumber = LocalDate.now().get(WeekFields.ISO.weekOfYear());


        for (int i = 0; i < args.length; i++) {
            switch (args[i].toLowerCase()) {
                case "--today" -> {
                    int dayIndex = LocalDate.now().getDayOfWeek().getValue();
                    WorkDay day = getWeek(weekNumber, config).getDays().get(dayIndex);
                    System.out.println("Todays schedule: " + (day == null ? "Off" : day.toString()));
                }
                case "--all" -> System.out.println("Standard schedule:\n" + config.getDefaultWeek() + "\n\nThis weeks schedule:\n" + getWeek(weekNumber, config));
                case "--week-n" -> {
                    int week = Integer.parseInt(args[++i]);
                    System.out.println("Schedule week " + week + ":\n" + getWeek(week, config));
                }
                case "--this-week" -> {
                    System.out.println("Schedule week " + weekNumber + ":\n" + getWeek(weekNumber, config));
                }
            }
        }

        if(args.length == 0) {
            System.out.println(helpMessage());
        }
    }

    public static WorkWeek getWeek(int weekNumber, Config config) {
        return config.getSpecial().stream()
                .filter(x -> (weekNumber == x.getValidBetweenWeeks().get(0)) || ((weekNumber >= x.getValidBetweenWeeks().get(0)) && (weekNumber <= x.getValidBetweenWeeks().get(1))))
                .findFirst()
                .orElse(config.getDefaultWeek());
    }

    public static String helpMessage() {
        return """
                No valid input registered.
                
                Available inputs are:
                --all Prints both the default schedule and the week specific ones.
                --today Prints todays day and schedule.
                --week-n x Prints specified weeks schedule.
                """;
    }
}