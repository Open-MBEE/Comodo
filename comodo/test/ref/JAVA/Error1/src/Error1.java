import org.apache.commons.scxml.env.AbstractStateMachine;

public class Error1 extends AbstractStateMachine {
	public Error1() {
		super(Error1.class.getClassLoader().getResource("Error1.xml"));
	}

	public void TopLevel() {
		/* your visitor code goes here */
		getLog().info("Visiting state: TopLevel");
	}

	public void R1_1() {
		/* your visitor code goes here */
		getLog().info("Visiting state: R1_1");
	}

	public void R1_1_1() {
		/* your visitor code goes here */
		getLog().info("Visiting state: R1_1_1");
	}

	public void R2_1() {
		/* your visitor code goes here */
		getLog().info("Visiting state: R2_1");
	}

	public void R1() {
		/* your visitor code goes here */
		getLog().info("Visiting parallel: R1");
	}

	public void R2() {
		/* your visitor code goes here */
		getLog().info("Visiting parallel: R2");
	}

	public static void main(String[] argv) throws java.io.IOException,
			java.lang.InterruptedException {
		Error1 sm = new Error1();
		sm.resetMachine();
		/*
		sm.fireEvent("MY_EVENT");
		 */
	}
}
