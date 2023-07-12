package se.payerl;

import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;
import java.time.LocalTime;

public class WorkDay {
    private final LocalTime start;
    private final LocalTime end;

    @ConstructorProperties({"start", "end"})
    public WorkDay(@NotNull LocalTime start, @NotNull LocalTime end) {
        this.start = start;
        this.end = end;
    }

    @NotNull
    public LocalTime getStart() {
        return start;
    }

    @NotNull
    public LocalTime getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return start + " - " + end;
    }
}