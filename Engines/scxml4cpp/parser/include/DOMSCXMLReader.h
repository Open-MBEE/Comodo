/*
 *    scampl4cpp/parser
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
 * $Id: DOMSCXMLReader.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef DOMSCXMLREADER_H
#define DOMSCXMLREADER_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#ifndef SCXMLREADER_H
#include "SCXMLReader.h"
#endif

#include <stdio.h>
#include <stdexcept>

#include <xercesc/dom/DOM.hpp>
#include <xercesc/dom/DOMDocument.hpp>
#include <xercesc/dom/DOMDocumentType.hpp>
#include <xercesc/dom/DOMElement.hpp>
#include <xercesc/dom/DOMImplementation.hpp>
#include <xercesc/dom/DOMImplementationLS.hpp>
#include <xercesc/dom/DOMNodeIterator.hpp>
#include <xercesc/dom/DOMNodeList.hpp>
#include <xercesc/dom/DOMText.hpp>
#include <xercesc/sax/HandlerBase.hpp>
#include <xercesc/parsers/XercesDOMParser.hpp>
#include <xercesc/util/XMLUni.hpp>


using namespace xercesc;

namespace scxml4cpp
{

    class StateMachine;
    class State;
    class Action;
    class Activity;

    class DOMSCXMLReader : public SCXMLReader 
    {
      public: 
	DOMSCXMLReader();
	~DOMSCXMLReader();
	StateMachine* read(const std::string& filename, 
			   std::list<Action*>* actions, 
			   std::list<Activity*>* activities) throw(std::runtime_error);

      private:
	std::list<DOMElement*> mStates;
	std::list<Action*>*    mActions;
	std::list<Activity*>*  mActivities;
	std::list<Action*>     mTraces;

	StateMachine*          mSM;
	DOMDocument*           mDoc;
	XercesDOMParser*       mDomParser;
	ErrorHandler*          mErrHandler;

	XMLCh* XML_SCXML;
	XMLCh* XML_INITIAL;
	XMLCh* XML_STATE;
	XMLCh* XML_PARALLEL;
	XMLCh* XML_FINAL;
	XMLCh* XML_ONENTRY;
	XMLCh* XML_ONEXIT;
	XMLCh* XML_TRANSITION;
	XMLCh* XML_HISTORY;
	XMLCh* XML_INVOKE;
    
	// SCXML Element's attributes names
	XMLCh* XML_ATTR_INITIAL;
	XMLCh* XML_ATTR_NAME;
	/*	
		XMLCh* XML_ATTR_XMLNS = "xmlns";
		XMLCh* XML_ATTR_VERSION = "version";
		XMLCh* XML_ATTR_PROFILE = "profile";
		XMLCh* XML_ATTR_EXMODE = "exmode";
	*/
	XMLCh* XML_ATTR_ID;
	XMLCh* XML_ATTR_EVENT;
	XMLCh* XML_ATTR_COND;
	XMLCh* XML_ATTR_TARGET;
	XMLCh* XML_ATTR_TYPE;
	/*
	  XMLCh* XML_ATTR_LABEL = "label";
	  XMLCh* XML_ATTR_EXPR = "expr";
	  XMLCh* XML_ATTR_LEVEL = "level";
	  XMLCh* XML_ATTR_ANCHOR = "anchor";
	*/
	// Custom Element name (extension to the SCXML Executable Content)
	XMLCh* XML_ACTION;
	XMLCh* XML_ATTR_ACTION_NAME;
	XMLCh* XML_ATTR_ACTION_PARAM;
    
	XMLCh* SCXML_HISTORY_DEEP;
	XMLCh* SCXML_HISTORY_SHALLOW;
	XMLCh* USER_DATA_KEY_STATE;
	XMLCh* DEFAULT_SCXML_NAME;


	void addTraces(State* s);
	void delTraces();

	void parseActivities();
	void parseActions(const XMLCh*);
	void resolveInitialStates();
	Action* findAction(const XMLCh*);
	DOMNodeList* findCustomActionXMLElement(DOMElement* elementList);
	Activity* findActivity(const XMLCh*);
	State* findTargetState(const XMLCh*);
	void parseTransitions();
	void resolveParents();
	void resolveHistory();
	bool isStateCompound(DOMElement*);
	void parseStates(); 
	void parseDoc() throw(std::runtime_error);
	
	DOMSCXMLReader(const DOMSCXMLReader&);             //! Disable copy constructor
	DOMSCXMLReader& operator= (const DOMSCXMLReader&); //! Disable assignment operator
    };

}
#endif
