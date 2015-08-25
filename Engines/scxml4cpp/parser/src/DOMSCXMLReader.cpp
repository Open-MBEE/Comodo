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
 * $Id: DOMSCXMLReader.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include "DOMSCXMLReader.h"
#include "ActionTrace.h"

#include "scxml4cpp/Log.h"
#include "scxml4cpp/StateMachine.h"
#include "scxml4cpp/Action.h"
#include "scxml4cpp/Activity.h"
#include "scxml4cpp/State.h"
#include "scxml4cpp/StateHistory.h"
#include "scxml4cpp/StateCompound.h"
#include "scxml4cpp/StateParallel.h"
#include "scxml4cpp/StateAtomic.h"
#include "scxml4cpp/Event.h"

#include <iostream>


using namespace scxml4cpp;


DOMSCXMLReader::DOMSCXMLReader() : 
    mActions(NULL),
    mActivities(NULL),
    mSM(NULL),
    mDoc(NULL),
    mDomParser(NULL),
    mErrHandler(NULL),
    XML_SCXML(NULL),
    XML_INITIAL(NULL),
    XML_STATE(NULL),
    XML_PARALLEL(NULL),
    XML_FINAL(NULL),
    XML_ONENTRY(NULL),
    XML_ONEXIT(NULL),
    XML_TRANSITION(NULL),
    XML_HISTORY(NULL),
    XML_INVOKE(NULL),
    XML_ATTR_INITIAL(NULL),
    XML_ATTR_NAME(NULL),
    XML_ATTR_ID(NULL),
    XML_ATTR_EVENT(NULL),
    XML_ATTR_COND(NULL),
    XML_ATTR_TARGET(NULL),
    XML_ATTR_TYPE(NULL),
    XML_ACTION(NULL),
    XML_ATTR_ACTION_NAME(NULL),
    XML_ATTR_ACTION_PARAM(NULL),
    SCXML_HISTORY_DEEP(NULL),
    SCXML_HISTORY_SHALLOW(NULL),
    USER_DATA_KEY_STATE(NULL),
    DEFAULT_SCXML_NAME(NULL)
{
    FILE_LOG(logTRACE);

    try 
	{
	XMLPlatformUtils::Initialize();  // Initialize Xerces infrastructure
	}
    catch (XMLException& e) 
	{
	char* message = XMLString::transcode(e.getMessage());
	FILE_LOG(logERROR) << "XML toolkit initialization error: " << message;
	XMLString::release(&message);
	// throw exception here to return ERROR_XERCES_INIT
	}

    // SCXML Element names
    XML_SCXML = XMLString::transcode("scxml");
    XML_INITIAL = XMLString::transcode("initial");
    XML_STATE = XMLString::transcode("state");
    XML_PARALLEL = XMLString::transcode("parallel");
    XML_FINAL = XMLString::transcode("final");
    XML_ONENTRY = XMLString::transcode("onentry");
    XML_ONEXIT = XMLString::transcode("onexit");
    XML_TRANSITION = XMLString::transcode("transition");
    XML_HISTORY = XMLString::transcode("history");
    XML_INVOKE = XMLString::transcode("invoke");
    // SCXML Element's attributes names
    XML_ATTR_INITIAL = XMLString::transcode("initial");
    XML_ATTR_NAME = XMLString::transcode("name");
    /*	
      XML_ATTR_XMLNS = "xmlns";
      XML_ATTR_VERSION = "version";
      XML_ATTR_PROFILE = "profile";
      XML_ATTR_EXMODE = "exmode";
    */
    XML_ATTR_ID = XMLString::transcode("id");
    XML_ATTR_EVENT = XMLString::transcode("event");
    XML_ATTR_COND = XMLString::transcode("cond");
    XML_ATTR_TARGET = XMLString::transcode("target");
    XML_ATTR_TYPE = XMLString::transcode("type");
    /*
      XML_ATTR_LABEL = "label";
      XML_ATTR_EXPR = "expr";
      XML_ATTR_LEVEL = "level";
      XML_ATTR_ANCHOR = "anchor";
    */
    // Custom Element name (extension to the SCXML Executable Content)
    XML_ACTION = XMLString::transcode("customActionDomain");
    XML_ATTR_ACTION_NAME = XMLString::transcode("name");
    XML_ATTR_ACTION_PARAM = XMLString::transcode("param");

    SCXML_HISTORY_DEEP = XMLString::transcode("deep");
    SCXML_HISTORY_SHALLOW = XMLString::transcode("shallow");
    DEFAULT_SCXML_NAME = XMLString::transcode("DefaultSCXML");
    USER_DATA_KEY_STATE = XMLString::transcode("state");

    /*
     * Create error handler for the parser.
     */
    mErrHandler = (ErrorHandler*) new HandlerBase();

    /*
     * Create and configure the parser.
     */
    mDomParser= new XercesDOMParser(); 
    mDomParser->setValidationScheme(XercesDOMParser::Val_Always);
    mDomParser->setDoNamespaces(true);
    mDomParser->setErrorHandler(mErrHandler);	

}

