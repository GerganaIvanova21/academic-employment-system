package bg.example.academicemploymentsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "evaluation")
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "academic_activity")
    private String academicActivity;

    @Basic
    @Column(name = "year_of_receipt")
    private Integer yearOfReceipt;

    @Basic
    @Column(name = "degree")
    private String degree;

    @Basic
    @Column(name = "date_of_graduation")
    private Integer dateOfGraduation;

    @Basic
    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Basic
    @Column(name = "internship")
    private Integer internship;

    @Basic
    @Column(name = "teaching_experience")
    private Integer teachingExperience;

    @Basic
    @Column(name = "education_methodological_activities")
    private String educationMethodologicalActivities;

    @Basic
    @Column(name = "research_activities")
    private String researchActivities;

    @Basic
    @Column(name = "qualification_administrative_other_activities")
    private String qualificationAdministrativeOtherActivities;

    @Basic
    @Column(name = "assessment_from_colleagues")
    private String assessmentFromColleagues;

    @Basic
    @Column(name = "decision")
    private String decision;

    @Basic
    @Column(name = "recommendation")
    private String recommendation;

    @Basic
    @Column(name = "education_methodological_activities_results")
    private String educationMethodologicalActivitiesResults;

    @Basic
    @Column(name = "research_activities_results")
    private String researchActivitiesResults;

    @Basic
    @Column(name = "decision_results")
    private String decisionResults;

    @Basic
    @Column(name = "recommendation_results")
    private String recommendationResults;

    @Basic
    @Column(name = "creation_date")
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_to", nullable = false)
    private User createdTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id")
    private Assignment assignment;








}
