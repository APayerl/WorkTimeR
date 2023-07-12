package se.payerl;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

import java.beans.ConstructorProperties;
import java.util.List;

public class Config {
    @JsonProperty("default")
    private WorkWeek defaultWeek;
    private List<WorkWeek> special;

    @ConstructorProperties({"defaultWeek", "special"})
    public Config(
            @NotNull @JsonProperty("default") WorkWeek defaultWeek,
            @NotNull List<WorkWeek> special) {
        this.defaultWeek = defaultWeek;
        this.special = special;
    }

    @NotNull
    public WorkWeek getDefaultWeek() {
        return defaultWeek;
    }

    @NotNull
    public List<WorkWeek> getSpecial() {
        return special;
    }
}