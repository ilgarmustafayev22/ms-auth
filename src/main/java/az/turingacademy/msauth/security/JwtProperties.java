package az.turingacademy.msauth.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.security.jwt")
public class JwtProperties {

    private String privateKey;
    private String publicKey;
    private String accessTokenExpiration;
    private String refreshTokenExpiration;

}

