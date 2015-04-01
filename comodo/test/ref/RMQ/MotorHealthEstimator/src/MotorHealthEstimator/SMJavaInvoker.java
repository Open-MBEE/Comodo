/*******************************************************************************
 *    ESO - European Southern Observatory
 *
 *    (c) European Southern Observatory, 2011
 *    Copyright by ESO 
 *  
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *
 * "@(#) $Id$" 
 *
 * who                when       what
 * ----------------  ----------  ----------------------------------------------
 * COMODO            -           Created.
 * 
 */

package MotorHealthEstimator;

import MotorHealthEstimator.SMActivity;

import java.util.Map;

import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.invoke.Invoker;
import org.apache.commons.scxml.invoke.InvokerException;
import org.apache.log4j.Logger;

public class SMJavaInvoker implements Invoker {

	private String mSource;
	private String mParentStateId;
	private SCInstance mParentSCInstance;
	private Thread mThread = null;
	private SMActivity mJob = null;
	private Logger mLogger = Logger.getLogger("MotorHealthEstimator");

	public String getSource() {
		return mSource;
	}

	public String getParentStateId() {
		return mParentStateId;
	}

	public SCInstance getParentSCInstance() {
		return mParentSCInstance;
	}

	public void setParentStateId(String parentStateId) {
		mParentStateId = parentStateId;
	}

	public void setSCInstance(SCInstance scInstance) {
		mParentSCInstance = scInstance;

	}

	public void invoke(String source, @SuppressWarnings("rawtypes") Map params)
			throws InvokerException {

		String parts[] = source.split("/");
		//System.out.println( parts[parts.length-1] );
		mSource = parts[parts.length - 1];

		mThread = new Thread(mJob);
		mThread.start();
		mLogger.debug("Invoked activity: " + mJob.getName() + " (threadId: "
				+ mThread.getName() + ")");
		mLogger.debug("Parent state: " + mParentStateId);

	}

	public void parentEvents(TriggerEvent[] events) throws InvokerException {

		// TODO events should be propageted to the activity

		for (int i = 0; i < events.length; i++) {
			mLogger.debug("TriggerEvent[" + i + "] = " + events[i].getName());
		}

	}

	public void cancel() throws InvokerException {
		if (mThread != null) {
			mJob.stop(mThread);
			mLogger.debug("Cancelled activity: " + mJob.getName()
					+ " (threadId: " + mThread.getName() + ")");
		} else {
			mLogger.error("ThreadId for activity " + mJob.getName()
					+ " is NULL!");
		}
		mLogger.debug("Parent state: " + mParentStateId);
	}

}
