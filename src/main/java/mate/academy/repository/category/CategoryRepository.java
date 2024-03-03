package mate.academy.repository.category;

import java.util.Optional;
import lombok.NonNull;
import mate.academy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String name);

    void deleteById(@NonNull Long id);
}
