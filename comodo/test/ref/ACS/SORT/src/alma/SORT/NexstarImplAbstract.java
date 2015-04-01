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

import alma.ACS.jbaci.*;
import alma.ACS.impl.*;

import alma.ACSErrTypeCommon.CouldntPerformActionEx;
import alma.ACSErrTypeCommon.wrappers.AcsJCouldntPerformActionEx;

import alma.SORT.*;

import alma.SORT.DevCCD;
import alma.SORT.DevCCDOperations;

/**
 * @see alma.SORTCommon
 * @see alma.SORT.DevCCD
 * @see alma.SORT.DevCCDOperations
 * @see alma.acs.component.ComponentLifecycle
 * @author ACS Component Code Generator
 * @version $Id$
 */
public abstract class NexstarImplAbstract extends CharacteristicComponentImpl
		implements
			ComponentLifecycle,
			DevCCDOperations {
	private ContainerServices m_containerServices;
	protected Logger m_logger;

	/* Holds the last frame that has been taken */private DataAccess frameDataAccess;
	private ROlongSeq frame;
	private ROlongSeqImpl frameImpl;
	private ROlongSeqPOATie frameTie;

	/* Holds the device name that should be accesed  */private DataAccess deviceDataAccess;
	private RW device;
	private RWImpl deviceImpl;
	private RWPOATie deviceTie;

	/* Set and get the red balance value */private DataAccess redDataAccess;
	private RWlong red;
	private RWlongImpl redImpl;
	private RWlongPOATie redTie;

	/* Set and get the blue balance value */private DataAccess blueDataAccess;
	private RWlong blue;
	private RWlongImpl blueImpl;
	private RWlongPOATie blueTie;

	/* Set and get the green balance value */private DataAccess greenDataAccess;
	private RWlong green;
	private RWlongImpl greenImpl;
	private RWlongPOATie greenTie;

	/* Set and get the size of the pixels */private DataAccess pixelBiasDataAccess;
	private RWlong pixelBias;
	private RWlongImpl pixelBiasImpl;
	private RWlongPOATie pixelBiasTie;

	/* Set and get the reset level (black balance) */private DataAccess resetLevelDataAccess;
	private RWlong resetLevel;
	private RWlongImpl resetLevelImpl;
	private RWlongPOATie resetLevelTie;

	/* Set and get the exposure time */private DataAccess exposureDataAccess;
	private RWlong exposure;
	private RWlongImpl exposureImpl;
	private RWlongPOATie exposureTie;

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

			frameDataAccess = new MemoryDataAccess();
			frameImpl = new ROlongSeqImpl("frame", this, frameDataAccess);
			frameTie = new ROlongSeqPOATie(frameImpl);
			frame = ROlongSeqHelper.narrow(this.registerProperty(frameImpl,
					frameTie));

			deviceDataAccess = new MemoryDataAccess();
			deviceImpl = new RWImpl("device", this, deviceDataAccess);
			deviceTie = new RWPOATie(deviceImpl);
			device = RWHelper.narrow(this.registerProperty(deviceImpl,
					deviceTie));

			redDataAccess = new MemoryDataAccess();
			redImpl = new RWlongImpl("red", this, redDataAccess);
			redTie = new RWlongPOATie(redImpl);
			red = RWlongHelper.narrow(this.registerProperty(redImpl, redTie));

			blueDataAccess = new MemoryDataAccess();
			blueImpl = new RWlongImpl("blue", this, blueDataAccess);
			blueTie = new RWlongPOATie(blueImpl);
			blue = RWlongHelper
					.narrow(this.registerProperty(blueImpl, blueTie));

			greenDataAccess = new MemoryDataAccess();
			greenImpl = new RWlongImpl("green", this, greenDataAccess);
			greenTie = new RWlongPOATie(greenImpl);
			green = RWlongHelper.narrow(this.registerProperty(greenImpl,
					greenTie));

			pixelBiasDataAccess = new MemoryDataAccess();
			pixelBiasImpl = new RWlongImpl("pixelBias", this,
					pixelBiasDataAccess);
			pixelBiasTie = new RWlongPOATie(pixelBiasImpl);
			pixelBias = RWlongHelper.narrow(this.registerProperty(
					pixelBiasImpl, pixelBiasTie));

			resetLevelDataAccess = new MemoryDataAccess();
			resetLevelImpl = new RWlongImpl("resetLevel", this,
					resetLevelDataAccess);
			resetLevelTie = new RWlongPOATie(resetLevelImpl);
			resetLevel = RWlongHelper.narrow(this.registerProperty(
					resetLevelImpl, resetLevelTie));

			exposureDataAccess = new MemoryDataAccess();
			exposureImpl = new RWlongImpl("exposure", this, exposureDataAccess);
			exposureTie = new RWlongPOATie(exposureImpl);
			exposure = RWlongHelper.narrow(this.registerProperty(exposureImpl,
					exposureTie));

		} catch (Throwable throwable) {
			throw new ComponentLifecycleException(
					"Failed to create properties of DevCCD", throwable);
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
	 * Get the frame
	 * @return ROlongSeq
	 */
	public ROlongSeq frame() {
		return frame;
	}

	/**
	 * Get the device
	 * @return RW
	 */
	public RW device() {
		return device;
	}

	/**
	 * Get the red
	 * @return RWlong
	 */
	public RWlong red() {
		return red;
	}

	/**
	 * Get the blue
	 * @return RWlong
	 */
	public RWlong blue() {
		return blue;
	}

	/**
	 * Get the green
	 * @return RWlong
	 */
	public RWlong green() {
		return green;
	}

	/**
	 * Get the pixelBias
	 * @return RWlong
	 */
	public RWlong pixelBias() {
		return pixelBias;
	}

	/**
	 * Get the resetLevel
	 * @return RWlong
	 */
	public RWlong resetLevel() {
		return resetLevel;
	}

	/**
	 * Get the exposure
	 * @return RWlong
	 */
	public RWlong exposure() {
		return exposure;
	}

	/////////////////////////////////////////////////////////////
	// Implementation of Operations
	/////////////////////////////////////////////////////////////
	/**	 * Tell the camera to capture a frame with certain exposure time.
	
	 * @param exposure 
	 * @return ImageType
	 */
	public abstract ImageType image(double exposure);

	/**	 * This method locks the component, so any order received from any	 * other component that is not CSATStatus or CSATControl will be ignored
	 * @return void
	 */
	public abstract void lock();

	/**	 * Unlocks the component, so the component will follow any order that
	 * @return void
	 */
	public abstract void unlock();

	/**	 * Turns the CCD on (in simple models it will only affect
	 * @return void
	 */
	public abstract void on();

	/**	 * Turns the CCD off (in simple models it will only affect
	 * @return void
	 */
	public abstract void off();

}
