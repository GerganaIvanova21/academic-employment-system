package bg.example.academicemploymentsystem.dto.request;

import lombok.Data;

@Data
public class DepartmentRequestDTO {
    private String departmentName;
    private Long facultyId;
}
