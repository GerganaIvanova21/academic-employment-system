package bg.example.academicemploymentsystem.dto.response;

import bg.example.academicemploymentsystem.type.Semester;
import lombok.Data;

@Data
public class CourseResponseDTO {
    private Long id;
    private String courseName;
    private Semester semester;
    private String specialityName;
    private String teacherName;
}
