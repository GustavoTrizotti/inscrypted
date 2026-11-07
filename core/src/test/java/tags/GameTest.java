package tags;

import org.junit.jupiter.api.Tag;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Tag("game")
public @interface GameTest {
}
