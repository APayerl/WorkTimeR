package se.payerl;

import java.util.Objects;

public class MenuAlternative {
    private String menuPath;
    private Logic logic;
    private String description;

    public MenuAlternative(String menuPath, String description, Logic logic) {
        this.menuPath = menuPath;
        this.description = description;
        this.logic = logic;
    }

    public String getMenuPath() {
        return menuPath;
    }

    public String getDescription() {
        return description;
    }

    public Logic getLogic() {
        return logic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Objects.equals(getMenuPath(), ((MenuAlternative) o).getMenuPath());
    }
}