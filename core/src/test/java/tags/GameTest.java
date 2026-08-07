package tags;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Tag("game")
public @interface GameTest {
}
