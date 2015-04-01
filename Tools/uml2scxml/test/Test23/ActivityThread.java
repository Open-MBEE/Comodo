import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.model.ModelException;

// Generic class for activity threads 

public class ActivityThread implements Runnable {

	String mParentStateId;
	SCInstance mParentSCInstance;
	volatile boolean mIsRunning;

	public ActivityThread(String parentStateId, SCInstance parentSCInstance) {
		mParentStateId = parentStateId;
		mParentSCInstance = parentSCInstance;
		mIsRunning = false;
	}

	@Override
	public void run() {
		System.out.println("Generic run() implementation in ActivityThread");
		mIsRunning = true;
		try {
			while (mIsRunning) {

				//Implementation of a generic run()
				Thread.sleep(1000);
				System.out.print(".");

			}
			// Special *.invoke.done message for the parent state
			System.out
					.println("Done with Thread.. Sending message to parent state ("
							+ mParentStateId + ")");
			mParentSCInstance.getExecutor().triggerEvent(
					new TriggerEvent(mParentStateId + ".invoke.done",
							TriggerEvent.SIGNAL_EVENT));
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} catch (ModelException me) {
			me.printStackTrace();
		}
	}

	public void stop(Thread threadId) {
		// Boolean used to stop the threads
		mIsRunning = false;
		System.out.println("Cancel threadId " + threadId.getName());
	}
}
