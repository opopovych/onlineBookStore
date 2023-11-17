package mate.academy.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import mate.academy.config.FieldMatch;
import org.hibernate.validator.constraints.Length;

@Data

@FieldMatch(first = "password", second = "repeatPassword",
        message = "The password fields must match")
public class UserRegistrationRequestDto {
    @NotBlank
    @Length(min = 5, max = 50)
    private String email;
    @NotBlank
    @Length(min = 8)
    private String password;
    @NotBlank
    @Length(min = 8)
    private String repeatPassword;
    @NotBlank
    @Length(min = 2, max = 50)
    private String firstName;
    @NotBlank
    @Length(min = 2, max = 50)
    private String lastName;
    private String shippingAddress;
}
