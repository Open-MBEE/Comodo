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

package alma.prototype;

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

import alma.prototype.*;

import alma.prototype.Device;
import alma.prototype.DeviceOperations;

import alma.prototype.CCDSpectro;
import alma.prototype.CCDSpectroOperations;

/**
 * @see alma.prototypeCommon
 * @see alma.prototype.Device
 * @see alma.prototype.DeviceOperations
 * @see alma.prototype.CCDSpectro
 * @see alma.prototype.CCDSpectroOperations
 * @see alma.acs.component.ComponentLifecycle
 * @author ACS Component Code Generator
 * @version $Id$
 */
public abstract class CCDSpectroImplAbstract
		extends
			CharacteristicComponentImpl
		implements
			ComponentLifecycle,
			DeviceOperations,
			CCDSpectroOperations {
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
	/**
	 * @return void
	 */
	public abstract void standby();

	/**
	 * @return void
	 */
	public abstract void online();

	/**
	 * @return void
	 */
	public abstract void off();

	/**	 * Tell the camera to capture a frame with certain exposure time.
	
	 * @param val 
	
	 * @param timeout 
	 * @return void
	 */
	public abstract void setup(String val, long timeout);

}
