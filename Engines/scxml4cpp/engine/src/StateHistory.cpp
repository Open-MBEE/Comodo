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
 * $Id: StateHistory.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include "StateHistory.h"
#include "Log.h"

#include <algorithm>


using namespace scxml4cpp;


StateHistory::StateHistory(const std::string& id, const HistoryType type)
    : State(id, History)
{
    FILE_LOG(logTRACE);
    setHistoryType(type);
}

StateHistory::~StateHistory()
{
    FILE_LOG(logTRACE);
}

StateHistory::HistoryType StateHistory::getHistoryType()
{
    FILE_LOG(logTRACE);
    return mType;
}

std::list<State*>& StateHistory::getHistoryValues()
{
    FILE_LOG(logTRACE);
    return mHistoryValues;
}

void StateHistory::clearHistoryValues()
{
    FILE_LOG(logTRACE);
    FILE_LOG(logDEBUG) << "Removing all elements from state from History stack " << getId();
    mHistoryValues.clear();
}

State* StateHistory::popHistoryValue()
{
    FILE_LOG(logTRACE);

    if (mHistoryValues.size() == 0) return NULL;

    State* s = mHistoryValues.back();
    FILE_LOG(logDEBUG) << "Removing " << s->getId() << " state from History stack " << getId();
    mHistoryValues.pop_back();
    return s;
}

void StateHistory::pushHistoryValue(State* s)
{
    FILE_LOG(logTRACE);

    /*
     * Shall we check for duplicates????
     */
    std::list<State*>::iterator findIt = std::find(mHistoryValues.begin(), 
						   mHistoryValues.end(), s);
    if (findIt == mHistoryValues.end()) {
        FILE_LOG(logDEBUG) << "Adding " << s->getId() << " state to History stack " << getId();
	mHistoryValues.push_back(s);
    }
}

void StateHistory::setHistoryType(HistoryType type)
{
    FILE_LOG(logTRACE);
    mType = type;
}

void StateHistory::setHistoryValues(std::list<State*>& historyValues)
{
    FILE_LOG(logTRACE);
    mHistoryValues = historyValues;
}





