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
 * $Id: EventQueue.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include <EventQueue.h>
#include "Log.h"

using namespace scxml4cpp;

EventQueue::EventQueue()
{    
    FILE_LOG(logTRACE);
}

EventQueue::~EventQueue()
{    
    FILE_LOG(logTRACE);
}

void EventQueue::add(const Event& e)
{    
    FILE_LOG(logTRACE);
    FILE_LOG(logDEBUG) << "EventQueue::add " << e.getId();
    mMutex.lock();
    mEvents.push(e);
    mMutex.unlock();
}

Event EventQueue::remove()
{    
    FILE_LOG(logTRACE);
    mMutex.lock();
    Event e = mEvents.front();
    mEvents.pop();
    mMutex.unlock();
    FILE_LOG(logDEBUG) << "EventQueue::remove " << e.getId();
    return e;
}

bool EventQueue::isEmpty()
{    
    FILE_LOG(logTRACE);
    return mEvents.empty();
}

int EventQueue::size()
{    
    FILE_LOG(logTRACE);
    return mEvents.size();
}

void EventQueue::clear()
{    
    FILE_LOG(logTRACE);
    mMutex.lock();
    std::queue<Event> emptyQueue;
    mEvents = emptyQueue;
    mMutex.unlock();
}



