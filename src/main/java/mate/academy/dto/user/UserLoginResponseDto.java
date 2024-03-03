package mate.academy.dto.user;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class UserLoginResponseDto {
    private String token;
}
