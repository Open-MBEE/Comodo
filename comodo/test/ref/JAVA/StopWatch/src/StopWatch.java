import org.apache.commons.scxml.env.AbstractStateMachine;

public class StopWatch extends AbstractStateMachine {
	public StopWatch() {
		super(StopWatch.class.getClassLoader().getResource("StopWatch.xml"));
	}

	public void reset() {
		/* your visitor code goes here */
		getLog().info("Visiting state: reset");
	}

	public void running() {
		/* your visitor code goes here */
		getLog().info("Visiting state: running");
	}

	public void stopped() {
		/* your visitor code goes here */
		getLog().info("Visiting state: stopped");
	}

	public void paused() {
		/* your visitor code goes here */
		getLog().info("Visiting state: paused");
	}

	public static void main(String[] argv) throws java.io.IOException,
			java.lang.InterruptedException {
		StopWatch sm = new StopWatch();
		sm.resetMachine();
		/*
		sm.fireEvent("MY_EVENT");
		 */
	}
}
