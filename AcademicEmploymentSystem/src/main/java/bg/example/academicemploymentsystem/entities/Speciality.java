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
@Table(name = "speciality")
public class Speciality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
    @Column(name = "name")
    private String specialityName;


    @OneToMany(mappedBy = "speciality", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Course> courses = new HashSet<>();
}
