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

import java.util.HashMap;

import RMQCommons.Topic;

public class TopicFactory {
	private static TopicFactory mInstance = null;

	/*
	 * Make sure it is created only via getInstance method
	 * by defining private the constructor.
	 */
	private TopicFactory() {
	}

	public static TopicFactory getInstance() {
		if (mInstance == null) {
			mInstance = new TopicFactory();
		}
		return mInstance;
	}

	public Topic createTopic(final String topicName, final String payload) {

		Topic t = null;

		if (topicName.isEmpty()) {
			/* error */
		} else if (topicName.matches("NamedPositionSV")) {
			t = new NamedPositionSV(payload);
		} else {
			/* error */
		}

		return t;
	}

	public HashMap<String, Topic> createAllTopics() {
		HashMap<String, Topic> map = new HashMap<String, Topic>();
		Topic t = null;

		t = new NamedPositionSV();
		map.put("NamedPositionSV", t);

		return map;
	}
}
