package bg.example.academicemploymentsystem.services.impl;

import bg.example.academicemploymentsystem.entities.Assignment;
import bg.example.academicemploymentsystem.entities.Evaluation;
import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.exceptions.ResourceNotFoundException;
import bg.example.academicemploymentsystem.exceptions.UnauthorizedException;
import bg.example.academicemploymentsystem.repositories.AssignmentRepository;
import bg.example.academicemploymentsystem.repositories.EvaluationRepository;
import bg.example.academicemploymentsystem.repositories.UserRepository;
import bg.example.academicemploymentsystem.services.EvaluationService;
import bg.example.academicemploymentsystem.type.Role;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationRepository evaluationRepository;
    private final UserRepository userRepository;
    private final AssignmentRepository assignmentRepository;

    private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

    @Autowired
    public EvaluationServiceImpl(EvaluationRepository evaluationRepository, UserRepository userRepository, AssignmentRepository assignmentRepository){
        this.evaluationRepository = evaluationRepository;
        this.userRepository = userRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public List<Evaluation> findAll() {
        logger.info("Извличане на всички атестации.");
        List<Evaluation> evaluations = evaluationRepository.findAll();
        if (evaluations.isEmpty()) {
            logger.warn("Няма налични атестации в базата данни.");
        }
        return evaluations;
    }

    @Override
    public List<Evaluation> getEvaluationsCreatedBy(Long userId) {
        logger.info("Извличане на атестации, създадени от потребител с ID {}", userId);
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "ID", userId)
        );
        return evaluationRepository.findByCreatedById(userId);
    }

    @Override
    public List<Evaluation> getEvaluationsCreatedTo(Long userId) {
        logger.info("Извличане на атестации, създадени за потребител с ID {}", userId);
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "ID", userId)
        );
        return evaluationRepository.findByCreatedToId(userId);
    }

    @Override
    public List<Evaluation> getEvaluationsByAssignment(Long assignmentId) {
        logger.info("Извличане на атестации по задача с ID {}", assignmentId);
        assignmentRepository.findById(assignmentId).orElseThrow(() ->
                new ResourceNotFoundException("Assignment", "ID", assignmentId)
        );
        return evaluationRepository.findByAssignmentId(assignmentId);
    }

    @Override
    public List<Evaluation> getEvaluationsByAssignmentAndUser(Long assignmentId, Long createdToId) {
        logger.info("Извличане на атестации по задача с ID {} и преподавател с ID {}", assignmentId, createdToId);
        assignmentRepository.findById(assignmentId).orElseThrow(() ->
                new ResourceNotFoundException("Assignment", "ID", assignmentId)
        );
        userRepository.findById(createdToId).orElseThrow(() ->
                new ResourceNotFoundException("User", "ID", createdToId)
        );
        return evaluationRepository.findByAssignmentIdAndCreatedToId(assignmentId, createdToId);
    }

    @Override
    public List<Evaluation> getEvaluationsAfterDate(Date date) {
        logger.info("Извличане на атестации, създадени след {}", date);
        return evaluationRepository.findByCreationDateAfter(date);
    }

    @Override
    public List<Evaluation> getEvaluationsBetweenDates(Date start, Date end) {
        logger.info("Извличане на атестации между {} и {}", start, end);
        return evaluationRepository.findByCreationDateBetween(start, end);
    }

    @Override
    public boolean existsEvaluationForAssignmentAndUser(Long assignmentId, Long createdToId) {
        logger.info("Проверка за съществуване на атестация по задача с ID {} и преподавател с ID {}", assignmentId, createdToId);
        assignmentRepository.findById(assignmentId).orElseThrow(() ->
                new ResourceNotFoundException("Assignment", "ID", assignmentId)
        );
        userRepository.findById(createdToId).orElseThrow(() ->
                new ResourceNotFoundException("User", "ID", createdToId)
        );
        return evaluationRepository.existsByAssignmentIdAndCreatedToId(assignmentId, createdToId);
    }

    @Override
    public List<Evaluation> getAllEvaluationsSortedByDate() {
        logger.info("Извличане на всички атестации, сортирани по дата.");
        return evaluationRepository.findAllByOrderByCreationDateDesc();
    }

    @Override
    public List<Evaluation> getEvaluationsByDegreeAndYear(String degree, Integer year) {
        logger.info("Извличане на атестации със степен на образование '{}' и година '{}'", degree, year);
        if (degree == null || year == null) {
            logger.warn("degree или year е null при getEvaluationsByDegreeAndYear");
            return List.of();
        }
        return evaluationRepository.findByDegreeAndYearOfReceipt(degree, year);
    }

    @Override
    public List<Evaluation> getEvaluationsByDateOfBirthBefore(Date date) {
        logger.info("Извличане на атестации за лица, родени преди {}", date);
        return evaluationRepository.findByDateOfBirthBefore(date);
    }

    @Override
    public List<Evaluation> getEvaluationsByDateOfBirthAfter(Date date) {
        logger.info("Извличане на атестации за лица, родени след {}", date);
        return evaluationRepository.findByDateOfBirthAfter(date);
    }

    @Override
    public List<Evaluation> getEvaluationsByUserAndPeriod(Long userId, Date from, Date to) {
        logger.info("Извличане на атестации за преподавател с ID {} между {} и {}", userId, from, to);
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Потребител", "ID", userId)
        );
        return evaluationRepository.findByCreatedByIdAndCreationDateBetween(userId, from, to);
    }

    @Override
    public List<Evaluation> getEvaluationsByUserAndYear(Long userId, Integer year) {
        logger.info("Извличане на атестации за преподавател с ID {} и година {}", userId, year);
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("Потребител", "ID", userId)
        );
        if (year == null) {
            logger.warn("Годината е null при getEvaluationsByUserAndYear");
            return List.of();
        }
        return evaluationRepository.findByCreatedToIdAndYearOfReceipt(userId, year);
    }

    @Override
    public List<Evaluation> searchEvaluations(Integer year, String degree, Long createdToId) {
        logger.info("Търсене на атестации по година: {}, степен: {}, преподавател: {}", year, degree, createdToId);
        if (createdToId != null) {
            userRepository.findById(createdToId).orElseThrow(() ->
                    new ResourceNotFoundException("Потребител", "ID", createdToId)
            );
        }
        return evaluationRepository.searchEvaluations(year, degree, createdToId);
    }

    @Override
    public Evaluation saveEvaluation(Evaluation evaluation) {
        Long creatorId = evaluation.getCreatedBy().getId();
        Long assignmentId = evaluation.getAssignment().getId();

        //аъществуване на потребител
        User creator = userRepository.findById(creatorId).orElseThrow(() ->
                new ResourceNotFoundException("User", "ID", creatorId)
        );

        //прверка за ръководител катедра
        if (!creator.getRole().name().equals("DEPARTMENT_HEAD")) {
            throw new UnauthorizedException("Само ръководител на катедра може да създава атестация.");
        }

        //проверка дали има възложена задача от ректора
        Assignment assignment = assignmentRepository.findById(assignmentId).orElseThrow(() ->
                new ResourceNotFoundException("Assignment", "ID", assignmentId)
        );


        User assignedTo = assignment.getAssignedTo();
        User assignedBy = assignment.getAssignedBy();

        if (!assignedTo.getId().equals(creatorId)) {
            throw new UnauthorizedException("Задачата не е възложена на този ръководител на катедра.");
        }

        // Проверка дали възложителят е ректор
        if (!assignedBy.getRole().name().equals("RECTOR")) {
            throw new UnauthorizedException("Задачата не е възложена от ректора.");
        }

        evaluation.setCreationDate(new Date());  // примерно да зададем дата на създаване
        return evaluationRepository.save(evaluation);
    }

    @Override
    public Evaluation updateEvaluation(Long evaluationId, Evaluation updatedEvaluation) {
        Evaluation existingEvaluation = evaluationRepository.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluation", "ID", evaluationId));

        User editor = updatedEvaluation.getCreatedBy();

        // Проверка за роля
        if (!editor.getRole().name().equals("DEPARTMENT_HEAD")) {
            throw new UnauthorizedException("Само ръководител на катедра може да създава атестация.");
        }

        // Проверка дали редакторът е създателят
        if (!existingEvaluation.getCreatedBy().getId().equals(editor.getId())) {
            throw new UnauthorizedException("Нямате права да редактирате тази атестация.");
        }

        // Проверка дали се правят неразрешени промени
        if (!existingEvaluation.getAssignment().getId().equals(updatedEvaluation.getAssignment().getId())
                || !existingEvaluation.getCreatedTo().getId().equals(updatedEvaluation.getCreatedTo().getId())) {
            throw new UnauthorizedException("Не можете да променяте задачата или преподавателя на атестацията.");
        }

        // Обновяване на позволените полета
        existingEvaluation.setDecision(updatedEvaluation.getDecision());
        existingEvaluation.setDateOfBirth(updatedEvaluation.getDateOfBirth());
        existingEvaluation.setDateOfGraduation(updatedEvaluation.getDateOfGraduation());
        existingEvaluation.setDecisionResults(updatedEvaluation.getDecisionResults());
        existingEvaluation.setName(updatedEvaluation.getName());
        existingEvaluation.setDegree(updatedEvaluation.getDegree());

        existingEvaluation.setCreationDate(new Date()); // актуализиране на датата при редакция (по избор)

        return evaluationRepository.save(existingEvaluation);
    }

    @Override
    public void deleteEvaluation(Long evaluationId, Long userId) {
        logger.info("Изтриване на атестация с ID {} от потребител с ID {}", evaluationId, userId);

        Evaluation evaluation = evaluationRepository.findById(evaluationId).orElseThrow(() ->
                new ResourceNotFoundException("Evaluation", "ID", evaluationId)
        );

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "ID", userId)
        );

        // Проверка дали потребителят е ръководител на катедра
        if (!user.getRole().equals(Role.DEPARTMENT_HEAD)) {
            throw new UnauthorizedException("Само ръководител на катедра може да изтрива атестация.");
        }

        // Проверка дали потребителят е създател на атестацията
        if (!evaluation.getCreatedBy().getId().equals(userId)) {
            throw new UnauthorizedException("Нямате права да изтриете тази атестация.");
        }

        evaluationRepository.delete(evaluation);

        logger.info("Успешно изтрита атестация с ID {}", evaluationId);
    }







}
