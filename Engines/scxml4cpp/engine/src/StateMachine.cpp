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
 * $Id: StateMachine.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include "StateMachine.h"
#include "Transition.h"
#include "State.h"
#include "Helper.h"
#include "Action.h"
#include "Log.h"


using namespace scxml4cpp;


StateMachine::StateMachine(const std::string& id) :
    mId(id)
{
    FILE_LOG(logTRACE);
}

StateMachine::~StateMachine()
{
    FILE_LOG(logTRACE);

    std::list<Transition*>::iterator it1;
    for (it1 = getInitialTrans().begin() ; it1 != getInitialTrans().end() ; it1++) 
	{
	Transition* t = *it1;
	delete t;
	}

    std::list<State*>::iterator it2;
    for (it2 = getSubstates().begin() ; it2 != getSubstates().end() ; it2++) 
	{
	State* s = *it2;
	delete s;
	}
	
    std::list<State*>::iterator it3;
    for (it3 = getParallel().begin() ; it3 != getParallel().end() ; it3++) 
	{
	State* s = *it3;
	delete s;
	}		
}

const std::string& StateMachine::getId() const
{
    FILE_LOG(logTRACE);
    return mId;
}

std::list<Transition*>& StateMachine::getInitialTrans()
{
    FILE_LOG(logTRACE);
    return mInitialTrans;
}

std::list<State*>& StateMachine::getSubstates()
{
    FILE_LOG(logTRACE);
    return mSubstates;
}

std::list<State*>& StateMachine::getParallel()
{
    FILE_LOG(logTRACE);
    return mParallel;
}

void StateMachine::setId(const std::string& id)
{
    FILE_LOG(logTRACE);
    mId = id;
}

void StateMachine::setInitialState(State* initialState, Action* a)
{
    FILE_LOG(logTRACE);
    Transition* t = new Transition();
    t->setSource(NULL);
    t->addTarget(initialState);
    //t->setEvent(NULL);
    t->addAction(a);
    mInitialTrans.push_back(t);
    initialState->setIsInitial(true);
}

void StateMachine::setFinalState(State* finalState)
{
    FILE_LOG(logTRACE);
    finalState->setIsFinal(true);
}

void StateMachine::setSubstates(std::list<State*>& substates)
{
    FILE_LOG(logTRACE);
    mSubstates = substates;
}

void StateMachine::setParallel(std::list<State*>& parallel)
{
    FILE_LOG(logTRACE);
    mParallel = parallel;
}

void StateMachine::addSubstate(State* s)
{
    FILE_LOG(logTRACE);
    mSubstates.push_back(s);
}

void StateMachine::addParallel(State* s)
{
    FILE_LOG(logTRACE);
    mParallel.push_back(s);
}


