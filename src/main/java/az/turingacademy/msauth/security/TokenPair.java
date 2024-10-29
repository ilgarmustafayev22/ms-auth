package az.turingacademy.msauth.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenPair implements Serializable {

    public static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;

}
