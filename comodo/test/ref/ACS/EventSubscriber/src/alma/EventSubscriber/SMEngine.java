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

package alma.EventSubscriber;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.scxml.Context;
import org.apache.commons.scxml.Evaluator;
import org.apache.commons.scxml.EventDispatcher;
import org.apache.commons.scxml.SCXMLExecutor;
import org.apache.commons.scxml.SCXMLHelper;
import org.apache.commons.scxml.TriggerEvent;
import org.apache.commons.scxml.env.SimpleDispatcher;
import org.apache.commons.scxml.env.Tracer;
import org.apache.commons.scxml.env.jexl.JexlContext;
import org.apache.commons.scxml.env.jexl.JexlEvaluator;
import org.apache.commons.scxml.io.SCXMLParser;
import org.apache.commons.scxml.model.SCXML;
import org.apache.commons.scxml.model.CustomAction;
import org.apache.commons.scxml.model.TransitionTarget;
import org.apache.commons.scxml.model.ModelException;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class SMEngine implements ErrorHandler {
	private SCXMLExecutor exec = null;
	private SCXML scxml = null;
	private Logger log = null;
	private Tracer errorTracer = null;
	private Evaluator exprEvaluator = null;
	private EventDispatcher eventDispatcher = null;
	private Context exprContext = null;

	public SMEngine(String fileName, Logger logger) {

		log = logger;

		errorTracer = new Tracer(); // create error tracer
		exprEvaluator = new JexlEvaluator(); // Evaluator evaluator = new ELEvaluator();
		eventDispatcher = new SimpleDispatcher(); // create event dispatcher
		exprContext = new JexlContext(); // set new context

		try {
			// load the scxml model
			loadModel(fileName);

			startExecution();

		} catch (Exception e) {
			log.info(e.getMessage());
		}

	}

	private URL getFileURL(String fileName) {
		// TODO: load file from
		// - filesystem via Java properties
		// - inside the jar file
		URL url = null;

		url = this.getClass().getResource("/config/" + fileName);

		return url;
	}

	private List<CustomAction> getActionMap() {
		// TODO
		// Review this whether it's good or not
		List<CustomAction> customActions = new ArrayList<CustomAction>();
		String line = null;
		String[] columns;

		BufferedReader reader = null;
		reader = new BufferedReader(new InputStreamReader(this.getClass()
				.getResourceAsStream("/config/SMActionMap.txt")));

		try {
			while ((line = reader.readLine()) != null) {
				columns = line.trim().split("\\s+");

				if (columns.length == 2) {
					customActions.add(new CustomAction(
							"http://my.custom-actions.domain/CUSTOM",
							columns[0], Class.forName(columns[1])));
					log.info("Mapping Custom Action: " + columns[0] + " -> "
							+ columns[1]);
				}
			}
		} catch (IOException e) {
			log.severe(e.getMessage());
		} catch (ClassNotFoundException e) {
			log.severe("Custom action class \"" + e.getMessage()
					+ "\" not found");
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					log.severe(e.getMessage());
				}
			}
		}

		return customActions;
	}

	/**
	 * Load SCXML model
	 */
	public void loadModel(final String fileName) {

		try {
			URL scxmlurl = getFileURL(fileName);
			scxml = SCXMLParser.parse(scxmlurl, this, getActionMap());
			log.info("Loaded SCXML file " + scxmlurl.toString() + "...");
		} catch (ModelException e) {
			log.severe("Could not load model: " + e.getMessage());
		} catch (SAXException e) {
			log.severe("Could not load model: " + e.getMessage());
		} catch (IOException e) {
			log.severe("Could not load model: " + e.getMessage());
		}

	}

	/**
	 * Start SCXML execution
	 */
	public void startExecution() {

		try {
			exec = new SCXMLExecutor(exprEvaluator, eventDispatcher,
					errorTracer);

			// make sure scxml is a valid SCXML doc -> ToBeDone

			exec.addListener(scxml, errorTracer);
			exec.setRootContext(exprContext);

			exec.setStateMachine(scxml);
			exec.registerInvokerClass("java", SMJavaInvoker.class);
			exec.go();
		} catch (ModelException e) {
			log.severe("Could not start SM execution: " + e.getMessage());
		}

		log.info("Started SM execution ...");
	}

	/**
	 * Retrieve current state as string.
	 */
	public String getCurrentState() {

		// Set<TransitionTarget> activeStates = exec.getCurrentStatus().getAllStates();
		Set<TransitionTarget> activeStates = exec.getCurrentStatus()
				.getStates();

		String status = "";
		for (TransitionTarget tt : activeStates) {
			status += " " + tt.getId();
		}
		return status;
	}

	public SCXMLExecutor getEngine() {
		return exec;
	}

	@Override
	public void error(SAXParseException exception) {
		log.severe(exception.getMessage());
	}

	@Override
	public void fatalError(SAXParseException exception) {
		log.severe(exception.getMessage());
	}

	@Override
	public void warning(SAXParseException exception) {
		log.warning(exception.getMessage());
	}

}
