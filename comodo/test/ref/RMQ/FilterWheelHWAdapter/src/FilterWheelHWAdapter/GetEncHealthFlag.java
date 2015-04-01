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

package FilterWheelHWAdapter;

import RMQCommons.Topic;

public class GetEncHealthFlag extends Topic {

	private Boolean f = false;

	public GetEncHealthFlag() {
		super("GetEncHealthFlag");
	}

	public GetEncHealthFlag(final String msg) {
		super("GetEncHealthFlag");
		setPayload(msg);
	}

	public Boolean getF() {
		return f;
	}

	public void setF(Boolean theF) {
		f = theF;
	}

	public String getPayload() {
		String payload = "";
		payload += "f boolean " + getF() + " ";

		return payload;
	}

	public String getPayloadWithoutType() {
		String payload = "";
		payload += "f " + getF() + " ";

		return payload;
	}

	public String getPayloadWithoutNameAndType() {
		String payload = "";
		payload += getF() + " ";

		return payload;
	}

	public void setPayload(final String payload) {

		/*
		 * Parse the message parameters and fill in the topic attributes.
		 */
		String[] params = payload.split(" ");
		for (int i = 0; i < params.length; i += 3) {
			if (params[i].matches("f") && ((i + 2) < params.length)) {
				setF(Boolean.parseBoolean(params[i + 2]));

			}

		}
	}

}
