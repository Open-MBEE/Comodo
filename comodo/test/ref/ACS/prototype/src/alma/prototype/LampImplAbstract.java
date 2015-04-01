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

import alma.prototype.Lamp;
import alma.prototype.LampOperations;

/**
 * @see alma.prototypeCommon
 * @see alma.prototype.Device
 * @see alma.prototype.DeviceOperations
 * @see alma.prototype.Lamp
 * @see alma.prototype.LampOperations
 * @see alma.acs.component.ComponentLifecycle
 * @author ACS Component Code Generator
 * @version $Id$
 */
public abstract class LampImplAbstract extends CharacteristicComponentImpl
		implements
			ComponentLifecycle,
			DeviceOperations,
			LampOperations {
	private ContainerServices m_containerServices;
	protected Logger m_logger;

	/* Holds the last frame that has been taken */private DataAccess stateDataAccess;
	private ROlong state;
	private ROlongImpl stateImpl;
	private ROlongPOATie stateTie;

	/* Holds the last frame that has been taken */private DataAccess subStateDataAccess;
	private ROlong subState;
	private ROlongImpl subStateImpl;
	private ROlongPOATie subStateTie;

	/* Holds the last frame that has been taken */private DataAccess modeDataAccess;
	private ROlong mode;
	private ROlongImpl modeImpl;
	private ROlongPOATie modeTie;

	/* Holds the last frame that has been taken */private DataAccess lampStatusDataAccess;
	private ROlong lampStatus;
	private ROlongImpl lampStatusImpl;
	private ROlongPOATie lampStatusTie;

	/* Holds the last frame that has been taken */private DataAccess lampCmdDataAccess;
	private ROlong lampCmd;
	private ROlongImpl lampCmdImpl;
	private ROlongPOATie lampCmdTie;

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

		// try to initialize BACI properties
		try {

			stateDataAccess = new MemoryDataAccess();
			stateImpl = new ROlongImpl("state", this, stateDataAccess);
			stateTie = new ROlongPOATie(stateImpl);
			state = ROlongHelper.narrow(this.registerProperty(stateImpl,
					stateTie));

			subStateDataAccess = new MemoryDataAccess();
			subStateImpl = new ROlongImpl("subState", this, subStateDataAccess);
			subStateTie = new ROlongPOATie(subStateImpl);
			subState = ROlongHelper.narrow(this.registerProperty(subStateImpl,
					subStateTie));

			modeDataAccess = new MemoryDataAccess();
			modeImpl = new ROlongImpl("mode", this, modeDataAccess);
			modeTie = new ROlongPOATie(modeImpl);
			mode = ROlongHelper
					.narrow(this.registerProperty(modeImpl, modeTie));

			lampStatusDataAccess = new MemoryDataAccess();
			lampStatusImpl = new ROlongImpl("lampStatus", this,
					lampStatusDataAccess);
			lampStatusTie = new ROlongPOATie(lampStatusImpl);
			lampStatus = ROlongHelper.narrow(this.registerProperty(
					lampStatusImpl, lampStatusTie));

			lampCmdDataAccess = new MemoryDataAccess();
			lampCmdImpl = new ROlongImpl("lampCmd", this, lampCmdDataAccess);
			lampCmdTie = new ROlongPOATie(lampCmdImpl);
			lampCmd = ROlongHelper.narrow(this.registerProperty(lampCmdImpl,
					lampCmdTie));

		} catch (Throwable throwable) {
			throw new ComponentLifecycleException(
					"Failed to create properties of Lamp", throwable);
		}

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
	// Implementation of BACI getters
	/////////////////////////////////////////////////////////////
	/**
	 * Get the state
	 * @return ROlong
	 */
	public ROlong state() {
		return state;
	}

	/**
	 * Get the subState
	 * @return ROlong
	 */
	public ROlong subState() {
		return subState;
	}

	/**
	 * Get the mode
	 * @return ROlong
	 */
	public ROlong mode() {
		return mode;
	}

	/**
	 * Get the lampStatus
	 * @return ROlong
	 */
	public ROlong lampStatus() {
		return lampStatus;
	}

	/**
	 * Get the lampCmd
	 * @return ROlong
	 */
	public ROlong lampCmd() {
		return lampCmd;
	}

	/////////////////////////////////////////////////////////////
	// Implementation of Operations
	/////////////////////////////////////////////////////////////
	/**
	 * @return void
	 */
	public abstract void switchOn();

	/**
	 * @return void
	 */
	public abstract void switchOff();

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
