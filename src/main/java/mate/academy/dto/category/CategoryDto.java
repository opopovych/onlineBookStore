package mate.academy.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDto {
    @NotBlank
    private String name;
    private String description;
}
