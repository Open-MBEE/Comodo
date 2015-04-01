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

package alma.pinger;

import java.util.logging.Level;
import java.util.logging.Logger;

import alma.acs.exceptions.AcsJException;
import alma.acs.container.ContainerServices;
import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;

import alma.ACS.*;
import alma.ACS.ComponentStates;

import alma.ACS.jbaci.*;
import alma.ACS.impl.*;

import alma.ACSErrTypeCommon.CouldntPerformActionEx;
import alma.ACSErrTypeCommon.wrappers.AcsJCouldntPerformActionEx;

import alma.pinger.*;

import alma.pinger.PingerIF;
import alma.pinger.PingerIFOperations;

/**
 * @see alma.pingerCommon
 * @see alma.pinger.PingableResource
 * @see alma.pinger.PingableResourceOperations
 * @see alma.pinger.PingerIF
 * @see alma.pinger.PingerIFOperations
 * @see alma.acs.component.ComponentLifecycle
 * @author ACS Component Code Generator
 * @version $Id$
 */
public abstract class PingerImplAbstract extends CharacteristicComponentImpl
		implements
			ComponentLifecycle,
			PingableResourceOperations,
			PingerIFOperations {
	private ContainerServices m_containerServices;
	protected Logger m_logger;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////

	public void initialize(ContainerServices containerServices)
			throws ComponentLifecycleException {
		preInitialize();

		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();

		m_logger.info("initialize() called...");

		super.initialize(m_containerServices);

		postInitialize();
	}

	public void execute() {
		preExecute();

		m_logger.info("execute() called...");

		postExecute();
	}

	public void cleanUp() {
		preCleanUp();

		m_logger.info("cleanUp() called...");

		postCleanUp();
	}

	public void aboutToAbort() {
		preAboutToAbort();

		cleanUp();
		m_logger.info("aboutToAbort() called...");

		postAboutToAbort();
	}

	public abstract void preInitialize();

	public abstract void postInitialize();

	public abstract void preExecute();

	public abstract void postExecute();

	public abstract void preCleanUp();

	public abstract void postCleanUp();

	public abstract void preAboutToAbort();

	public abstract void postAboutToAbort();

	/////////////////////////////////////////////////////////////
	// Implementation of ACSComponent
	/////////////////////////////////////////////////////////////

	public ComponentStates componentState() {
		return m_containerServices.getComponentStateManager().getCurrentState();
	}

	public String name() {
		return m_containerServices.getName();
	}

	/////////////////////////////////////////////////////////////
	// Implementation of Operations
	/////////////////////////////////////////////////////////////
	/**	 * Tell the camera to capture a frame with certain exposure time.
	
	 * @param howMany How many children dynamic components of the same type have to be requested.
	
	 * @param container In which container should the new components be deployed
	
	 * @param baseName What base name should be used for these components.
	A postfix _## with the sequential number will be appended to the base name
	 * @return void
	 */
	public abstract void spawnChildren(int howMany, String container,
			String baseName);

	/**	 * Sends to the logging system information about the configuration of the	 * component.
	In particular it will log a list of all children, i.e.	 * of all components that will receive a ping command in recursive
	 * @return void
	 */
	public abstract void logInfo();

	/**	 * @param fast  If true, skip performing fancier checks, so that	 * the returned result only tells us whether the resources can communicate,	 * but not if they are otherwise healthy. This could be used	 * for performance measurements.
	@param recursive  If true, the call should recursively	 * ping other PingableResources managed by this PingableResource.
	@param id  The caller	 * may pass a random number that can be used by the	 * resources to identify this call and avoid endless recursion if "recursive"	 * is true and it is not clear whether the resource topology
	
	 * @param fast 
	
	 * @param recursive 
	
	 * @param id 
	 * @return boolean
	 */
	public abstract boolean ping(boolean fast, boolean recursive, int id);

}
