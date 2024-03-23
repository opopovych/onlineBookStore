package mate.academy.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserLoginRequestDto {
    @NotEmpty
    @Length(min = 4, max = 50)
    @Email
    private String email;
    @NotEmpty
    @Length(min = 4, max = 50)
    private String password;
}
