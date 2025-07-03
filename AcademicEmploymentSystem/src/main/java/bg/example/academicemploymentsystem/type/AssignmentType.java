package bg.example.academicemploymentsystem.type;

public enum AssignmentType {
    REPORT ("Доклад"),
    CERTIFICATION ("Атестация"),
    TEACHING_PLAN("График за занятия");

    private final String displayName;

    AssignmentType (String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

