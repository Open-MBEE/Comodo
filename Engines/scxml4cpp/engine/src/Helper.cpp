/*
 *    scampl4cpp/engine
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
 * $Id: Helper.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include "Helper.h"
#include "Action.h"
#include "State.h"
#include "StateHistory.h"
#include "StateMachine.h"
#include "Transition.h"
#include "Event.h"
#include "ExecutableContent.h"
#include "Log.h"

#include <algorithm>


using namespace scxml4cpp;


Helper::Helper()
{
    FILE_LOG(logTRACE);
}

Helper::~Helper()
{
    FILE_LOG(logTRACE);
}



std::string Helper::printStateType(State* s) 
{
    FILE_LOG(logTRACE);

    std::string str = "";
    if (s == NULL) return str;

    switch (s->getType()) 
	{
	case State::History:
	    str = "History";
	    break;
	case State::Atomic:
	    str = "Atomic";
	    break;
	case State::Compound:
	    str = "Compound";
	    break;
	case State::Parallel:
	    str = "Parallel";	
	    break;
        default:
	    str = "undefined";
	    break;
    }
    return str;
}


std::string Helper::printTransition(Transition* t)
{
    FILE_LOG(logTRACE);

    std::string str = "";
    if (t == NULL) return str;

    /*
     * sourceState - [cond] event / action -> targetState
     */
    str += "  ";
    State* source = t->getSource();
    if (source == NULL) {
        str += "NULL";
    } else {
        str += source->getId();
    }
	
    str += " - ";

    std::list<Action*> conditions = t->getConditions()->getActions();
    if (conditions.size() != 0) {
        str += "[ ";
	std::list<Action*>::iterator it;
	for (it = conditions.begin(); it != conditions.end(); it++) {
	    Action* c = *it;
	    str += c->getId() + " ";
	}
	str += "]";
    }

    Event e = t->getEvent();
    if (e.isNull() == false) {
        str += e.getId();
    }

    str += " / ";

    std::list<Action*> actions = t->getActions()->getActions();
    if (actions.size() != 0) {
	std::list<Action*>::iterator it;
	for (it = actions.begin(); it != actions.end(); it++) {
	    Action* a = *it;
	    str += a->getId() + " ";
	}
    }
	
    str += " -> ";
	
    std::list<State*> targets = t->getTargets();
    std::list<State*>::iterator it;
    for (it = targets.begin(); it != targets.end(); it++) {
        State* s = *it;
	str += s->getId() + " ";
    }

    str += "\n";

    return str;
}

std::string Helper::printTransitions(std::list<Transition*>& transitions)
{
    FILE_LOG(logTRACE);

    std::string str = "";
    std::list<Transition*>::iterator it;
    for (it = transitions.begin(); it != transitions.end(); it++) {
        Transition* t = *it;
	str += printTransition(t);
    }
    return str;
}


std::string Helper::printTargetState(Transition* t)
{
    FILE_LOG(logTRACE);

    std::string str = "";
    if (t == NULL) return str;

    std::list<State*> targets = t->getTargets();
    std::list<State*>::iterator it;
    for (it = targets.begin(); it != targets.end(); it++) {
        State* s = *it;
	str += s->getId() + " ";
    }
    return str;
}

std::string Helper::printTargetStates(std::list<Transition*>& transitions)
{
    FILE_LOG(logTRACE);

    std::string str = "";
    std::list<Transition*>::iterator it;
    for (it = transitions.begin(); it != transitions.end(); it++) {
        Transition* t = *it;
	str += printTargetState(t);
    }
    return str;
}

std::string Helper::printActions(std::list<Action*>& actions)
{
    FILE_LOG(logTRACE);

    std::string str = "";
    std::list<Action*>::iterator it;	
    for (it = actions.begin(); it != actions.end(); it++) {
        Action* a = *it;
	str += a->getId() + " (param=" + a->getParam() + ")" + " ";
    }	
    return str;
}

