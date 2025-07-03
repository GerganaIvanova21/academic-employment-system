package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {

    // Всички оценки, създадени от определен потребител
    List<Evaluation> findByCreatedById(Long userId);

    // Всички оценки, създадени за определен потребител
    List<Evaluation> findByCreatedToId(Long userId);

    // Оценки по assignment
    List<Evaluation> findByAssignmentId(Long assignmentId);

    // Оценки по assignment + създадени за конкретен човек
    List<Evaluation> findByAssignmentIdAndCreatedToId(Long assignmentId, Long createdToId);

    // Оценки създадени след дадена дата
    List<Evaluation> findByCreationDateAfter(Date date);

    // Оценки създадени между дати
    List<Evaluation> findByCreationDateBetween(Date start, Date end);

    // Проверка дали има вече направена оценка по assignment и за конкретен човек
    boolean existsByAssignmentIdAndCreatedToId(Long assignmentId, Long createdToId);

    // Изтегляне на всички оценки, сортирани по дата на създаване
    List<Evaluation> findAllByOrderByCreationDateDesc();

    //Филтриране по степен и година
    List<Evaluation> findByDegreeAndYearOfReceipt(String degree, Integer yearOfReceipt);


    //атестации по дата на раждане
    List<Evaluation> findByDateOfBirthBefore(Date date);  // напр. родени преди 1980
    List<Evaluation> findByDateOfBirthAfter(Date date);

    //Всички атестации, създадени от потребител в даден интервал от време
    List<Evaluation> findByCreatedByIdAndCreationDateBetween(Long userId, Date from, Date to);


    //Намиране на оценки по преподавател и година
    List<Evaluation> findByCreatedToIdAndYearOfReceipt(Long userId, Integer year);

    //Филтриране по година, степен и преподавател
    @Query("SELECT e FROM Evaluation e WHERE " +
            "(:year IS NULL OR e.yearOfReceipt = :year) AND " +
            "(:degree IS NULL OR e.degree = :degree) AND " +
            "(:createdToId IS NULL OR e.createdTo.id = :createdToId)")
    List<Evaluation> searchEvaluations(@Param("year") Integer year,
                                       @Param("degree") String degree,
                                       @Param("createdToId") Long createdToId);
}
