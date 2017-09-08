import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;


enum Test {
	@StartState
	START {
		public Test run() {
			System.out.println("ending");
			return END;
		}
	}, END;

	@StateMachine(methodType = StateMachine.MethodType.INIT)
	public void init() {System.out.println("intitializing");}


	@StateMachine(methodType = StateMachine.MethodType.RUN)
	public Test run() {
		System.out.println("starting");
		return END;
	}

	@StateMachine(methodType = StateMachine.MethodType.END)
	public void end() {
		System.out.println("The END!!");
	}
}

public class StateMachineRunner {

	public synchronized static <T extends Enum> void run(Class enumStateMachine, String startMethod, String runMethod, String endMethodName, T state) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
		assertIfMethodsExists(enumStateMachine, startMethod, runMethod, endMethodName);
		assertIfRunMethodReturnsItself(state, runMethod);

		T previousState = null;
		Method[] methods= null;

//		TODO improve loop to be more robust
		for (;;)
		{
			if (state != previousState)
			{
				if (previousState != null)
					endState(previousState , methods);

				methods = getMethods(state, startMethod, runMethod, endMethodName);
				initState(state, methods);
			}

			previousState = state;
			state = runState(state, methods);

		}
	}

	/**
	 * Method that is run to run the method in the state to end the current state. Runs the method[2] with zero parameters
	 * @param state The current state
	 * @param methods The start, run and end methods of the state
	 */
	private static <T extends Enum> void endState(T state, Method[] methods) throws InvocationTargetException, IllegalAccessException {
		methods[2].invoke(state);
	}


	/**
	 * Method that is run to run the method in the state that is meant to be contiously called unitl redirected to the next state. Runs the method[1] with zero parameters and returns its output
	 * @param state The current state
	 * @param methods The start, run and end methods of the state
	 * @return T Returns what the next state to be run should be
	 */
	private static <T extends Enum> T runState(T state, Method[] methods) throws InvocationTargetException, IllegalAccessException {
		return (T) methods[1].invoke(state);
	}

	/**
	 * Method that is run to run the method in the state to initialize the current state
	 * @param state The current state
	 * @param methods The start, run and end methods of the state
	 */
	private static  <T extends Enum> void initState(T state, Method[] methods) throws InvocationTargetException, IllegalAccessException {
		methods[0].invoke(state);
	}

	/**
	 * Given the Enum state the method will find the Methods with the gievn method names and return them as an array
	 * @param state The class to look for the method
	 * @param methodNames the names of the methods that are meant to be returned
	 * @return an array of Method corresponding to methodNames
	 */
	private static <T extends  Enum> Method[] getMethods(T state, String... methodNames)
	{
		Method[] methods = new Method[methodNames.length];

		for (int i = 0; i < methods.length; i++)
		{
			for (Method method:state.getClass().getMethods())
			{
				if (method.getName().equals(methodNames[i]))
				{
					methods[i] = method;
					break;
				}

			}
		}

		return methods;
	}

	/**
	 * Checks that the method run in the state enum is capable of returning a next state
	 * @param tClass
	 * @param runMethodName
	 * @param <T>
	 * @throws NoSuchMethodException
	 */
	private static <T extends Enum> void assertIfRunMethodReturnsItself(T tClass, String runMethodName) throws NoSuchMethodException {
			Class<?> returnValueOfMethod = tClass.getClass().getMethod(runMethodName).getReturnType();

			if (!returnValueOfMethod.isInstance(tClass))
				throw new IllegalArgumentException("The " + runMethodName + " method in " + tClass.getClass().getName() + " should return an instance of " + tClass.getClass().getName() + " not " + returnValueOfMethod.getName());

	}

	/**
	 * Checks if the needed methods are avaliable in the Enum
	 * @param tClass
	 * @param methodNames
	 */
	private static void assertIfMethodsExists(Class tClass, String... methodNames)
	{
		ArrayList<String> absentMethodNames = new ArrayList<>();

		Method[] methods = tClass.getMethods();

		for (String name: methodNames)
		{
			boolean hasName = false;

			for (Method method:methods)
			{
				if (method.getName().equals(name))
				{
					hasName = true;
					break;
				}

			}

			if (!hasName)
				absentMethodNames.add(name);
		}

		if (absentMethodNames.size() > 0)
			throw new IllegalArgumentException("You have not included the " + absentMethodNames.toString() + " method" + (absentMethodNames.size() > 1? "s": "") + " in " + tClass.getClass().getName());

	}

	public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

//		run(Test.class,"init", "run", "end", Test.START);

	}


}