std::string Helper::printStates(State* s)
{
    FILE_LOG(logTRACE);

    std::string str = "";
    if (s == NULL) return str;

    std::string parentId = "none";
    if (s->getParent() != NULL) {
        parentId = s->getParent()->getId();
    }

    std::string substatesId = "";
    std::list<State*> substates = s->getSubstates();
    std::list<State*>::iterator it;
    for (it = substates.begin() ; it != substates.end() ; it++ ) {
        State* substate = *it;
	substatesId += "  " + substate->getId() + "\n";
    }
    
    std::string historyStateName = "";
    if (s->getHistory() != NULL) {
        historyStateName = s->getHistory()->getId();
    }

    str += "\n\n";
    str += "State            : " + s->getId() + "\n";
    str += " type            : " + printStateType(s) + "\n";
    str += " isInitial       : " + std::string((s->isInitial()==true)?"Yes":"No") + "\n";
    str += " isFinal         : " +  std::string((s->isFinal()==true)?"Yes":"No") + "\n";
    str += " entryActions    : " + printActions(s->getEntryActions()->getActions()) + "\n";
    str += " doActions       : \n";
    str += " exitActions     : " + printActions(s->getExitActions()->getActions()) + "\n";
    str += " parentState     : " + parentId + "\n";
    str += " initialState    : " + printTargetStates(s->getInitialTrans()) + "\n";
    str += " historyState    : " + historyStateName + "\n";
    str += " substates       : \n";
    str += substatesId;
    str += " transitions     : \n";
    str += printTransitions(s->getTransitions()); // one transition per line
	
    /*
     * Explore substates in detail.
     */
    for (it = substates.begin() ; it != substates.end() ; it++ ) {
        State* substate = *it;
	str += printStates(substate);
    }

    return str;
}

std::string Helper::printStateMachine(StateMachine* sm)
{
    FILE_LOG(logTRACE);

    std::string str = "";
    if (sm == NULL) return str;

    str += "\n\n";
    str += "State Machine    : " + sm->getId() + "\n";
    str += " initialState    : " + printTargetStates(sm->getInitialTrans())+"\n";

    /*
     * Explore all states and parallel regions.
     */
    if (sm->getSubstates().size() != 0) {
        std::list<State*>::iterator it;
	for (it = sm->getSubstates().begin(); it != sm->getSubstates().end(); it++) {
	    State* s = *it;
	    str += printStates(s);
	}
    }
	
    if (sm->getParallel().size() != 0) {
        std::list<State*>::iterator it;
	for (it = sm->getParallel().begin(); it != sm->getParallel().end(); it++) {
	    State* s = *it;
	    str += printStates(s);
	}
    }
	
    return str;	
}

std::string Helper::formatStatus(std::set<State*>& status)
{
    FILE_LOG(logTRACE);

    std::string str = "";
    std::set<State*>::iterator it;
    for (it = status.begin(); it != status.end(); it++) {
        State* s = *it;
	if (s->isAtomic()) {
	    std::list<State*> ancestors = getAncestorsList(s);
	    std::list<State*>::iterator it1;
	    for (it1 = ancestors.begin(); it1 != ancestors.end(); it1++) {
	        State* s1 = *it1;
		str += s1->getId();
		//fix this!!!
		//if ( it1 + 1 != ancestors.end() ) {
		str += "/";
		//}
	    }
	    str += " ";
	}
    }
    return str;
}


bool Helper::isDescendant(State* s, State* p)
{
    FILE_LOG(logTRACE);

    if (s == NULL) {
        return false;
    } else if (p == NULL) {
        return true;
    }

    State* parent = s->getParent();
    while (parent != NULL) {
        if (parent == p) {
	    return true;
	}
	parent = parent->getParent();
    }

    return false;
}


int Helper::countParents(State* s)
{
    FILE_LOG(logTRACE);

    int numParents = 0;
    while (s != NULL){
        s = s->getParent();
	numParents++;
    }
    return numParents;
}

std::list<State*> Helper::getProperAncestors(State* state, State* upperBound)
{
    FILE_LOG(logTRACE);

    std::list<State*> anc;
    // proper ancestor does not include given state,
    // therefore starts from the parent of the given state
    if (state == NULL) return anc;
    State* s = state->getParent();
    while (s != NULL) {
        if (upperBound != NULL && upperBound == s)
	    break;
	if (std::count(anc.begin(), anc.end(), s) > 0 )
	    break;
	else
	    anc.push_back(s);
	s = s->getParent();
    }
    return anc;
}

std::set<State*> Helper::getAncestors(std::list<State*>& states)
{
    FILE_LOG(logTRACE);

    std::set<State*> anc;
    std::list<State*>::iterator it;
    std::pair<std::set<State*>::iterator,bool> ret;
    for (it = states.begin(); it != states.end(); it++) {
        State* s = *it;
	while (s != NULL) {
	    ret = anc.insert(s);
	    if (ret.second == false)
		break;
	    s = s->getParent();
	}
    }
    return anc;
}

