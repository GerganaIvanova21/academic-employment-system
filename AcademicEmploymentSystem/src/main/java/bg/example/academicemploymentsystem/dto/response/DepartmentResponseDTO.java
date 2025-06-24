package bg.example.academicemploymentsystem.dto.response;

import lombok.Data;

@Data
public class DepartmentResponseDTO {
    private Long id;
    private String departmentName;
    private String facultyName;
}
