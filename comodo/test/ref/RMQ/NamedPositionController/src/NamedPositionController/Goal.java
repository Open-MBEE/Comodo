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

import RMQCommons.Topic;

public class Goal extends Topic {

	private String id = new String();
	private String spec = new String();

	public Goal() {
		super("Goal");
	}

	public Goal(final String msg) {
		super("Goal");
		setPayload(msg);
	}

	public String getId() {
		return id;
	}

	public void setId(String theId) {
		id = theId;
	}
	public String getSpec() {
		return spec;
	}

	public void setSpec(String theSpec) {
		spec = theSpec;
	}

	public String getPayload() {
		String payload = "";
		payload += "id string " + getId() + " ";
		payload += "spec string " + getSpec() + " ";

		return payload;
	}

	public String getPayloadWithoutType() {
		String payload = "";
		payload += "id " + getId() + " ";
		payload += "spec " + getSpec() + " ";

		return payload;
	}

	public String getPayloadWithoutNameAndType() {
		String payload = "";
		payload += getId() + " ";
		payload += getSpec() + " ";

		return payload;
	}

	public void setPayload(final String payload) {

		/*
		 * Parse the message parameters and fill in the topic attributes.
		 */
		String[] params = payload.split(" ");
		for (int i = 0; i < params.length; i += 3) {
			if (params[i].matches("id") && ((i + 2) < params.length)) {
				setId(params[i + 2]);

			}
			if (params[i].matches("spec") && ((i + 2) < params.length)) {
				setSpec(params[i + 2]);

			}

		}
	}

}
