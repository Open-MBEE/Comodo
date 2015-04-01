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

package wsf2ex1;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.scxml.ErrorReporter;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCInstance;
import org.apache.commons.scxml.SCXMLExpressionException;
import org.apache.commons.scxml.model.Action;
import org.apache.commons.scxml.model.ModelException;

public class SMActionDEBUGExecute extends Action {

	/** Serial version UID. */
	private static final long serialVersionUID = 1L;

	private String mName;

	public SMActionDEBUGExecute() {
		super();
	}

	/**
	 * Get the name.
	 *
	 * @return Returns the name.
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Set the name.
	 *
	 * @param name The name to set.
	 */
	public void setName(String name) {
		mName = name;
	}

	public void execute(final EventDispatcher evtDispatcher,
			final ErrorReporter errRep, final SCInstance scInstance,
			final Log appLog,
			@SuppressWarnings("rawtypes") final Collection derivedEvents)
			throws ModelException, SCXMLExpressionException {
		appLog.debug("class: SMActionDEBUGExecute, method: execute, attribute name: "
				+ mName);

	}

}
