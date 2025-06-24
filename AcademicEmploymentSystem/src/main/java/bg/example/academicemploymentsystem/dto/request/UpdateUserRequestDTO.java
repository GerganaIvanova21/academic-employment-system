package bg.example.academicemploymentsystem.dto.request;

import lombok.Data;

@Data
public class UpdateUserRequestDTO {
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String password;
}
