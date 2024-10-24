package az.turingacademy.msauth.model.dto;

import az.turingacademy.msauth.model.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long id;

    @NotBlank
    private String fullName;

    @NotBlank
    private String username;

    @NotBlank
    private String password;
    private UserRole role;
    private LocalDateTime createdDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id.equals(userDto.id) && username.equals(userDto.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username);
    }

}