DOMSCXMLReader::~DOMSCXMLReader() 
{
    FILE_LOG(logTRACE);

    /*
     * Cleanup action traces
     */
    delTraces();

    /*
     * Delete the parser.
     */
    try
	{
	delete mDomParser;
	}
    catch(xercesc::XMLException& e)
	{
	char* message = XMLString::transcode( e.getMessage() );
	FILE_LOG(logERROR) << "Exception deleting domParser: " << message;
	XMLString::release(&message);
	}

    /*
     * Delete the error handler for the parser.
     */
    delete mErrHandler;

    /*
     * Delete the XML tags
     */
    try
	{
	XMLString::release(&XML_SCXML);
	XMLString::release(&XML_INITIAL);
	XMLString::release(&XML_STATE);
	XMLString::release(&XML_PARALLEL);
	XMLString::release(&XML_FINAL);
	XMLString::release(&XML_ONENTRY);
	XMLString::release(&XML_ONEXIT);
	XMLString::release(&XML_TRANSITION);
	XMLString::release(&XML_HISTORY);
	XMLString::release(&XML_INVOKE);
	XMLString::release(&XML_ATTR_INITIAL);
	XMLString::release(&XML_ATTR_NAME);
	XMLString::release(&XML_ATTR_ID);
	XMLString::release(&XML_ATTR_EVENT);
	XMLString::release(&XML_ATTR_COND);
	XMLString::release(&XML_ATTR_TARGET);
	XMLString::release(&XML_ATTR_TYPE);
	XMLString::release(&XML_ACTION);
	XMLString::release(&XML_ATTR_ACTION_NAME);
	XMLString::release(&XML_ATTR_ACTION_PARAM);
	XMLString::release(&SCXML_HISTORY_DEEP);
	XMLString::release(&SCXML_HISTORY_SHALLOW);
	XMLString::release(&USER_DATA_KEY_STATE);
	XMLString::release(&DEFAULT_SCXML_NAME);
	}
    catch(...)
	{
	FILE_LOG(logERROR) << "Unknown exception encountered while releasing XML targs";
	}

    // Terminateto use xerces after release of memory
    try 
	{
	XMLPlatformUtils::Terminate();  
	}
    catch (xercesc::XMLException& e)
	{
	char* message = XMLString::transcode(e.getMessage());
	FILE_LOG(logERROR) << "XML toolkit teardown error: " << message;
	XMLString::release(&message);
	}
    catch (...)
	{
	FILE_LOG(logERROR) << "Unknown exception encountered while terminating";
	}
}

void DOMSCXMLReader::addTraces(State* s)
{
    FILE_LOG(logTRACE);
 
    if (s != NULL) 
	{
	std::string id1 = "TRACE Enter " + s->getId();
	Action* actionTrace1 = new ActionTrace(id1);
	mTraces.push_back(actionTrace1);
	s->addEntryAction(actionTrace1);

	std::string id2 = "TRACE Exit " + s->getId();
	Action* actionTrace2 = new ActionTrace(id2);
	mTraces.push_back(actionTrace2);
	s->addExitAction(actionTrace2);
	}
}

void DOMSCXMLReader::delTraces()
{
    FILE_LOG(logTRACE);

    std::list<Action*>::iterator it;
    for (it = mTraces.begin(); it != mTraces.end(); it++) 
	{
	Action* t = *it;
	delete t;
	}
}

