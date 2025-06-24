package bg.example.academicemploymentsystem.type;

public enum HourType {
    LECTURE("Лекция"),
    LABORATORY_EXERCISE("Лабораторно упражнение"),
    SEMINAR_EXERCISE("Семинарно упражнение"),
    STUDY_PRACTICE("Учебна практика"),
    SPECIALIZED_SPORTS_TRAINING("Специализирана спортна подготовка");

    private final String displayName;

    HourType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

