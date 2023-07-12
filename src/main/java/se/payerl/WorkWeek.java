package se.payerl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;
import java.util.List;
import java.util.stream.Stream;

@JsonPropertyOrder("validBetweenWeeks")
public class WorkWeek{
    private WorkDay monday = null;
    private WorkDay tuesday = null;
    private WorkDay wednesday = null;
    private WorkDay thursday = null;
    private WorkDay friday = null;
    private WorkDay saturday = null;
    private WorkDay sunday = null;
    private List<Integer> validBetweenWeeks;

    @ConstructorProperties({"monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday", "validBetweenWeeks"})
    public WorkWeek(
            WorkDay monday,
            WorkDay tuesday,
            WorkDay wednesday,
            WorkDay thursday,
            WorkDay friday,
            WorkDay saturday,
            WorkDay sunday,
            List<Integer> validBetweenWeeks) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.validBetweenWeeks = validBetweenWeeks;
    }

    public WorkWeek(
            WorkDay monday,
            WorkDay tuesday,
            WorkDay wednesday,
            WorkDay thursday,
            WorkDay friday,
            WorkDay saturday,
            WorkDay sunday) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.validBetweenWeeks = List.of();
    }

    public WorkWeek(List<Integer> validBetweenWeeks) {
        this.validBetweenWeeks = validBetweenWeeks;
    }

    public WorkDay getMonday() {
        return monday;
    }

    public WorkDay getTuesday() {
        return tuesday;
    }

    public WorkDay getWednesday() {
        return wednesday;
    }

    public WorkDay getThursday() {
        return thursday;
    }

    public WorkDay getFriday() {
        return friday;
    }

    public WorkDay getSaturday() {
        return saturday;
    }

    public WorkDay getSunday() {
        return sunday;
    }

    @JsonIgnore
    public List<WorkDay> getDays() {
        return Stream.of(monday, tuesday, wednesday, thursday, friday, saturday, sunday).toList();
    }

    @NotNull
    public List<Integer> getValidBetweenWeeks() {
        return validBetweenWeeks;
    }

    private String rowFormatting(WorkDay day) {
        return day == null ? "Off" : day.toString();
    }

    @Override
    public String toString() {
        return "Monday: " + rowFormatting(monday) + "\n" +
                "Tuesday: " + rowFormatting(tuesday) + "\n" +
                "Wednesday: " + rowFormatting(wednesday) + "\n" +
                "Thursday: " + rowFormatting(thursday) + "\n" +
                "Friday: " + rowFormatting(friday) + "\n" +
                "Saturday: " + rowFormatting(saturday) + "\n" +
                "Sunday: " + rowFormatting(sunday);
    }
}