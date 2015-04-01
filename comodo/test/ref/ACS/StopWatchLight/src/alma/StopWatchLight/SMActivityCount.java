/*******************************************************************************
 *    ALMA - Atacama Large Millimiter Array
 *
 *    (c) European Southern Observatory, 2002
 *    Copyright by ESO (in the framework of the ALMA collaboration)
 *    and Cosylab 2002, All rights reserved
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
 * COMODO                        Created.
 * 
 */

package alma.StopWatchLight;

import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.model.ModelException;

public class SMActivityCount extends SMActivity {

	private volatile boolean mIsRunning;

	public SMActivityCount(String parentStateId, SCInstance parentSCInstance) {
		super(parentStateId, parentSCInstance);
		mIsRunning = false;
	}

	@Override
	//Starting the thread
	public void run() {

		//JUST FOR TESTING
		System.out.println("Starting with Count");
		mIsRunning = true;
		try {
			while (mIsRunning) {
				// TODO: Put your implementation for the Activity
				//JUST FOR TESTING
				for (int i = 0; i < 20; ++i) {
					Thread.sleep(100);
					System.out.print(".");
				}
				break;
				//JUST FOR TESTING
			}
			// Special *.done message for the parent state
			System.out.println("Done with Count... Sending message \"\"");
			mParentSCInstance.getExecutor().triggerEvent(
					new TriggerEvent("", TriggerEvent.SIGNAL_EVENT, null));
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		} catch (ModelException me) {
			me.printStackTrace();
		}
	}

	@Override
	public void stop(Thread threadId) {
		// Boolean used to stop the threads
		mIsRunning = false;
		System.out.println("Cancel threadId " + threadId.getName());
	}

}
