package az.turingacademy.msauth.controller;


import az.turingacademy.msauth.security.TokenPair;
import az.turingacademy.msauth.model.request.SigninRequest;
import az.turingacademy.msauth.model.request.SignupRequest;
import az.turingacademy.msauth.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignupRequest signupRequest) {
        authenticationService.signup(signupRequest);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenPair> signin(@Valid @RequestBody SigninRequest signinRequest) {
        return ResponseEntity.ok(authenticationService.signin(signinRequest));
    }

    @Operation(summary = "Use refresh token to get new access token")
    @PostMapping("/refresh")
    public ResponseEntity<TokenPair> refreshToken(@AuthenticationPrincipal UserDetails userDetails, String refreshToken) {
        return ResponseEntity.ok(authenticationService.refreshToken(refreshToken, userDetails));
    }

    @GetMapping("/signout")
    public ResponseEntity<Void> signout(@RequestHeader("Authorization") @NotBlank String authorizationHeader) {
        authenticationService.signout(authorizationHeader);
        return ResponseEntity.ok().build();
    }

}
