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

package MotorHealthEstimator;

import RMQCommons.Topic;

public class MotorHealthSV extends Topic {

	private Boolean status = false;
	private double certanty = 0.0;

	public MotorHealthSV() {
		super("MotorHealthSV");
	}

	public MotorHealthSV(final String msg) {
		super("MotorHealthSV");
		setPayload(msg);
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean theStatus) {
		status = theStatus;
	}
	public double getCertanty() {
		return certanty;
	}

	public void setCertanty(double theCertanty) {
		certanty = theCertanty;
	}

	public String getPayload() {
		String payload = "";
		payload += "status boolean " + getStatus() + " ";
		payload += "certanty double " + getCertanty() + " ";

		return payload;
	}

	public String getPayloadWithoutType() {
		String payload = "";
		payload += "status " + getStatus() + " ";
		payload += "certanty " + getCertanty() + " ";

		return payload;
	}

	public String getPayloadWithoutNameAndType() {
		String payload = "";
		payload += getStatus() + " ";
		payload += getCertanty() + " ";

		return payload;
	}

	public void setPayload(final String payload) {

		/*
		 * Parse the message parameters and fill in the topic attributes.
		 */
		String[] params = payload.split(" ");
		for (int i = 0; i < params.length; i += 3) {
			if (params[i].matches("status") && ((i + 2) < params.length)) {
				setStatus(Boolean.parseBoolean(params[i + 2]));

			}
			if (params[i].matches("certanty") && ((i + 2) < params.length)) {
				setCertanty(Double.parseDouble(params[i + 2]));

			}

		}
	}

}
