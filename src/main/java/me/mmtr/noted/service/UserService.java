package me.mmtr.noted.service;

import me.mmtr.noted.data.User;
import me.mmtr.noted.data.dto.UserDTO;

import java.util.List;

public interface UserService {
    void saveUser(UserDTO userDTO);

    User findUserByUsername(String username);

    List<UserDTO> findAllUsers();
}
