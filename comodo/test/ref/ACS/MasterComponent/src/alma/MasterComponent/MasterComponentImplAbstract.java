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

package alma.MasterComponent;

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

import alma.MasterComponent.*;

import alma.MasterComponent.MasterComponentIFReadOnly;
import alma.MasterComponent.MasterComponentIFReadOnlyOperations;

import alma.MasterComponent.MasterComponentIF;
import alma.MasterComponent.MasterComponentIFOperations;

import alma.MasterComponent.SMMaintenanceOperations;

/**
 * @see alma.MasterComponentCommon
 * @see alma.MasterComponent.MasterComponentIFReadOnly
 * @see alma.MasterComponent.MasterComponentIFReadOnlyOperations
 * @see alma.MasterComponent.MasterComponentIF
 * @see alma.MasterComponent.MasterComponentIFOperations
 * @see alma.acs.component.ComponentLifecycle
 * @author ACS Component Code Generator
 * @version $Id$
 */
public abstract class MasterComponentImplAbstract
		extends
			CharacteristicComponentImpl
		implements
			ComponentLifecycle,
			MasterComponentIFReadOnlyOperations,
			MasterComponentIFOperations,
			SMMaintenanceOperations {
	private ContainerServices m_containerServices;
	protected Logger m_logger;
	protected SMEngine m_engine;

	private DataAccess currentStateHierarchyDataAccess;
	private ROstring currentStateHierarchy;
	private ROstringImpl currentStateHierarchyImpl;
	private ROstringPOATie currentStateHierarchyTie;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////

	public void initialize(ContainerServices containerServices)
			throws ComponentLifecycleException {
		preInitialize();

		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();

		m_logger.info("initialize() called...");

		m_engine = new SMEngine("MasterComponent.xml", m_logger);

		super.initialize(m_containerServices);

		// try to initialize BACI properties
		try {

			currentStateHierarchyDataAccess = new MemoryDataAccess();
			currentStateHierarchyImpl = new ROstringImpl(
					"currentStateHierarchy", this,
					currentStateHierarchyDataAccess);
			currentStateHierarchyTie = new ROstringPOATie(
					currentStateHierarchyImpl);
			currentStateHierarchy = ROstringHelper.narrow(this
					.registerProperty(currentStateHierarchyImpl,
							currentStateHierarchyTie));

		} catch (Throwable throwable) {
			throw new ComponentLifecycleException(
					"Failed to create properties of MasterComponentIFReadOnly",
					throwable);
		}
		// try to initialize BACI properties
		try {

			currentStateHierarchyDataAccess = new MemoryDataAccess();
			currentStateHierarchyImpl = new ROstringImpl(
					"currentStateHierarchy", this,
					currentStateHierarchyDataAccess);
			currentStateHierarchyTie = new ROstringPOATie(
					currentStateHierarchyImpl);
			currentStateHierarchy = ROstringHelper.narrow(this
					.registerProperty(currentStateHierarchyImpl,
							currentStateHierarchyTie));

		} catch (Throwable throwable) {
			throw new ComponentLifecycleException(
					"Failed to create properties of MasterComponentIF",
					throwable);
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
	 * Get the currentStateHierarchy
	 * @return ROstring
	 */
	public ROstring currentStateHierarchy() {
		return currentStateHierarchy;
	}

	/////////////////////////////////////////////////////////////
	// Implementation of Operations
	/////////////////////////////////////////////////////////////
	/**
	
	 * @param event 
	 * @return void
	 */
	public abstract void doTransition(SubsystemStateEvent event);

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
			m_engine.loadModel("MasterComponent.xml");
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

	public boolean shutdownPass2() {
		return SMfireSignal("shutdownPass2");
	}

	public boolean initPass2() {
		return SMfireSignal("initPass2");
	}

	public boolean disaster() {
		return SMfireSignal("disaster");
	}

	public boolean stop() {
		return SMfireSignal("stop");
	}

	public boolean shutdownPass1() {
		return SMfireSignal("shutdownPass1");
	}

	public boolean reinit() {
		return SMfireSignal("reinit");
	}

	public boolean error() {
		return SMfireSignal("error");
	}

	public boolean start() {
		return SMfireSignal("start");
	}

	public boolean initPass1() {
		return SMfireSignal("initPass1");
	}

}
