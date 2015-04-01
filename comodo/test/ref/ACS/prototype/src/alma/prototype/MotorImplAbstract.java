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

import alma.prototype.Motor;
import alma.prototype.MotorOperations;

/**
 * @see alma.prototypeCommon
 * @see alma.prototype.Device
 * @see alma.prototype.DeviceOperations
 * @see alma.prototype.Motor
 * @see alma.prototype.MotorOperations
 * @see alma.acs.component.ComponentLifecycle
 * @author ACS Component Code Generator
 * @version $Id$
 */
public abstract class MotorImplAbstract extends CharacteristicComponentImpl
		implements
			ComponentLifecycle,
			DeviceOperations,
			MotorOperations {
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

	/* Holds the last frame that has been taken */private DataAccess mStatusDataAccess;
	private ROlong mStatus;
	private ROlongImpl mStatusImpl;
	private ROlongPOATie mStatusTie;

	/* Holds the last frame that has been taken */private DataAccess mErrorCodeDataAccess;
	private ROlong mErrorCode;
	private ROlongImpl mErrorCodeImpl;
	private ROlongPOATie mErrorCodeTie;

	/* Holds the last frame that has been taken */private DataAccess mPositionDataAccess;
	private ROlong mPosition;
	private ROlongImpl mPositionImpl;
	private ROlongPOATie mPositionTie;

	/* Holds the last frame that has been taken */private DataAccess mEnabledDataAccess;
	private ROlong mEnabled;
	private ROlongImpl mEnabledImpl;
	private ROlongPOATie mEnabledTie;

	/* Holds the last frame that has been taken */private DataAccess mMoveDataAccess;
	private ROlong mMove;
	private ROlongImpl mMoveImpl;
	private ROlongPOATie mMoveTie;

	/* Holds the last frame that has been taken */private DataAccess mStopDataAccess;
	private ROlong mStop;
	private ROlongImpl mStopImpl;
	private ROlongPOATie mStopTie;

	/* Holds the last frame that has been taken */private DataAccess mSetPositionDataAccess;
	private ROlong mSetPosition;
	private ROlongImpl mSetPositionImpl;
	private ROlongPOATie mSetPositionTie;

	/* Holds the last frame that has been taken */private DataAccess mAccDataAccess;
	private ROlong mAcc;
	private ROlongImpl mAccImpl;
	private ROlongPOATie mAccTie;

	/* Holds the last frame that has been taken */private DataAccess mDecDataAccess;
	private ROlong mDec;
	private ROlongImpl mDecImpl;
	private ROlongPOATie mDecTie;

	/* Holds the last frame that has been taken */private DataAccess mVelDataAccess;
	private ROlong mVel;
	private ROlongImpl mVelImpl;
	private ROlongPOATie mVelTie;

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

			mStatusDataAccess = new MemoryDataAccess();
			mStatusImpl = new ROlongImpl("mStatus", this, mStatusDataAccess);
			mStatusTie = new ROlongPOATie(mStatusImpl);
			mStatus = ROlongHelper.narrow(this.registerProperty(mStatusImpl,
					mStatusTie));

			mErrorCodeDataAccess = new MemoryDataAccess();
			mErrorCodeImpl = new ROlongImpl("mErrorCode", this,
					mErrorCodeDataAccess);
			mErrorCodeTie = new ROlongPOATie(mErrorCodeImpl);
			mErrorCode = ROlongHelper.narrow(this.registerProperty(
					mErrorCodeImpl, mErrorCodeTie));

			mPositionDataAccess = new MemoryDataAccess();
			mPositionImpl = new ROlongImpl("mPosition", this,
					mPositionDataAccess);
			mPositionTie = new ROlongPOATie(mPositionImpl);
			mPosition = ROlongHelper.narrow(this.registerProperty(
					mPositionImpl, mPositionTie));

			mEnabledDataAccess = new MemoryDataAccess();
			mEnabledImpl = new ROlongImpl("mEnabled", this, mEnabledDataAccess);
			mEnabledTie = new ROlongPOATie(mEnabledImpl);
			mEnabled = ROlongHelper.narrow(this.registerProperty(mEnabledImpl,
					mEnabledTie));

			mMoveDataAccess = new MemoryDataAccess();
			mMoveImpl = new ROlongImpl("mMove", this, mMoveDataAccess);
			mMoveTie = new ROlongPOATie(mMoveImpl);
			mMove = ROlongHelper.narrow(this.registerProperty(mMoveImpl,
					mMoveTie));

			mStopDataAccess = new MemoryDataAccess();
			mStopImpl = new ROlongImpl("mStop", this, mStopDataAccess);
			mStopTie = new ROlongPOATie(mStopImpl);
			mStop = ROlongHelper.narrow(this.registerProperty(mStopImpl,
					mStopTie));

			mSetPositionDataAccess = new MemoryDataAccess();
			mSetPositionImpl = new ROlongImpl("mSetPosition", this,
					mSetPositionDataAccess);
			mSetPositionTie = new ROlongPOATie(mSetPositionImpl);
			mSetPosition = ROlongHelper.narrow(this.registerProperty(
					mSetPositionImpl, mSetPositionTie));

			mAccDataAccess = new MemoryDataAccess();
			mAccImpl = new ROlongImpl("mAcc", this, mAccDataAccess);
			mAccTie = new ROlongPOATie(mAccImpl);
			mAcc = ROlongHelper
					.narrow(this.registerProperty(mAccImpl, mAccTie));

			mDecDataAccess = new MemoryDataAccess();
			mDecImpl = new ROlongImpl("mDec", this, mDecDataAccess);
			mDecTie = new ROlongPOATie(mDecImpl);
			mDec = ROlongHelper
					.narrow(this.registerProperty(mDecImpl, mDecTie));

			mVelDataAccess = new MemoryDataAccess();
			mVelImpl = new ROlongImpl("mVel", this, mVelDataAccess);
			mVelTie = new ROlongPOATie(mVelImpl);
			mVel = ROlongHelper
					.narrow(this.registerProperty(mVelImpl, mVelTie));

		} catch (Throwable throwable) {
			throw new ComponentLifecycleException(
					"Failed to create properties of Motor", throwable);
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
	 * Get the mStatus
	 * @return ROlong
	 */
	public ROlong mStatus() {
		return mStatus;
	}

	/**
	 * Get the mErrorCode
	 * @return ROlong
	 */
	public ROlong mErrorCode() {
		return mErrorCode;
	}

	/**
	 * Get the mPosition
	 * @return ROlong
	 */
	public ROlong mPosition() {
		return mPosition;
	}

	/**
	 * Get the mEnabled
	 * @return ROlong
	 */
	public ROlong mEnabled() {
		return mEnabled;
	}

	/**
	 * Get the mMove
	 * @return ROlong
	 */
	public ROlong mMove() {
		return mMove;
	}

	/**
	 * Get the mStop
	 * @return ROlong
	 */
	public ROlong mStop() {
		return mStop;
	}

	/**
	 * Get the mSetPosition
	 * @return ROlong
	 */
	public ROlong mSetPosition() {
		return mSetPosition;
	}

	/**
	 * Get the mAcc
	 * @return ROlong
	 */
	public ROlong mAcc() {
		return mAcc;
	}

	/**
	 * Get the mDec
	 * @return ROlong
	 */
	public ROlong mDec() {
		return mDec;
	}

	/**
	 * Get the mVel
	 * @return ROlong
	 */
	public ROlong mVel() {
		return mVel;
	}

	/////////////////////////////////////////////////////////////
	// Implementation of Operations
	/////////////////////////////////////////////////////////////
	/**
	 * @return void
	 */
	public abstract void move();

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
