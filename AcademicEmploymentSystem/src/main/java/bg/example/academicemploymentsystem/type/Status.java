package bg.example.academicemploymentsystem.type;

public enum Status {
    PENDING("Предстояща"),
    IN_PROGRESS("В процес"),
    COMPLETED("Завършена"),
    REJECTED("Отхвърлена"),
    CANCELLED("Анулирана"),
    OVERDUE("Просрочена");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