void DOMSCXMLReader::parseActivities()
{
    FILE_LOG(logTRACE);

    DOMElement* rootElement = mDoc->getDocumentElement();		
    DOMNodeList* nl = rootElement->getElementsByTagName(XML_INVOKE);
    if (nl != NULL && nl->getLength() > 0) 
	{
	for(XMLSize_t i = 0 ; i < nl->getLength(); i++) 
	    {
	    DOMElement* el = (DOMElement*)nl->item(i);
	    DOMElement* parentElement = (DOMElement*)el->getParentNode();
	    const XMLCh* parentElementName = parentElement->getNodeName();
	    if (XMLString::equals(parentElementName, XML_STATE) || 
		XMLString::equals(parentElementName, XML_PARALLEL) || 
		XMLString::equals(parentElementName, XML_HISTORY)) 
		{
		State* s = (State*) parentElement->getUserData(USER_DATA_KEY_STATE);
		if (el->hasAttribute(XML_ATTR_ID)) 
		    {
		    const XMLCh* activityId = el->getAttribute(XML_ATTR_ID);
		    if (activityId != NULL) 
			{
			Activity* a = findActivity(activityId);
			if (a != NULL) 
			    {
			    s->addActivity(a);	
			    } 
			else 
			    {
			    // @TODO log missing Activity
			    }
			}
		    }
		}
	    }
	}
}

void DOMSCXMLReader::parseActions(const XMLCh* tag) 
{
    FILE_LOG(logTRACE);

    DOMElement* rootElement = mDoc->getDocumentElement();		

    /*
     * Get a list (nl) of entry or exit nodes depending on the tag
     */
    DOMNodeList* nl = rootElement->getElementsByTagName(tag);
    if (nl != NULL && nl->getLength() > 0) 
	{
	for(XMLSize_t i = 0 ; i < nl->getLength(); i++) 
	    {
	    DOMElement* el = (DOMElement*)nl->item(i);
	    DOMElement* parentElement = (DOMElement*)el->getParentNode();
	    const XMLCh* parentElementName = parentElement->getNodeName();
	    /*
	     * For each entry/exit node (el) retrieve the parent state name
	     */
	    if (XMLString::equals(parentElementName, XML_STATE) || 
		XMLString::equals(parentElementName, XML_PARALLEL) || 
		XMLString::equals(parentElementName, XML_HISTORY) ) 
		{
		State* s = (State*) parentElement->getUserData(USER_DATA_KEY_STATE);
		/*
		 * Retrieve a list of ACTION elements (nl1) from the entry/exit node
		 */
		// @TODO retrieve actionId
		DOMNodeList* nl1 = findCustomActionXMLElement(el);
		//DOMNodeList* nl1 = el->getElementsByTagName(XML_ACTION);
		if (nl1 != NULL && nl1->getLength() > 0) 
		    {
		    for(XMLSize_t j = 0 ; j < nl1->getLength(); j++) 
			{
			DOMElement* el1 = (DOMElement*)nl1->item(j);
			/*
			 * el1 is an ACTION element, now parse the parameters
			 */
			if (el1->hasAttribute(XML_ATTR_ACTION_NAME)) 
			    {
			    const XMLCh* actionId = el1->getAttribute(XML_ATTR_ACTION_NAME);
			    if (actionId != NULL) 
				{
				//std::cout << "hasActionId" << XMLString::transcode(actionId) << std::endl;
				Action* a = findAction(actionId);
				if (a != NULL) 
				    {
				    //std::cout << a->getId() << std::endl;;
				    if (XMLString::equals(tag, XML_ONENTRY)) 
					{
					s->addEntryAction(a);
					} 
				    else 
					{
					s->addExitAction(a);
					}
				    
				    if (el1->hasAttribute(XML_ATTR_ACTION_PARAM)) 
					{
					const XMLCh* actionParam = el1->getAttribute(XML_ATTR_ACTION_PARAM);
					if (actionParam != NULL) 
					    {
					    //std::cout << "hasActionParam = " << XMLString::transcode(actionParam) << std::endl;
					    char* str = XMLString::transcode(actionParam);
					    a->setParam(str);
					    XMLString::release(&str);
					    }
					} 
				    else 
					{
					//std::cout << "Action " << a->getId() << " has NO Param " << std::endl;
					}
				    } 
				else 
				    {
				    // @TODO log error: missing action
				    }
				}
			    }
			}
		    }				
		}
	    }
	}
}

