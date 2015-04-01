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

package NamedPositionEstimator;

import RMQCommons.Topic;

public class NamedPositionSV extends Topic {

	private String name = new String();
	private double certanty = 0.0;

	public NamedPositionSV() {
		super("NamedPositionSV");
	}

	public NamedPositionSV(final String msg) {
		super("NamedPositionSV");
		setPayload(msg);
	}

	public String getName() {
		return name;
	}

	public void setName(String theName) {
		name = theName;
	}
	public double getCertanty() {
		return certanty;
	}

	public void setCertanty(double theCertanty) {
		certanty = theCertanty;
	}

	public String getPayload() {
		String payload = "";
		payload += "name string " + getName() + " ";
		payload += "certanty double " + getCertanty() + " ";

		return payload;
	}

	public String getPayloadWithoutType() {
		String payload = "";
		payload += "name " + getName() + " ";
		payload += "certanty " + getCertanty() + " ";

		return payload;
	}

	public String getPayloadWithoutNameAndType() {
		String payload = "";
		payload += getName() + " ";
		payload += getCertanty() + " ";

		return payload;
	}

	public void setPayload(final String payload) {

		/*
		 * Parse the message parameters and fill in the topic attributes.
		 */
		String[] params = payload.split(" ");
		for (int i = 0; i < params.length; i += 3) {
			if (params[i].matches("name") && ((i + 2) < params.length)) {
				setName(params[i + 2]);

			}
			if (params[i].matches("certanty") && ((i + 2) < params.length)) {
				setCertanty(Double.parseDouble(params[i + 2]));

			}

		}
	}

}
