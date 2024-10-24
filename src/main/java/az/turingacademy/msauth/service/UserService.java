package az.turingacademy.msauth.service;


import az.turingacademy.msauth.model.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto findUserByUsername(String username);

    List<UserDto> findAllUsers();

    UserDto findById(Long id);

    UserDto save(UserDto userDto);

    void deleteUser(Long id);

    boolean existsByUsername(String username);

}