void DOMSCXMLReader::resolveInitialStates() 
{
    FILE_LOG(logTRACE);

    // set initial state for SCXML
    DOMElement* e = mDoc->getDocumentElement();		
    const XMLCh* eName = e->getNodeName();
    if (XMLString::equals(eName,XML_SCXML))
	{
	if (e->hasAttribute(XML_ATTR_INITIAL)) 
	    {
	    const XMLCh* initialStateId = e->getAttribute(XML_ATTR_INITIAL);
	    State* s = findTargetState(initialStateId);
	    if (s != NULL) 
		{
		s->setIsInitial(true);
		mSM->setInitialState(s, NULL);
		}
	    }
	}

    // set initial state for composite and parallel states
    std::list<DOMElement*>::iterator it;
    for (it = mStates.begin(); it != mStates.end(); it++) 
	{
	e = *it;
	State* s = (State*)e->getUserData(USER_DATA_KEY_STATE);
	if (s->isCompound() || s->isParallel()) 
	    {
	    if (e->hasAttribute(XML_ATTR_INITIAL)) 
		{
		const XMLCh* initialStateId = e->getAttribute(XML_ATTR_INITIAL);
		State* t = findTargetState(initialStateId);
		if (t != NULL) 
		    {
		    t->setIsInitial(true);
		    /*
		     * @TODO add look-up for initial transition action
		     */
		    s->setInitialState(t, NULL);
		    }
		}
	    }
	}
}


Action* DOMSCXMLReader::findAction(const XMLCh* actionId) 
{
    FILE_LOG(logTRACE);

    if (mActions == NULL || actionId == NULL) 
	{
	return NULL;
	}

    if (XMLString::stringLen(actionId) == 0) 
	{
	return NULL;
	}
	
    std::list<Action*>::iterator it;
    for (it = mActions->begin(); it != mActions->end(); it++) 
	{
	Action* a = *it;
	char* str = XMLString::transcode(actionId);
	if (a->getId().compare(str) == 0) 
	    {
	    //std::cout << "====> Action " + a->getId() + " found!\n";
	    XMLString::release(&str);
	    return a;
	    }
	XMLString::release(&str);
	}
	
    return NULL;
}

DOMNodeList* DOMSCXMLReader::findCustomActionXMLElement(DOMElement* elementList) 
{
    FILE_LOG(logTRACE);

    DOMNodeList* nodeList = NULL;

    std::list<Action*>::iterator it;
    for (it = mActions->begin(); it != mActions->end(); it++) 
	{
        Action* a = *it;
        std::string xmlElementName = "customActionDomain:"+a->getId();
	XMLCh* str = XMLString::transcode(xmlElementName.c_str());
        nodeList = elementList->getElementsByTagName(str);
        if (nodeList != NULL && nodeList->getLength() > 0) 
	    {
	    //std::cout << "====> Found CustomAction XML element " + xmlElementName + "\n";
	    XMLString::release(&str);
	    return nodeList;
	    }
	XMLString::release(&str);
	}

    return nodeList;
}

Activity* DOMSCXMLReader::findActivity(const XMLCh* activityId) 
{
    FILE_LOG(logTRACE);

    if (mActivities == NULL || activityId == NULL) 
	{
	return NULL;
	}
    if (XMLString::stringLen(activityId) == 0) 
	{
	return NULL;
	}

    std::list<Activity*>::iterator it;
    for (it = mActivities->begin(); it != mActivities->end(); it++) 
	{
	Activity* a = *it;
	char* str = XMLString::transcode(activityId);
	if (a->getId().compare(str) == 0) 
	    {
	    XMLString::release(&str);
	    return a;
	    }
	XMLString::release(&str);
	}

    return NULL;
}

