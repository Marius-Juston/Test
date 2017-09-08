import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE_USE)
public @interface LambdaStartState {
	boolean bool() default true;
}

interface BooleanExpression {
	boolean returnBoolean();
}
