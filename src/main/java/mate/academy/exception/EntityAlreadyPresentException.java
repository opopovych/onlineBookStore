package mate.academy.exception;

import jakarta.validation.constraints.NotNull;

public class EntityAlreadyPresentException extends Throwable {
    public EntityAlreadyPresentException(@NotNull String s) {
    }
}
