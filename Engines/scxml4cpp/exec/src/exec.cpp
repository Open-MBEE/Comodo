/*
 *    scampl4cpp/exec
 *
 *    Copyright by European Southern Observatory, 2012
 *    All rights reserved
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
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 *    02111-1307 USA.
 */

/*
 * $Id: exec.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <iostream>
#include <list>

#include <scxml4cpp/Helper.h>
#include <scxml4cpp/StateMachine.h>
#include <scxml4cpp/Event.h>
#include <scxml4cpp/Action.h>
#include <scxml4cpp/Executor.h>
#include <scxml4cpp/Context.h>
#include <scxml4cpp/State.h>
#include <scxml4cpp/SCXMLReader.h>
#include <scxml4cpp/DOMSCXMLReader.h>
#include "ActionTest.h"

using namespace std;
using namespace scxml4cpp;

void printHelp() {
	cout << "Help:\n";
	cout << " h  Print this help\n";
	cout << " q  Exit the application\n";
	cout << " e  Inject event 'e1'\n";
	cout << " l  Load SCXML model\n";
	cout << " r  Reset SCXML executor\n";
	cout << " s  Print SM status\n";
}


int main(void) {
	Helper helper;
	DOMSCXMLReader reader;
	Event event("generic", Event::CHANGE_EVENT);
	Context ctx;

	StateMachine* sm = NULL;
	Executor* exec = NULL;

	list<Action*> actionList;
	ActionTest actionLog("LogEntryS0");
	actionList.push_back(&actionLog);

	puts("SCXML Executor (h=help, q=quit):");
	bool quit = false;
	string inBuffer;
	while (quit == false) {
		cout << "Enter command: ";
		getline(cin, inBuffer);
		if (inBuffer.compare("q") == 0) {
			quit = true;
			cout << "Exiting ... \n";
		} else if (inBuffer.compare("h") == 0) {
			printHelp();
		} else if (inBuffer.compare("s") == 0) {
			if (exec != NULL) {
				cout << "Status: " << exec->formatStatus() << "\n";
			}
		} else if (inBuffer.compare("r") == 0) {
			cout << "Reset\n";
			if (exec != NULL) {
				exec->stop();
				exec->start();
			}
		} else if (inBuffer.compare("l") == 0) {
			if (exec != NULL) {
				exec->stop();
			}

			cout << "\nEnter SCXML model to load: ";
			string filename;
			getline(cin, filename);
			delete sm;
			sm = reader.read(filename, &actionList, NULL);
			if (sm == NULL) {
				cout << "\nERROR: not able to parse " << filename << "\n";
			} else {
			cout << helper.printStateMachine(sm);
				if (sm == NULL) {
					cout << "\nERROR: not able to create context\n";
				} else {
					delete exec;
					exec = new Executor(sm, &ctx);
					if (exec == NULL) {
						cout << "\nERROR: not able to create SM executor\n";
					} else {
						exec->start();
						cout << "SCXML model " << filename << " started ...\n";
						cout << "Status: " << exec->formatStatus() << "\n";
					}
				}
			}
		} else if (inBuffer.compare("e") == 0) {
			cout << "\nEnter event name to inject: ";
			string eventName;
			getline(cin, eventName);
			if (exec != NULL) {
				if (exec->isRunning()) {
					event.setId(eventName);
					exec->processEvent(event);
				} else {
					cout << "\nERROR: no SM executor running\n";
				}
			} else {
				cout << "\nERROR: no SM executor available\n";
			}
		}
	}

	if (exec != NULL) {
		exec->stop();
		cout << "Stopped executor ...\n";
	}

	delete exec;
	//cout << "Deleting executor ...\n";
	//cout << "Before Deleting SM model ...\n";
	delete sm;
	//cout << "After Deleting SM model ...\n";

	cout << "Exiting ...\n";
	return EXIT_SUCCESS;
}
