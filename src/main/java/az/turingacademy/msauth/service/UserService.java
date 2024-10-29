package az.turingacademy.msauth.service;

import az.turingacademy.msauth.dao.entity.UserEntity;
import az.turingacademy.msauth.dao.repository.UserRepository;
import az.turingacademy.msauth.exception.UserNotFoundException;
import az.turingacademy.msauth.mapper.UserMapper;
import az.turingacademy.msauth.model.dto.UserDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with username " + username));
    }

    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }

    @Transactional
    public UserDto save(UserDto userDto) {
        UserEntity userEntity = userMapper.toEntity(userDto);
        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public boolean existsByUsername(final String username) {
        return userRepository.existsByUsername(username);
    }

}
