package se.payerl;

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
}
