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
 * $Id: State.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include "State.h"
#include "Action.h"
#include "Activity.h"
#include "ExecutableContent.h"
#include "StateHistory.h"
#include "Transition.h"
#include "Event.h"
#include "Log.h"


using namespace scxml4cpp;


State::State(const std::string& id , StateType type) :
    mId(id),
    mType(type),
    mParent(NULL),
    mHistory(NULL),
    mOnEntry(NULL),
    mOnExit(NULL),
    mIsInitial(false),
    mIsFinal(false)
{    
    FILE_LOG(logTRACE);

    mOnEntry = new ExecutableContent();
    mOnExit = new ExecutableContent();
}

State::~State()
{    
    FILE_LOG(logTRACE);

    /*
     * Note that there is no need to delete mParent and mHistory:
     * - the last parent state is deleted by the StateMachine class.
     * - the history state is a normal substate.
     */

    delete mOnEntry;
    delete mOnExit;

    std::list<State*>::iterator it1 = mSubstates.begin();
    for (; it1 != mSubstates.end(); it1++) 
	{
	State* s = (*it1);
	delete s;
	}

    std::list<Transition*>::iterator it2 = mTransitions.begin();
    for (; it2 != mTransitions.end(); it2++) 
	{
	Transition* t = (*it2);
	delete t;
	}

    std::list<Transition*>::iterator it3 = mInitialTrans.begin();
    for (; it3 != mInitialTrans.end(); it3++) 
	{
	Transition* t = (*it3);
	delete t;
	}
}


std::string State::getId() const
{    
    FILE_LOG(logTRACE);
    return mId;
}

State::StateType State::getType() const
{    
    FILE_LOG(logTRACE);
    return mType;
}

State* State::getParent()
{    
    FILE_LOG(logTRACE);
    return mParent;
}

StateHistory* State::getHistory()
{    
    FILE_LOG(logTRACE);
    return mHistory;
}

std::list<Transition*>& State::getInitialTrans()
{    
    FILE_LOG(logTRACE);
    return mInitialTrans;
}

State* State::getInitialState()
{    
    FILE_LOG(logTRACE);

    /*
     * All compound states should have ONE initial state
     */
    if (mInitialTrans.size() > 0) {
        Transition *t = mInitialTrans.front();
	return t->getTargets().front();
    }
    return NULL;
}

std::list<State*>& State::getSubstates()
{    
    FILE_LOG(logTRACE);
    return mSubstates;
}

ExecutableContent* State::getExitActions()
{    
    FILE_LOG(logTRACE);
    return mOnExit;
}

ExecutableContent* State::getEntryActions()
{    
    FILE_LOG(logTRACE);
    return mOnEntry;
}

std::list<Transition*>& State::getTransitions()
{    
    FILE_LOG(logTRACE);
    return mTransitions;
}

std::list<Activity*>& State::getActivities()
{    
    FILE_LOG(logTRACE);
    return mActivities;
}


void State::setId(const std::string& id)
{    
    FILE_LOG(logTRACE);
    mId = mId;
}

void State::setParent(State* parent)
{    
    FILE_LOG(logTRACE);
    mParent = parent;
}

void State::setIsInitial(const bool isInitial)
{    
    FILE_LOG(logTRACE);
    mIsInitial = isInitial;
}

void State::setIsFinal(const bool isFinal)
{    
    FILE_LOG(logTRACE);
    mIsFinal = isFinal;
}

void State::setHistory(StateHistory* h)
{    
    FILE_LOG(logTRACE);
    mHistory = h;
}

void State::setInitialState(State* initialState, Action* a)
{    
    FILE_LOG(logTRACE);

    Transition *t = new Transition();
    t->setSource(NULL);
    t->addTarget(initialState);
    //t->setEvent(NULL);
    //t.addCondition(null);
    t->addAction(a);
    mInitialTrans.push_back(t);
    initialState->setIsInitial(true);
}

void State::setFinalState(State* finalState)
{    
    FILE_LOG(logTRACE);
    finalState->setIsFinal(true);
}

void State::setSubstates(std::list<State*>& substates)
{    
    FILE_LOG(logTRACE);
    mSubstates = substates;
}

void State::setActivities(std::list<Activity*>& activities)
{    
    FILE_LOG(logTRACE);
    mActivities = activities;
}

void State::setTransitions(std::list<Transition *>& transitions)
{
    FILE_LOG(logTRACE);
    mTransitions = transitions;
}

bool State::isInitial()
{
    FILE_LOG(logTRACE);
    return mIsInitial;
}

bool State::isFinal()
{
    FILE_LOG(logTRACE);
    return mIsFinal;
}

bool State::isCompound()
{
    FILE_LOG(logTRACE);
    return (mType == Compound);
}

bool State::isParallel()
{
    FILE_LOG(logTRACE);
    return (mType == Parallel);
}

bool State::isAtomic()
{
    FILE_LOG(logTRACE);
    return (mType == Atomic);
}

bool State::isHistory()
{
    FILE_LOG(logTRACE);
    return (mType == History);
}

void State::addEntryAction(Action* a)
{
    FILE_LOG(logTRACE);
    mOnEntry->addAction(a);
}

void State::addExitAction(Action* a)
{
    FILE_LOG(logTRACE);
    mOnExit->addAction(a);
}


void State::addSubstate(State* s) 
{
    FILE_LOG(logTRACE);
    s->setParent(this);
    mSubstates.push_back(s);
}

void State::addTransition(std::list<State*>& targets, const Event& event, 
			  Action* condition, Action* action) 
{
    FILE_LOG(logTRACE);

    Transition *t = new Transition();
    t->setSource(this);
    t->setTargets(targets);
    t->setEvent(event);
    t->addCondition(condition);
    t->addAction(action);
    mTransitions.push_back(t);
}

void State::addTransition(State* target, const Event& event, Action* condition, Action* action) 
{    
    FILE_LOG(logTRACE);

    std::list<State*> targets;
    if (target) {
        targets.push_back(target);
    }
    addTransition(targets, event, condition, action);
}

void State::addActivity(Activity* activity)
{
    FILE_LOG(logTRACE);

    if (activity != NULL) {
        mActivities.push_back(activity);
    }
}

void State::startActivities()
{
    FILE_LOG(logTRACE);

    std::list<Activity *>::iterator it = mActivities.begin();
    for (; it != mActivities.end(); it++) 
	{
	Activity* a = *it;
	a->start();
	FILE_LOG(logDEBUG) << "Activity " << a->getId() << " started";
	}
}

void State::cancelActivities()
{
    FILE_LOG(logTRACE);

    std::list<Activity *>::iterator it = mActivities.begin();
    for (; it != mActivities.end(); it++) {
        Activity* a = *it;
	if (a->isRunning() == true) {
	    a->stop();
	    FILE_LOG(logDEBUG) << "Activity " << a->getId() << " stopped";
	} else {
	    FILE_LOG(logDEBUG) << "Activity " << a->getId() << " was already stopped";
	}
    }
}



