package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.type.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByEgn(String egn);

    List<User> findUserByFirstNameAndLastName(String firstName, String lastName);
    List<User> findUserByFirstNameAndMiddleNameAndLastName(String firstName, String middleName, String lastName);
    List<User> findUserByRole(Role role);


    //Търсене по катедра и роля
    List<User> findByDepartmentDepartmentName(String departmentName);
    List<User> findByRoleAndDepartmentDepartmentName(Role role, String departmentName);
    List<User> findByRoleAndDepartmentFacultyId(Role role, Long facultyId);


    //Проверка дали даден имейл съществува (при регистрация/валидация/)
    boolean existsByEmail(String email);
    boolean existsByEgn(String egn);



    // -- Частично търсене (за динамично търсене в UI) --
    /*List<User> findByFirstNameContainingIgnoreCase(String keyword);
    List<User> findByLastNameContainingIgnoreCase(String keyword);
    List<User> findByEmailContainingIgnoreCase(String keyword);*/

    /*@Query("SELECT u FROM User u " +
            "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "   OR LOWER(u.middleName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "   OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> searchByName(String name);

    List<User> findByEmailContainingIgnoreCase(String emailPart);*/


    //за комбинирано търсене по име и имейл
    @Query("SELECT u FROM User u " +
            "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "   OR LOWER(u.middleName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "   OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<User> searchByName(@Param("name") String name);

    List<User> findByEmailContainingIgnoreCase(String email);




    // -- Сортиране --
    List<User> findByDepartmentDepartmentNameOrderByLastNameAsc(String departmentName);
    List<User> findByRoleOrderByFirstNameAsc(Role role);



    List<User> findByDepartmentFacultyFacultyName(String facultyName);
    List<User> findByRoleAndDepartmentFacultyFacultyName(Role role, String facultyName);

    @Query("SELECT u FROM User u " +
            "WHERE u.role = :role " +
            "AND LOWER(u.department.faculty.facultyName) LIKE LOWER(CONCAT('%', :facultyNamePart, '%'))")
    List<User> findByRoleAndFacultyNameContainingIgnoreCase(@Param("role") Role role,
                                                            @Param("facultyNamePart") String facultyNamePart);

    @Query("SELECT u FROM User u")
    List<User> findAllIncludingDeleted();

}