State* DOMSCXMLReader::findTargetState(const XMLCh* targetId) 
{
    FILE_LOG(logTRACE);

    std::list<DOMElement*>::iterator it;
    for (it = mStates.begin(); it != mStates.end(); it++) 
	{
	DOMElement* e = *it;
	State* s = (State*)e->getUserData(USER_DATA_KEY_STATE);
	char* str = XMLString::transcode(targetId);
	if (s->getId().compare(str) == 0) 
	    {
	    XMLString::release(&str);
	    return s;
	    }
	XMLString::release(&str);
	}
    return NULL;
}

void DOMSCXMLReader::parseTransitions() 
{
    FILE_LOG(logTRACE);
 
    DOMElement* rootElement = mDoc->getDocumentElement();		
    
    // Initial Pseudo-States
    DOMNodeList* nl = rootElement->getElementsByTagName(XML_TRANSITION);
    if (nl != NULL && nl->getLength() > 0) 
	{
	for (XMLSize_t i = 0 ; i < nl->getLength(); i++) 
	    {
	    DOMElement* el = (DOMElement*)nl->item(i);

	    //Event event = NULL;
	    Event event;
	    const XMLCh* eventId = NULL;
	    if (el->hasAttribute(XML_ATTR_EVENT)) 
		{
		eventId = el->getAttribute(XML_ATTR_EVENT);
		if (eventId != NULL) 
		    {
		    // @TODO what about the type?
		    char* str = XMLString::transcode(eventId);
		    //event = new Event(str, Event::CHANGE_EVENT);
		    event.setId(str);
		    event.setType(Event::CHANGE_EVENT);
		    XMLString::release(&str);
		    }
		}

	    Action* cond = NULL;
	    const XMLCh* condId = NULL;
	    if (el->hasAttribute(XML_ATTR_COND)) 
		{
		condId = el->getAttribute(XML_ATTR_COND);
		XMLCh* str = XMLString::transcode("");
		if (!XMLString::equals(condId, str)) 
		    {
		    // @TODO replace with real condition
		    cond = findAction(condId);
		    if (cond == NULL) 
			{
			// @TODO log error
			}
		    }
		XMLString::release(&str);
		}
	    
	    // @TODO support multiple targets for parallel states
	    State* target = NULL;
	    const XMLCh* targetId = NULL;
	    if (el->hasAttribute(XML_ATTR_TARGET)) 
		{
		targetId = el->getAttribute(XML_ATTR_TARGET);
		}
	    if (targetId != NULL) 
		{
		target = findTargetState(targetId);
		}

	    Action* transAction = NULL;
			
	    // @TODO retrieve actionId

	    DOMNodeList* nl1 = findCustomActionXMLElement(el);
	    //DOMNodeList* nl1 = el->getElementsByTagName(XML_ACTION);
	    if (nl1 != NULL && nl1->getLength() > 0) 
		{
		DOMElement* el1 = (DOMElement*)nl1->item(0);
		if (el1->hasAttribute(XML_ATTR_ACTION_NAME)) 
		    {
		    const XMLCh* actionId = el1->getAttribute(XML_ATTR_ACTION_NAME);
		    if (actionId != NULL) 
			{
			// @TODO replace with real action
			transAction = findAction(actionId);
			if (transAction == NULL) 
			    {
			    // @TODO log error
			    }
			}
		    }
		}
	    
	    State* source = NULL;
	    DOMElement* parentElement = (DOMElement*)el->getParentNode();
	    const XMLCh* parentElementName = parentElement->getNodeName();
	    if (XMLString::equals(parentElementName,XML_STATE) || 
		XMLString::equals(parentElementName, XML_PARALLEL) || 
		XMLString::equals(parentElementName, XML_HISTORY)) 
		{
		// Transition from a Compound, Atomic, Parallel, History state
		source = (State*) parentElement->getUserData(USER_DATA_KEY_STATE);
		source->addTransition(target, event, cond, transAction);
		} 
	    else if (XMLString::equals(parentElementName, XML_INITIAL)) 
		{
		// Transition from Initial pseudo-state
		if (target != NULL) 
		    {
		    target->setIsInitial(true);
		    DOMElement* parentStateElement = (DOMElement*)parentElement->getParentNode();
		    const XMLCh* parentStateElementName = parentStateElement->getNodeName();
		    if (XMLString::equals(parentStateElementName, XML_STATE) || 
			XMLString::equals(parentStateElementName, XML_PARALLEL)) 
			{
			State* parentState = (State*) parentStateElement->getUserData(USER_DATA_KEY_STATE);
			parentState->setInitialState(target, transAction);	
			} 
		    else if (XMLString::equals(parentStateElementName, XML_SCXML)) 
			{
			mSM->setInitialState(target, transAction);	
			}
		    }
		} 
	    }
	}
}

