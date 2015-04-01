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

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;

import org.apache.commons.scxml.model.State;
import org.apache.commons.scxml.model.ModelException;
import org.apache.commons.scxml.TriggerEvent;

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

import alma.prsModule.*;

import alma.prsModule.PrsInterface;
import alma.prsModule.PrsInterfaceOperations;

import alma.prsModule.SMMaintenanceOperations;

import alma.acs.nc.SimpleSupplier;
import alma.acs.nc.Consumer;

import alma.prsModule.CHANNEL_PRSINTERFACE_P2;
import alma.prsModule.CHANNEL_PRSINTERFACE_P1;

/**
 * @see alma.prsModuleCommon
 * @see alma.prsModule.PrsInterface
 * @see alma.prsModule.PrsInterfaceOperations
 * @see alma.acs.component.ComponentLifecycle
 * @author ACS Component Code Generator
 * @version $Id$
 */
public abstract class prsComponentCppImplAbstract
		extends
			CharacteristicComponentImpl
		implements
			ComponentLifecycle,
			PrsInterfaceOperations,
			SMMaintenanceOperations {
	private ContainerServices m_containerServices;
	protected Logger m_logger;
	protected SMEngine m_engine;

	protected SimpleSupplier m_supplier_p2 = null;
	protected Consumer m_consumer_p1 = null;

	private DataAccess statusDataAccess;
	private ROlong status;
	private ROlongImpl statusImpl;
	private ROlongPOATie statusTie;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////

	public void initialize(ContainerServices containerServices)
			throws ComponentLifecycleException {
		preInitialize();

		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();

		m_logger.info("initialize() called...");

		m_engine = new SMEngine("prsComponent.xml", m_logger);

		super.initialize(m_containerServices);

		try {
			m_supplier_p2 = new SimpleSupplier(CHANNEL_PRSINTERFACE_P2.value,
					m_containerServices);
		} catch (AcsJException e) {
			m_logger.info("m_supplier_p2 init failed..");
		}

		try {
			m_consumer_p1 = new Consumer(CHANNEL_PRSINTERFACE_P1.value,
					m_containerServices);
			m_consumer_p1.addSubscription(CoordTopic.class, this);
			m_consumer_p1.consumerReady();
			m_consumer_p1.disconnect();
		} catch (AcsJException e) {
			m_logger.info("m_consumer_p1 init failed..");
		}

		// try to initialize BACI properties
		try {

			statusDataAccess = new MemoryDataAccess();
			statusImpl = new ROlongImpl("status", this, statusDataAccess);
			statusTie = new ROlongPOATie(statusImpl);
			status = ROlongHelper.narrow(this.registerProperty(statusImpl,
					statusTie));

		} catch (Throwable throwable) {
			throw new ComponentLifecycleException(
					"Failed to create properties of PrsInterface", throwable);
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
	// Declaration of Notification Channel Operations
	/////////////////////////////////////////////////////////////

	public abstract void sendEvent() throws CouldntPerformActionEx;

	public abstract void receive(CoordTopic event);

	/////////////////////////////////////////////////////////////
	// Implementation of BACI getters
	/////////////////////////////////////////////////////////////
	/**
	 * Get the status
	 * @return ROlong
	 */
	public ROlong status() {
		return status;
	}

	/////////////////////////////////////////////////////////////
	// Implementation of Operations
	/////////////////////////////////////////////////////////////
	/**
	 * @return void
	 */
	public abstract void Park();

	/**
	 * Resets the StateMachine to initial state
	 */
	public void SMreset() {
		try {
			m_engine.getEngine().reset();
		} catch (ModelException e) {
			m_logger.info("Could not reset the enigne: " + e.getMessage());
		}
	}

	/**
	 * Loads a SCXML definition file.
	 * If given an empty string for file, it reloads the currently active file,
	 * otherwise it loads the file from the given path.
	 * @return true on success; false on error e.g. "file not found"
	 */
	public boolean SMloadDefinition(final String fileName) {

		if (fileName.isEmpty()) {
			m_engine.loadModel("prsComponent.xml");
		} else {
			m_engine.loadModel(fileName);
		}
		m_engine.startExecution();

		return true;
	}

	/**
	 * Retrieve current state as string.
	 */
	public String SMgetCurrentState() {
		return m_engine.getCurrentState();
	}

	public boolean SMfireSignal(String signal) {
		TriggerEvent evnt = new TriggerEvent(signal, TriggerEvent.SIGNAL_EVENT,
				null);
		try {
			m_engine.getEngine().triggerEvent(evnt);
		} catch (ModelException e) {
			m_logger.info(e.getMessage());
		}
		return m_engine.getEngine().getCurrentStatus().isFinal();
	}

	public boolean Preset() {
		return SMfireSignal("Preset");
	}

	public boolean MoveTelErr() {
		return SMfireSignal("MoveTelErr");
	}

	public boolean MoveTelOk() {
		return SMfireSignal("MoveTelOk");
	}

	public boolean Init() {
		return SMfireSignal("Init");
	}

}
