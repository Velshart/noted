package me.mmtr.noted.service;

import me.mmtr.noted.data.Role;
import me.mmtr.noted.data.User;
import me.mmtr.noted.data.dto.UserDTO;
import me.mmtr.noted.repository.RoleRepository;
import me.mmtr.noted.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository USER_REPOSITORY;
    private final RoleRepository ROLE_REPOSITORY;
    private final PasswordEncoder PASSWORD_ENCODER;

    public UserServiceImpl(UserRepository USER_REPOSITORY, RoleRepository ROLE_REPOSITORY, PasswordEncoder PASSWORD_ENCODER) {
        this.USER_REPOSITORY = USER_REPOSITORY;
        this.ROLE_REPOSITORY = ROLE_REPOSITORY;
        this.PASSWORD_ENCODER = PASSWORD_ENCODER;
    }

    @Override
    public void saveUser(UserDTO userDTO) {
        User userToSave = new User();
        userToSave.setUsername(userDTO.getUsername());
        userToSave.setPassword(PASSWORD_ENCODER.encode(userDTO.getPassword()));

        Role userToSaveRole = ROLE_REPOSITORY.findByName("USER");

        if (userToSaveRole == null) {
            Role newRole = new Role();
            newRole.setName("USER");
            userToSaveRole = ROLE_REPOSITORY.save(newRole);
        }

        userToSave.setRoles(List.of(userToSaveRole));
    }

    @Override
    public User findUserByUsername(String username) {
        return USER_REPOSITORY.findByUsername(username);
    }

    @Override
    public List<UserDTO> findAllUsers() {
        List<User> users = USER_REPOSITORY.findAll();

        return users.stream()
                .map(this::mapUserToUserDTO)
                .toList();
    }

    private UserDTO mapUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());

        return userDTO;
    }
}
