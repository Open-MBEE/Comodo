
import java.util.Map;

import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.invoke.Invoker;
import org.apache.commons.scxml.invoke.InvokerException;

public class JavaInvoker implements Invoker {

	String mSource;
	String parentStateId;
	SCInstance parentSCInstance;
	Thread mThread = null;
	ActivityThread myJob = null;

	@Override
	public void setParentStateId(String parentStateId) {
		this.parentStateId = parentStateId;
	}

	@Override
	public void setSCInstance(SCInstance scInstance) {
		this.parentSCInstance = scInstance;

	}

	@Override
	public void invoke(String source, Map params) throws InvokerException {

		String parts[] = source.split("/");
		System.out.println(parts[parts.length - 1]);
		this.mSource = parts[parts.length - 1];

		{
			{
				System.out.println("Error with invoke");
				System.out.println("mSource: " + this.mSource);
			};
		}

		mThread = new Thread(myJob);
		mThread.start();
		System.out.println("Done with Invoke, threadId " + mThread.getName());
	}

	@Override
	public void parentEvents(TriggerEvent[] evts) throws InvokerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void cancel() throws InvokerException {
		System.out.println("Leaving state " + parentStateId
				+ ". In Cancel for ID " + mSource);
		if (mThread != null) {
			myJob.stop(mThread);
		} else {
			System.out.println("mThread is NULL!");
		}
	}

}
