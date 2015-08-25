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
 * $Id: Transition.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include "Transition.h"
#include "State.h"
#include "ExecutableContent.h"
#include "Action.h"
#include "Context.h"
#include "Log.h"

using namespace scxml4cpp;

Transition::Transition() :
    mConditions(NULL),
    mActions(NULL),
    mSource(NULL),
    mTargets()
{
    FILE_LOG(logTRACE);

    mConditions = new ExecutableContent();
    mActions = new ExecutableContent();
}

Transition::~Transition()
{
    FILE_LOG(logTRACE);

    delete mConditions;
    delete mActions;
}

void Transition::addTarget(State* s)
{
    FILE_LOG(logTRACE);

    if (s != NULL) {
        mTargets.push_back(s);
    }
}

void Transition::addAction(Action* a)
{
    FILE_LOG(logTRACE);

    if (a != NULL) {
        mActions->addAction(a);
    }
}

void Transition::addCondition(Action *c)
{
    FILE_LOG(logTRACE);

    if (c != NULL) {
        mConditions->addAction(c);
    }
}

bool Transition::isEnabled(Context* c)
{
    FILE_LOG(logTRACE);
    return (mEvent.getId().empty() == true && mConditions->evaluate(c) == true);
}

bool Transition::isEnabled(const Event& e, Context* c)
{
    FILE_LOG(logTRACE);
    return (mHelper.eventMatch(e, mEvent) == true && mConditions->evaluate(c) == true);
}

State* Transition::getSource()
{
    FILE_LOG(logTRACE);
    return mSource;
}

std::list<State*>& Transition::getTargets()
{
    FILE_LOG(logTRACE);
    return mTargets;
}

ExecutableContent* Transition::getConditions()
{
    FILE_LOG(logTRACE);
    return mConditions;
}

Event& Transition::getEvent()
{
    FILE_LOG(logTRACE);
    return mEvent;
}

ExecutableContent* Transition::getActions()
{
    FILE_LOG(logTRACE);
    return mActions;
}


void Transition::setSource(State* source)
{
    FILE_LOG(logTRACE);
    mSource = source;
}


void Transition::setTargets(std::list<State *>& targets)
{
    FILE_LOG(logTRACE);
    mTargets = targets;
}

void Transition::setEvent(const Event& e)
{
    FILE_LOG(logTRACE);
    mEvent = e;
}



