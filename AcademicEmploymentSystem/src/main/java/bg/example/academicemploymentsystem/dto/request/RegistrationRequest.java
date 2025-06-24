package bg.example.academicemploymentsystem.dto.request;

import bg.example.academicemploymentsystem.type.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationRequest {
    private String email;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;
    private String egn;
    private Role role;
    private Long departmentId;
}
