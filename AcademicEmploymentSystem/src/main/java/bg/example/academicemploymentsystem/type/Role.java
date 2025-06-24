package bg.example.academicemploymentsystem.type;

public enum Role {

    ADMIN("Администратор"),
    RECTOR("Ректор"),
    VICE_RECTOR("Заместник ректор"),
    DEAN("Декан"),
    VICE_DEAN("Заместник декан"),
    DEPARTMENT_HEAD("Ръководител катедра"),
    TEACHER("Преподавател"),
    SECRETARY("Секретар на факултет"),
    TECHNICAL_CONTRACTOR("Технически изпълнител");


    private final String displayName;

    Role (String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }


}
