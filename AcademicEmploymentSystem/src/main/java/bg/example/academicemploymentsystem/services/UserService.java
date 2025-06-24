package bg.example.academicemploymentsystem.services;

import bg.example.academicemploymentsystem.dto.request.UpdateUserRequestDTO;
import bg.example.academicemploymentsystem.entities.User;
import bg.example.academicemploymentsystem.type.Role;

import java.util.List;

public interface UserService {

    User getUserByEmail(String email);

    User getUserByEgn(String egn);

    User getUserById(Long id);

    List<User> getAllUsers();

    boolean existsByEmail(String email);

    boolean existsByEgn(String egn);

    List<User> getUserByRole(Role role);

    List<User> getUserByRoleAndDepartment(String departmentName, Role role);

    List<User> getUserByRoleAndFacultyId(Role role, Long facultyId);

    List<User> getUserByRoleAndFacultyName(Role role, String facultyName);

    List<User> getUserByRoleAndFacultyNameContaining(Role role, String facultyNamePart);

    List<User> searchUserByName(String name);

    List<User> searchUserByEmailPart(String email);

    User saveUser(User user);

    User updateUser(Long id, UpdateUserRequestDTO userUpdateDTO);

    void deleteUserById(Long id);
}
