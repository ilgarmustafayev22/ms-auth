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
    @Size(min = 8, max = 10)
    private String password;

    @Override
    public String toString() {
        return "{fullName='%s', username='%s', password='******'}"
                .formatted(fullName, username);
    }

}
