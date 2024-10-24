package az.turingacademy.msauth.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(max = 50)
    private String fullName;

    @NotBlank
    private String username;

    @NotBlank
    @Size(max = 10, min = 8)
    private String password;

    @Override
    public String toString() {
        return new StringBuilder("SignupRequest{")
                .append("fullName='").append(fullName).append('\'')
                .append(", username='").append(username).append('\'')
                .append(", password='").append("******").append('\'')
                .append('}')
                .toString();
    }

}
