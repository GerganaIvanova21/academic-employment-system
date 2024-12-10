package bg.example.academicemploymentsystem.repositories;

import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.type.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findUserById(Integer id);
    Optional<User> findByEmail(String email);

    List<User> findUserByFirstNameAndLastName(String firstName, String lastName);

    List<User> findUserByRole(Role role);







}
