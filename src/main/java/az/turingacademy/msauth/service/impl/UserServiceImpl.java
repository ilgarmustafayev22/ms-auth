package az.turingacademy.msauth.service.impl;

import az.turingacademy.msauth.dao.entity.UserEntity;
import az.turingacademy.msauth.dao.repository.UserRepository;
import az.turingacademy.msauth.exception.UserNotFoundException;
import az.turingacademy.msauth.mapper.UserMapper;
import az.turingacademy.msauth.model.dto.UserDto;
import az.turingacademy.msauth.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDto findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with username " + username));
    }

    @Override
    public List<UserDto> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto findById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
    }

    @Override
    @Transactional
    public UserDto save(UserDto userDto) {
        UserEntity userEntity = userMapper.toEntity(userDto);
        return userMapper.toDto(userRepository.save(userEntity));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(final String username) {
        return userRepository.existsByUsername(username);
    }

}
