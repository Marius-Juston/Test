import java.lang.annotation.*;


@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface StateMachine {
	/**
	 * Returns the method type for the state machine.
	 *
	 * @return the state machine
	 */
	MethodType methodType();

	enum MethodType {
		RUN, INIT, END
	}

}

