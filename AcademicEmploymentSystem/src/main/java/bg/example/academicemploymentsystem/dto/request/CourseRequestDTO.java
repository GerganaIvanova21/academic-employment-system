package bg.example.academicemploymentsystem.dto.request;

import bg.example.academicemploymentsystem.type.Semester;
import lombok.Data;

@Data
public class CourseRequestDTO {
    private String courseName;
    private Semester semester;
    private Long specialityId;
}