std::list<State*> Helper::getAncestorsList(std::set<State*>& states)
{
    FILE_LOG(logTRACE);

    std::list<State*> anc;
    std::set<State*>::iterator it;
    for (it = states.begin(); it != states.end(); it++) {
        State* s = *it;
	while (s != NULL) {
	    if (std::count(anc.begin(), anc.end(), s) > 0)
		break;
	    else
		anc.push_back(s);
	}
    }
    return anc;
}


std::set<State*> Helper::getAncestors(State* state)
{
    FILE_LOG(logTRACE);

    std::set<State*> anc;
    State* s;
    std::pair<std::set<State*>::iterator,bool> ret;
    s = state;
    while (s != NULL) {
        ret = anc.insert(s);
	if (ret.second == false)
	    break;
	s = s->getParent();
    }
    return anc;
}

std::list<State*> Helper::getAncestorsList(State* state)
{
    FILE_LOG(logTRACE);

    std::list<State*> anc;
    State* s = state;
    while (s != NULL) {
        anc.push_back(s);
	s = s->getParent();
    }
    return anc;
}


State* Helper::findLeastCommonAncestor(State* s1, State* s2)
{
    FILE_LOG(logTRACE);

    if (s1 == s2) {
        return s1; //self-transition
    } else if (isDescendant(s1, s2)) {
        return s2; // s2 is ancestor of s1
    } else if (isDescendant(s2, s1)) {
        return s1; // s1 is ancestor of s2
    }

    // build list of ancestors of s1
    std::list<State*> parents;
    State* tmp = s1;
    while ((tmp = tmp->getParent()) != NULL)
	parents.push_back(tmp);

    // get the ancestors of s2 until one is found which is common with s1
    tmp = s2;
    while ((tmp = tmp->getParent()) != NULL) {
        //test redundant add = common ancestor
        if (std::count(parents.begin(), parents.end(), tmp ) > 0)
	    return tmp;
    }

    return NULL;
}

bool Helper::eventMatch(const Event& e1, const Event& e2)
{
    FILE_LOG(logTRACE);

    if (e1.isNull() && e2.isNull()) {
        return true;
    } else if (e1.isNull())  {
        return false;
    } else if (e2.isNull())  {
        return false;
    }
    return ((e1.getId().compare(e2.getId()) == 0) && (e1.getType() == e2.getType()));
}

std::list<State *> Helper::getAtomicStates(std::set<State*>& states)
{
    FILE_LOG(logTRACE);

    std::list<State*> atomicStates;
    std::set<State*>::iterator it;
    for( it = states.begin() ; it != states.end() ; it++ ){
        State* s = *it;
	if (s->isAtomic() == true) {
	    atomicStates.push_back(s);
	}
    }
    return atomicStates;
}

std::list<State *> Helper::getAtomicStates(std::list<State*>& states)
{
    FILE_LOG(logTRACE);

    std::list<State*> atomicStates;
    std::list<State*>::iterator it;
    for (it = states.begin(); it != states.end(); it++){
        State* s = *it;
	if (s->isAtomic() == true) {
	    atomicStates.push_back(s);
	}
    }
    return atomicStates;
}

bool Helper::isPreempted(State* s, std::list<Transition*>& transitions)
{
    FILE_LOG(logTRACE);

    /*
     * Return true if a transition 't' in 'transitions' exits an ancestor of state 's'.
     *
     * In this case, taking 't' will pull the state machine out of 's' and we say that
     * it preempts the selection of a transition from 's'. Such preemption will occur
     * only if 's' is a descendant of a parallel region and 't' exits that region.
     * If we did not do this preemption check, we could end up in an illegal configuration,
     * namely one in which there were multiple active states that were not all descendants
     * of a common parallel ancestor.
     */
    bool preempted = false;
    std::list<Transition*>::iterator it;
    for (it = transitions.begin(); it != transitions.end(); it++) {
        Transition* t = *it;
	State* source = t->getSource();
	std::list<State*> targets = t->getTargets();
	if (targets.empty() == false) {
	    State* LCA = findLeastCommonAncestor(source, targets.front());
	    if (isDescendant(s, LCA) == true) {
	        preempted = true;
		break;
	    }
	}
    }
    return preempted;
}



