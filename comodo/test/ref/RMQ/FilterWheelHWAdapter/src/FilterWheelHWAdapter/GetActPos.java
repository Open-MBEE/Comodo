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

public class GetActPos extends Topic {

	private double angle = 0.0;

	public GetActPos() {
		super("GetActPos");
	}

	public GetActPos(final String msg) {
		super("GetActPos");
		setPayload(msg);
	}

	public double getAngle() {
		return angle;
	}

	public void setAngle(double theAngle) {
		angle = theAngle;
	}

	public String getPayload() {
		String payload = "";
		payload += "angle double " + getAngle() + " ";

		return payload;
	}

	public String getPayloadWithoutType() {
		String payload = "";
		payload += "angle " + getAngle() + " ";

		return payload;
	}

	public String getPayloadWithoutNameAndType() {
		String payload = "";
		payload += getAngle() + " ";

		return payload;
	}

	public void setPayload(final String payload) {

		/*
		 * Parse the message parameters and fill in the topic attributes.
		 */
		String[] params = payload.split(" ");
		for (int i = 0; i < params.length; i += 3) {
			if (params[i].matches("angle") && ((i + 2) < params.length)) {
				setAngle(Double.parseDouble(params[i + 2]));

			}

		}
	}

}
