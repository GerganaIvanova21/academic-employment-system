package bg.example.academicemploymentsystem.type;

public enum Semester {

    SUMMER("Летен"),
    WINTER("Зимен");

    private final String displayName;

    Semester(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
