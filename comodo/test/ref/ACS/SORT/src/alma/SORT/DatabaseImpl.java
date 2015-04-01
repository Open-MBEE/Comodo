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
public class DatabaseImpl extends DatabaseImplAbstract {
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
	public int storeProposal(Target targets) {
		return 0;
	}

	@Override
	public void removeProposal(int pid) {
		//TODO
	}

	@Override
	public Proposal getProposals() {
		return new Proposal();
	}

	@Override
	public int getProposalStatus(int pid) {
		return 0;
	}

	@Override
	public void storeImage(int pid, ImageType image) {
		//TODO
	}

	@Override
	public void clean() {
		//TODO
	}

	@Override
	public void setProposalStatus(int pid, int status) {
		//TODO
	}

	@Override
	public ImageList getProposalObservations(int pid) {
		return new ImageList();
	}

}
