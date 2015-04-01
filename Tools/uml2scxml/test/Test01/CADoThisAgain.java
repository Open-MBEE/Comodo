import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

public class CADoThisAgain extends Action {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	private String mName;

	public CADoThisAgain() {
		super();
	}

	/**
	 * Get the name.
	 *
	 * @return Returns the name.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Set the name.
	 *
	 * @param name The name to set.
	 */
	public void setName(String name) {
		mName = name;
	}

	public void execute(final EventDispatcher evtDispatcher,
			final ErrorReporter errRep, final SCInstance scInstance,
			final Log appLog,
			@SuppressWarnings("rawtypes") final Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {
		if (appLog.isInfoEnabled()) {
			appLog.info("class: CADoThisAgain, method: execute, attribute name: "
					+ mName);
		}
	}

}