/*
 * To be called after the parents have been resolved!
 */
void DOMSCXMLReader::resolveHistory() 
{
    FILE_LOG(logTRACE);

    std::list<DOMElement*>::iterator it;
    for (it = mStates.begin(); it != mStates.end(); it++) 
	{
	DOMElement* e = *it;
	State* s = (State*)e->getUserData(USER_DATA_KEY_STATE);
	if (s->isHistory()) 
	    {
	    State* p = s->getParent();
	    if (p != NULL) 
		{
		p->setHistory(static_cast<StateHistory*>(s));
		}
	    }
	}
}

void DOMSCXMLReader::resolveParents() 
{
    FILE_LOG(logTRACE);

    std::list<DOMElement*>::iterator it;
    for (it = mStates.begin(); it != mStates.end(); it++) 
	{
	DOMElement* e = *it;
	State* s = (State*)e->getUserData(USER_DATA_KEY_STATE);
	DOMElement* parentElement = (DOMElement*)e->getParentNode();

	const XMLCh* parentElementName = parentElement->getNodeName();
	if (XMLString::equals(parentElementName, XML_SCXML)) 
	    {
	    s->setParent(NULL);
	    if (s->isParallel()) 
		{
		mSM->addParallel(s);
		} 
	    else 
		{
		mSM->addSubstate(s);
		}
	    } 
	else if (XMLString::equals(parentElementName,XML_STATE) || XMLString::equals(parentElementName, XML_PARALLEL)) 
	    {
	    State* parent = (State*)parentElement->getUserData(USER_DATA_KEY_STATE);
	    s->setParent(parent);
	    if (parent->isParallel() || parent->isCompound()) 
		{
		parent->addSubstate(s);					
		}
	    } 
	else 
	    {
	    s->setParent(NULL);	
	    // @TODO exception
	    }			
	}
}


bool DOMSCXMLReader::isStateCompound(DOMElement* e) 
{
    FILE_LOG(logTRACE);

    DOMNodeList* nl = e->getChildNodes();
    for(XMLSize_t i = 0 ; i < nl->getLength(); i++) 
	{
	const XMLCh* name = nl->item(i)->getNodeName();
	if (XMLString::equals(name, XML_STATE)) return true;
	if (XMLString::equals(name, XML_PARALLEL)) return true;
	}
    return false;
}


