package se.payerl;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
                .findAndRegisterModules()
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);

        File worktimerPath = new File(System.getProperty("user.home") + File.separator + ".worktimer");
        File worktimerConfig = new File(worktimerPath.getAbsolutePath() + File.separator + "config.yaml");

        if(!worktimerPath.exists())  System.out.println("Created directory: " + worktimerPath.mkdir());
        if(!worktimerConfig.exists()) {
            System.out.println("Created config: " + worktimerConfig.createNewFile());
            writeNewConfigFile(mapper, worktimerConfig);
        }


        Config config = mapper.readValue(worktimerConfig, Config.class);
        LocalDate currentDay = LocalDate.now();

        List<MenuAlternative> menuItems = Stream.of(
                new MenuAlternative("today", "Prints todays day and schedule.", (i) -> {
                    int dayIndex = currentDay.getDayOfWeek().getValue() - 1;
                    WorkDay day = getWeek(getWeekNumber(currentDay), config).getDays().get(dayIndex);
                    System.out.println("Todays schedule: " + (day == null ? "Off" : day.toString()));
                }),
                new MenuAlternative("tomorrow", "Prints todays day and schedule.", (i) -> {
                    LocalDate tomorrow = currentDay.plusDays(1);
                    int dayIndex = tomorrow.getDayOfWeek().getValue() - 1;
                    WorkDay day = getWeek(getWeekNumber(tomorrow), config).getDays().get((dayIndex + 1) % 7);
                    System.out.println("Tomorrows schedule: " + (day == null ? "Off" : day.toString()));
                }),
                new MenuAlternative("yesterday", "Prints yesterdays schedule.", (i) -> {
                    LocalDate yesterday = currentDay.minusDays(1);
                    int dayIndex = yesterday.getDayOfWeek().getValue() - 1;
                    WorkDay day = getWeek(getWeekNumber(yesterday), config).getDays().get((dayIndex - 1) % 7);
                    System.out.println("Yesterdays schedule: " + (day == null ? "Off" : day.toString()));
                }),
                new MenuAlternative("all", "Prints both the default schedule and the week specific ones.", (i) -> {
                    System.out.println("Standard schedule:\n" + config.getDefaultWeek() + "\n\nThis weeks schedule:\n" + getWeek(getWeekNumber(currentDay), config));
                }),
                new MenuAlternative("week-n", "Prints specified weeks schedule.", (i) -> {
                    int week = Integer.parseInt(args[i.incrementAndGet()]);
                    System.out.println("Schedule week " + week + ":\n" + getWeek(week, config));
                }),
                new MenuAlternative("this-week", "Prints this weeks schedule.", (i) -> {
                    System.out.println("Schedule week " + getWeekNumber(currentDay) + ":\n" + getWeek(getWeekNumber(currentDay), config));
                }),
                new MenuAlternative("next-week", "Prints next weeks schedule.", (i) -> {
                    System.out.println("Schedule week " + (getWeekNumber(currentDay.plusWeeks(1))) + ":\n" + getWeek(getWeekNumber(currentDay.plusWeeks(1)), config));
                }),
                new MenuAlternative("last-week", "Prints last weeks schedule.", (i) -> {
                    System.out.println("Schedule week " + getWeekNumber(currentDay.minusWeeks(1)) + ":\n" + getWeek(getWeekNumber(currentDay.minusWeeks(1)), config));
                })
        ).distinct().collect(Collectors.toList());

        for(AtomicInteger i = new AtomicInteger(0); i.get() < args.length; i.incrementAndGet()) {
            Optional<MenuAlternative> menuItem = menuItems.stream()
                    .filter(x -> args[i.get()].equalsIgnoreCase("--" + x.getMenuPath()))
                    .findFirst();
            if(menuItem.isEmpty())
                System.out.println(helpMessage(menuItems));
            else
                menuItem.get().getLogic().action(i);
        }

        if(args.length == 0) {
            System.out.println(helpMessage(menuItems));
        }
    }

    public static int getWeekNumber(LocalDate date) {
        return date.get(WeekFields.ISO.weekOfYear());
    }

    public static WorkWeek getWeek(int weekNumber, Config config) {
        return config.getSpecial().stream()
                .filter(x -> x.getValidBetweenWeeks().contains(weekNumber) || ((weekNumber >= x.getValidBetweenWeeks().get(0)) && (weekNumber <= x.getValidBetweenWeeks().get(1))))
                .map(x -> new Tuple<Integer, WorkWeek>(x.getValidBetweenWeeks().get(1) - x.getValidBetweenWeeks().get(0), x))
                .sorted(Comparator.comparingInt(Tuple::getFirst))
                .map(Tuple::getSecond)
                .findFirst()
                .orElse(config.getDefaultWeek());
    }

    public static String helpMessage(List<MenuAlternative> menuItems) {
        return """
                No valid input registered.
                                
                Available inputs are:\n""".concat(
                menuItems.stream()
                        .sorted(Comparator.comparing(MenuAlternative::getMenuPath))
                        .map(x -> "--" + x.getMenuPath() + " " + x.getDescription())
                        .collect(Collectors.joining("\n"))
        );
    }

    public static void writeNewConfigFile(ObjectMapper om, File configFile) throws IOException {
        Config config2 = new Config(
                new WorkWeek(
                        new WorkDay(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                        new WorkDay(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                        new WorkDay(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                        new WorkDay(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                        new WorkDay(LocalTime.of(8, 0), LocalTime.of(17, 0)),
                        null,
                        null
                ),
                List.of(
                        new WorkWeek(
                                new WorkDay(LocalTime.of(8, 0), LocalTime.of(16, 45)),
                                new WorkDay(LocalTime.of(8, 0), LocalTime.of(16, 45)),
                                new WorkDay(LocalTime.of(8, 0), LocalTime.of(16, 45)),
                                new WorkDay(LocalTime.of(8, 0), LocalTime.of(16, 45)),
                                new WorkDay(LocalTime.of(8, 0), LocalTime.of(15, 0)),
                                null,
                                null,
                                List.of(21, 36)
                        ),
                        new WorkWeek(List.of(29, 32))
                )
        );
        om.writeValue(configFile, config2);
    }
}