package bg.example.academicemploymentsystem.entities;

import bg.example.academicemploymentsystem.type.HourType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "hour")
public class Hour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "start_time")
    private LocalTime startTime;

    @Basic
    @Column(name = "end_time")
    private LocalTime endTime;

    @Basic
    @Column(name = "day")
    private LocalDate day;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private HourType type;

    @Basic
    @Column(name = "room")
    private String room;

    @Basic
    @Column(name = "group")
    private String group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;




}
