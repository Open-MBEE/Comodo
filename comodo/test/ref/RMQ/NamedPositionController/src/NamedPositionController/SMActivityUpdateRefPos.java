/*******************************************************************************
 *    ESO - European Southern Observatory
 *
 *    (c) European Southern Observatory, 2011
 *    Copyright by ESO 
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
 * COMODO            -           Created.
 * 
 */

package NamedPositionController;

import org.apache.commons.scxml.SCInstance;

import org.apache.log4j.Logger;

import NamedPositionController.SMActivity;
import RMQCommons.Message;

public class SMActivityUpdateRefPos extends SMActivity {

	public SMActivityUpdateRefPos(String name, String parentStateId,
			SCInstance parentSCInstance, Logger logger) {
		super(name, parentStateId, parentSCInstance, logger);
	}

	public void run() {

		startRunning();
		openConnection();

		while (isRunning()) {
			try {
				Message msg = nextMessage(10);
				if (msg != null) {

					if (msg.getName().matches(mMotorHealthSV.getName())) {
						// ToBeImplemented
					}
					if (msg.getName().matches(mNamedPositionSV.getName())) {
						// ToBeImplemented
					}
					if (msg.getName().matches(mGoal.getName())) {
						// ToBeImplemented
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
				stopRunning();
			}
		}
		closeConnection();
		triggerEvent("UpdateRefPos.done");
	}

}
