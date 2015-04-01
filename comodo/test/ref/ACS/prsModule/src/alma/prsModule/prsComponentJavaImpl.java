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

package alma.prsModule;

import alma.ACS.*;
import alma.ACS.ComponentStates;
import java.util.logging.Level;

import alma.ACS.jbaci.*;
import alma.ACS.impl.*;

import alma.ACSErrTypeCommon.CouldntPerformActionEx;
import alma.ACSErrTypeCommon.wrappers.AcsJCouldntPerformActionEx;

import alma.prsModule.*;

import alma.acs.nc.SimpleSupplier;
import alma.acs.nc.Consumer;

import alma.prsModule.CHANNEL_PRSINTERFACE_P2;
import alma.prsModule.CHANNEL_PRSINTERFACE_P1;

/**
 * Developer's file to implement the ComponentAbstract
 * @see alma.prsModule.ComponentAbstract
 * @author ACS Component Code Generator
 * @version $Id$
 */
public class prsComponentJavaImpl extends prsComponentJavaImplAbstract {
	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////
	@Override
	public void preInitialize() {
		// TODO
	}

	@Override
	public void postInitialize() {
		// TODO
	}

	@Override
	public void preExecute() {
		// TODO
	}

	@Override
	public void postExecute() {
		// TODO
	}

	@Override
	public void preCleanUp() {
		// TODO
	}

	@Override
	public void postCleanUp() {
		// TODO
	}

	@Override
	public void preAboutToAbort() {
		// TODO
	}

	@Override
	public void postAboutToAbort() {
		// TODO
	}

	/////////////////////////////////////////////////////////////
	// Implementation of Notification Channel Operations
	/////////////////////////////////////////////////////////////

	@Override
	public void sendEvent() throws CouldntPerformActionEx {
		try {
			// TODO
			// e.g.:
			// myEventIDLStruct  event = new MyEventIDLStruct(Math.random(), "Event");
			// m_supplier.publishEvent(myEventIDLStruct);
			// m_logger.info("Now sending myEventIDLStruct event...");
		} catch (Throwable thr) {
			m_logger.log(Level.WARNING, "failed to send event.");
			throw (new AcsJCouldntPerformActionEx(thr))
					.toCouldntPerformActionEx();
		}
	}

	@Override
	public void receive(CoordTopic event) {
		//TODO
	}

	/////////////////////////////////////////////////////////////
	// Implementation of Operations
	/////////////////////////////////////////////////////////////
	@Override
	public void Park() {
		//TODO
	}

}
