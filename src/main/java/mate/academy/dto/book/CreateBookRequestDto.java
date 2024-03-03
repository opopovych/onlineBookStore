package mate.academy.dto.book;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Set;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Data
public class CreateBookRequestDto {
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    @ISBN(type = ISBN.Type.ANY)
    private String isbn;
    private String description;
    private String coverImage;
    private Set<Long> categoryIds;
}
