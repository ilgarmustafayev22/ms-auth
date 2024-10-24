package az.turingacademy.msauth.service.impl;


import az.turingacademy.msauth.dao.entity.UserEntity;
import az.turingacademy.msauth.dao.repository.UserRepository;
import az.turingacademy.msauth.exception.InvalidTokenException;
import az.turingacademy.msauth.exception.UsernameAlreadyExistsException;
import az.turingacademy.msauth.security.jwt.JwtCreator;
import az.turingacademy.msauth.security.TokenPair;
import az.turingacademy.msauth.model.enums.TokenType;
import az.turingacademy.msauth.model.enums.UserRole;
import az.turingacademy.msauth.model.request.SigninRequest;
import az.turingacademy.msauth.model.request.SignupRequest;
import az.turingacademy.msauth.service.AuthenticationService;
import az.turingacademy.msauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtCreator jwtCreator;
    private final PasswordEncoder encoder;
    private final UserService userService;
    private final UserRepository repository;
    private final AuthenticationManager authManager;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void signup(SignupRequest signupRequest) {
        if (userService.existsByUsername(signupRequest.getUsername()))
            throw new UsernameAlreadyExistsException("User with username " + signupRequest.getUsername() + " already exists");

        var user = UserEntity.builder()
                .fullName(signupRequest.getFullName())
                .username(signupRequest.getUsername())
                .password(encoder.encode(signupRequest.getPassword()))
                .role(UserRole.USER)
                .createdDate(LocalDateTime.now())
                .build();

        repository.save(user);
    }

    @Override
    public TokenPair signin(SigninRequest signinRequest) {
        Authentication authenticate = authManager.authenticate
                (
                        new UsernamePasswordAuthenticationToken(
                                signinRequest.getUsername(),
                                signinRequest.getPassword()
                        )
                );
        return getTokenPair(authenticate);
    }

    @Override
    public void signout(String authHeader) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String refreshToken = redisTemplate.opsForValue().get(authentication.getName());

        if (refreshToken == null)
            throw new InvalidTokenException();

        redisTemplate.delete(authentication.getName());
    }

    @Override
    public TokenPair refreshToken(String refreshToken, UserDetails userDetails) {
        jwtCreator.isTokenValid(refreshToken, userDetails);
        final Authentication authentication = jwtCreator.parseAuthentication(refreshToken, userDetails);
        final TokenPair newTokenPair = getTokenPair(authentication);
        redisTemplate.opsForValue().set
                (
                        authentication.getName(),
                        newTokenPair.getRefreshToken(),
                        24, TimeUnit.HOURS
                );
        return newTokenPair;
    }

    protected TokenPair getTokenPair(Authentication authentication) {
        TokenPair tokenResponse = new TokenPair();
        tokenResponse.setAccessToken(jwtCreator.generateToken(authentication, TokenType.ACCESS));
        tokenResponse.setRefreshToken(jwtCreator.generateToken(authentication, TokenType.REFRESH));

        redisTemplate.opsForValue().set
                (
                        authentication.getName(),
                        tokenResponse.getRefreshToken(),
                        24, TimeUnit.HOURS
                );
        return tokenResponse;
    }

}
