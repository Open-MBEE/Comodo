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

import alma.ACS.*;
import alma.ACS.ComponentStates;
import java.util.logging.Level;

import alma.ACSErrTypeCommon.CouldntPerformActionEx;
import alma.ACSErrTypeCommon.wrappers.AcsJCouldntPerformActionEx;

import alma.SORT.*;

/**
 * Developer's file to implement the ComponentAbstract
 * @see alma.SORT.ComponentAbstract
 * @author ACS Component Code Generator
 * @version $Id$
 */
public class InstrumentImpl extends InstrumentImplAbstract {
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
	// Implementation of Operations
	/////////////////////////////////////////////////////////////
	@Override
	public ImageType takeImage(int exposureTime) {
		return new ImageType();
	}

	@Override
	public void cameraOn() {
		//TODO
	}

	@Override
	public void cameraOff() {
		//TODO
	}

	@Override
	public void setRGB(RGB rgbConfig) {
		//TODO
	}

	@Override
	public void setPixelBias(int bias) {
		//TODO
	}

	@Override
	public void setResetLevel(int resetLevel) {
		//TODO
	}

}
