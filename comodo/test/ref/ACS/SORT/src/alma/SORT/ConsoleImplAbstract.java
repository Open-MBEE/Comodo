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

package alma.SORT;

import java.util.logging.Level;
import java.util.logging.Logger;

import alma.acs.exceptions.AcsJException;
import alma.acs.container.ContainerServices;
import alma.acs.component.ComponentLifecycle;
import alma.acs.component.ComponentLifecycleException;

import alma.ACS.*;
import alma.ACS.ComponentStates;

import alma.ACSErrTypeCommon.CouldntPerformActionEx;
import alma.ACSErrTypeCommon.wrappers.AcsJCouldntPerformActionEx;

import alma.SORT.*;

import alma.SORT.CommonOperations;
import alma.SORT.CommonOperationsOperations;

import alma.SORT.Console;
import alma.SORT.ConsoleOperations;

/**
 * @see alma.SORTCommon
 * @see alma.SORT.CommonOperations
 * @see alma.SORT.CommonOperationsOperations
 * @see alma.SORT.Console
 * @see alma.SORT.ConsoleOperations
 * @see alma.acs.component.ComponentLifecycle
 * @author ACS Component Code Generator
 * @version $Id$
 */
public abstract class ConsoleImplAbstract
		implements
			ComponentLifecycle,
			CommonOperationsOperations,
			ConsoleOperations {
	private ContainerServices m_containerServices;
	protected Logger m_logger;

	/////////////////////////////////////////////////////////////
	// Implementation of ComponentLifecycle
	/////////////////////////////////////////////////////////////

	public void initialize(ContainerServices containerServices) {
		preInitialize();

		m_containerServices = containerServices;
		m_logger = m_containerServices.getLogger();

		m_logger.info("initialize() called...");

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
	/**	 * et the automatic manual mode for the operator. Raises an exception
	
	 * @param mode if true then automatic mode otherwise manual mode.
	 * @return void
	 */
	public abstract void setMode(boolean mode);

	/**
	 * @return boolean
	 */
	public abstract boolean getMode();

	/**	 * Move telescope in synchronous mode. Raises an exception if the requested
	
	 * @param coordinates az, el coordinates
	 * @return void
	 */
	public abstract void moveTelescope(Position coordinates);

	/**
	 * @return Position
	 */
	public abstract Position getTelescopePosition();

	/**	 * Get an image from the camera (from actual position of telescope).
	 * @return ImageType
	 */
	public abstract ImageType getCameraImage();

	/**
	 * @return void
	 */
	public abstract void cameraOn();

	/**
	 * @return void
	 */
	public abstract void cameraOff();

	/**
	
	 * @param rgbConfig the RGB configuration
	 * @return void
	 */
	public abstract void setRGB(RGB rgbConfig);

	/**
	
	 * @param bias the pixel bias configuration
	 * @return void
	 */
	public abstract void setPixelBias(int bias);

	/**
	
	 * @param resetLevel ResetLevel the reset level configuration
	 * @return void
	 */
	public abstract void setResetLevel(int resetLevel);

}
