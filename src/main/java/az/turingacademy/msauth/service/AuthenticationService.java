package az.turingacademy.msauth.service;


import az.turingacademy.msauth.security.TokenPair;
import az.turingacademy.msauth.model.request.SigninRequest;
import az.turingacademy.msauth.model.request.SignupRequest;
import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticationService {

    void signup(SignupRequest request);

    TokenPair signin(SigninRequest request);

    TokenPair refreshToken(String refreshToken, UserDetails userDetails);

    void signout(String authHeader);

}
