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

package alma.EventSubscriber;

import java.util.Map;

import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.invoke.Invoker;
import org.apache.commons.scxml.invoke.InvokerException;

public class SMJavaInvoker implements Invoker {

	String mSource;
	String parentStateId;
	SCInstance parentSCInstance;
	Thread mThread = null;
	SMActivity myJob = null;

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
	public void parentEvents(TriggerEvent[] events) throws InvokerException {
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
