package bg.example.academicemploymentsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "title")
    private String title;

    @Basic
    @Column(name = "created_at")
    private Date createdAt;

    @Basic
    @Column(name = "report_text")
    private String reportText;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Hour> hours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prepared_by", nullable = false)
    private User preparedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prepared_to", nullable = false)
    private User preparedTo;
}
