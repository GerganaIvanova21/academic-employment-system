package bg.example.academicemploymentsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "faculty")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "faculty_id")
    private Long id;

    @Basic
    @Column(name = "name")
    private String facultyName;

    @OneToMany(mappedBy = "faculty",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Department> departments = new HashSet<>();
}
