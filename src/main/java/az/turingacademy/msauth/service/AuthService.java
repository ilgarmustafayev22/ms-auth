package az.turingacademy.msauth.service;


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
import az.turingacademy.msauth.util.TokenUtil;
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
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtCreator jwtCreator;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authManager;
    private final RedisTemplate<String, String> redisTemplate;

    public void signup(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername()))
            throw new UsernameAlreadyExistsException("User with username " + signupRequest.getUsername() + " already exists");

        var user = UserEntity.builder()
                .fullName(signupRequest.getFullName())
                .username(signupRequest.getUsername())
                .password(encoder.encode(signupRequest.getPassword()))
                .role(UserRole.USER)
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
    }

    public TokenPair signin(SigninRequest signinRequest) {
        Authentication authenticate = authManager.authenticate
                (
                        new UsernamePasswordAuthenticationToken
                                (
                                        signinRequest.getUsername(),
                                        signinRequest.getPassword()
                                )
                );
        return getTokenPair(authenticate);
    }

    public void signout(String authHeader) {
        String jwtToken = TokenUtil.extractToken(authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException();
        }

        final String username = jwtCreator.extractUsername(jwtToken);
        String refreshToken = redisTemplate.opsForValue().get(username);

        redisTemplate.delete(username);
    }

    public TokenPair refreshToken(String refreshToken) {
        if (jwtCreator.isTokenExpired(refreshToken))
            throw new InvalidTokenException();

        final Authentication authentication = jwtCreator.parseAuthentication(refreshToken);
        final TokenPair newTokenPair = getTokenPair(authentication);
        redisTemplate.opsForValue().set
                (
                        authentication.getName(),
                        newTokenPair.getRefreshToken(),
                        24, TimeUnit.HOURS
                );
        return newTokenPair;
    }

    private TokenPair getTokenPair(Authentication authentication) {
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