void DOMSCXMLReader::parseStates() 
{
    FILE_LOG(logTRACE);

    DOMElement* rootElement = mDoc->getDocumentElement();		

    // Compound and Atomic States
    DOMNodeList* nl = rootElement->getElementsByTagName(XML_STATE);
    if (nl != NULL && nl->getLength() > 0) 
	{
	for (XMLSize_t i = 0 ; i < nl->getLength(); i++) 
	    {
	    DOMElement* el = (DOMElement*)nl->item(i);
	    if (el->hasAttribute(XML_ATTR_ID)) 
		{
		State* s = NULL;
		char* str = XMLString::transcode(el->getAttribute(XML_ATTR_ID));
		if (isStateCompound(el)) 
		    {
		    s = new StateCompound(str);
		    }
		else 
		    {
		    s = new StateAtomic(str);
		    }
		XMLString::release(&str);
		el->setUserData(USER_DATA_KEY_STATE, s, NULL);
		mStates.push_back(el);
		}
	    //addTraces(s);
	    }
	}

    // Parallel States
    nl = rootElement->getElementsByTagName(XML_PARALLEL);
    for (XMLSize_t i = 0 ; i < nl->getLength(); i++) 
	{
	DOMElement* el = (DOMElement*)nl->item(i);
	if (el->hasAttribute(XML_ATTR_ID)) 
	    {
	    char* id = XMLString::transcode(el->getAttribute(XML_ATTR_ID));
	    State* s = new StateParallel(id);
	    el->setUserData(USER_DATA_KEY_STATE, s, NULL);
	    mStates.push_back(el);
	    XMLString::release(&id);
	    }
	//addTraces(s);
	}

    // History States		
    nl = rootElement->getElementsByTagName(XML_HISTORY);
    for (XMLSize_t i = 0 ; i < nl->getLength(); i++) 
	{
	DOMElement* el = (DOMElement*)nl->item(i);	
	if (el->hasAttribute(XML_ATTR_ID)) 
	    {
	    char* id = XMLString::transcode(el->getAttribute(XML_ATTR_ID));
	    StateHistory::HistoryType hType = StateHistory::Shallow;
	    if (el->hasAttribute(XML_ATTR_TYPE)) 
		{
		if (XMLString::equals(el->getAttribute(XML_ATTR_TYPE), SCXML_HISTORY_DEEP)) 
		    {
		    hType = StateHistory::Deep;
		    } 
		else if (XMLString::equals(el->getAttribute(XML_ATTR_TYPE), SCXML_HISTORY_SHALLOW)) 
		    {
		    hType = StateHistory::Shallow;
		    }
		}
	    State* s = new StateHistory(id, hType);
	    el->setUserData(USER_DATA_KEY_STATE, s, NULL);
	    mStates.push_back(el);
	    XMLString::release(&id);
	    }

	//addTraces(s);
	}

    // Final States
    nl = rootElement->getElementsByTagName(XML_FINAL);
    for (XMLSize_t i = 0 ; i < nl->getLength(); i++) 
	{
	DOMElement* el = (DOMElement*)nl->item(i);
	if (el->hasAttribute(XML_ATTR_ID)) 
	    {
	    char* id = XMLString::transcode(el->getAttribute(XML_ATTR_ID));
	    State* s = new StateAtomic(id);
	    s->setIsFinal(true);
	    el->setUserData(USER_DATA_KEY_STATE, s, NULL);
	    mStates.push_back(el);
	    XMLString::release(&id);
	    }
	//addTraces(s);
	}
	
}

void DOMSCXMLReader::parseDoc() throw(std::runtime_error)
{
    FILE_LOG(logTRACE);

    // Get the top-level element: NAme is "root". No attributes for "root"
    DOMElement* rootElement = mDoc->getDocumentElement();
    if (!rootElement) throw(std::runtime_error("empty XML document"));

    if (XMLString::equals(rootElement->getTagName(), XML_SCXML))
	{
	const XMLCh* smName = DEFAULT_SCXML_NAME;
	if (rootElement->hasAttribute(XML_ATTR_NAME))
	    {
	    smName = rootElement->getAttribute(XML_ATTR_NAME);
	    }

	char* str = XMLString::transcode(smName);
	mSM = new StateMachine(str);
	XMLString::release(&str);

	parseStates();
	resolveParents();
	parseTransitions();
	resolveInitialStates();
	resolveHistory();

	parseActions(XML_ONENTRY);
	parseActions(XML_ONEXIT);

	parseActivities();
	}
}

StateMachine* DOMSCXMLReader::read(const std::string& filename, 
				   std::list<Action*>* actions, 
				   std::list<Activity*>* activities) throw(std::runtime_error)
{
    FILE_LOG(logTRACE);
    FILE_LOG(logDEBUG) << "Parsing " << filename << " ...";

    try 
	{
	mDomParser->parse(filename.c_str());
	mDoc = mDomParser->getDocument();
	}
    catch (const XMLException& toCatch) 
	{
	char* message = XMLString::transcode(toCatch.getMessage());
	std::cout << "Exception message is: \n" << message << "\n";
	XMLString::release(&message);
	return NULL;
        }
    catch (const DOMException& toCatch) 
	{
	char* message = XMLString::transcode(toCatch.msg);
	std::cout << "Exception message is: \n" << message << "\n";
	XMLString::release(&message);
	return NULL;
        }
    catch (...) 
	{
	std::cout << "Unexpected Exception \n";
	return NULL;
        }
    mActions = actions;
    mActivities = activities;
	

    //mSM = NULL;
    parseDoc();
/*
    mDoc->release();
*/
    return mSM;
}
