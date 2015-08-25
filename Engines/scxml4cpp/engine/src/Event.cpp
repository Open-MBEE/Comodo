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
 * $Id: Event.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include "Event.h"
#include "Log.h"

using namespace scxml4cpp;

Event::Event() :
    mId(""),
    mType(Event::CHANGE_EVENT),
    mStatus(Event::TOBEPROCESSED),
    mPayload(NULL)
{
    FILE_LOG(logTRACE);
}

Event::Event(const std::string& id, EventType type) :
    mId(id),
    mType(type),
    mStatus(Event::TOBEPROCESSED),
    mPayload(NULL)
{    
    FILE_LOG(logTRACE);
}

/*
 * Copy constructor
 */
Event::Event(const Event& e) :
    mId(e.getId()),
    mType(e.getType()),
    mStatus(e.getStatus()),
    mPayload(e.getPayload())
{    
    FILE_LOG(logTRACE);
}

/*
 * Assignment operator
 */
Event& Event::operator=(const Event& e) 
{    
    FILE_LOG(logTRACE);
    mId = e.getId();
    mType = e.getType();
    mStatus = e.getStatus();
    mPayload = e.getPayload();
    return *this;
}


Event::~Event()
{    
    FILE_LOG(logTRACE);
}

std::string Event::getId() const
{    
    FILE_LOG(logTRACE);
    return mId;
}

Event::EventType Event::getType() const
{    
    FILE_LOG(logTRACE);
    return mType;
}

Event::EventStatus Event::getStatus() const
{    
    FILE_LOG(logTRACE);
    return mStatus;
}

void* Event::getPayload() const
{    
    FILE_LOG(logTRACE);
    return mPayload;
}

void Event::setId(std::string id)
{    
    FILE_LOG(logTRACE);
    mId = id;
}

void Event::setType(EventType type)
{    
    FILE_LOG(logTRACE);
    mType = type;
}

void Event::setStatus(EventStatus status)
{    
    FILE_LOG(logTRACE);
    mStatus = status;
}

void Event::setPayload(void* payload)
{    
    FILE_LOG(logTRACE);
    mPayload = payload;
}

bool Event::isNull() const
{
    FILE_LOG(logTRACE);
    return mId.empty();
}

