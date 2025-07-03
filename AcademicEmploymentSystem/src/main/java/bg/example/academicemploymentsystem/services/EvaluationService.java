package bg.example.academicemploymentsystem.services;

import bg.example.academicemploymentsystem.entities.Evaluation;

import java.util.Date;
import java.util.List;

public interface EvaluationService {

    List<Evaluation> findAll();

    Evaluation saveEvaluation(Evaluation evaluation);

    Evaluation updateEvaluation(Long id, Evaluation updatedEvaluation);

    void deleteEvaluation(Long evaluationId, Long userId);

    List<Evaluation> getEvaluationsCreatedBy(Long userId);

    List<Evaluation> getEvaluationsCreatedTo(Long userId);

    List<Evaluation> getEvaluationsByAssignment(Long assignmentId);

    List<Evaluation> getEvaluationsByAssignmentAndUser(Long assignmentId, Long createdToId);

    List<Evaluation> getEvaluationsAfterDate(Date date);

    List<Evaluation> getEvaluationsBetweenDates(Date start, Date end);

    boolean existsEvaluationForAssignmentAndUser(Long assignmentId, Long createdToId);

    List<Evaluation> getAllEvaluationsSortedByDate();

    List<Evaluation> getEvaluationsByDegreeAndYear(String degree, Integer year);

    List<Evaluation> getEvaluationsByDateOfBirthBefore(Date date);

    List<Evaluation> getEvaluationsByDateOfBirthAfter(Date date);

    List<Evaluation> getEvaluationsByUserAndPeriod(Long userId, Date from, Date to);

    List<Evaluation> getEvaluationsByUserAndYear(Long userId, Integer year);

    List<Evaluation> searchEvaluations(Integer year, String degree, Long createdToId);

   /* Evaluation saveEvaluation(Evaluation evaluation);

    void deleteEvaluation(Long id);

    List<Evaluation> getByCreatedById(Long userId);

    List<Evaluation> getByCreatedToId(Long userId);

    List<Evaluation> getByAssignmentId(Long assignmentId);

    List<Evaluation> getByAssignmentIdAndCreatedToId(Long assignmentId, Long createdToId);

    boolean existsByAssignmentIdAndCreatedToId(Long assignmentId, Long createdToId);

    List<Evaluation> getByCreationDateAfter(Date date);

    List<Evaluation> getAllSortedByCreationDateDesc();

    List<Evaluation> getByDegreeAndYear(String degree, Integer year);

    List<Evaluation> getByCreatedToAndYear(Long userId, Integer year);

    List<Evaluation> searchEvaluations(Integer year, String degree, Long createdToId);

    */
}
