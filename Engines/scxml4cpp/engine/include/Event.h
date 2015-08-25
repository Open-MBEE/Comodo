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
 * $Id: Event.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef EVENT_H
#define EVENT_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <string>

namespace scxml4cpp
{

    class Event
    {
      public:
	enum EventType{
	    CALL_EVENT = 0, 
	    CHANGE_EVENT,
	    SIGNAL_EVENT, 
	    TIME_EVENT, 
	    ERROR_EVENT
	};

	enum EventStatus{
	    TOBEPROCESSED = 0, 
	    PROCESSED,
	    IGNORED,
	    REJECTED, 
	    DEFERRED
	};

	Event();
	Event(const std::string& id, const EventType type);
	Event(const Event& e);
	Event& operator=(const Event& e);
	~Event();

	std::string getId() const;
	EventType   getType() const;
	EventStatus getStatus() const;
	void*       getPayload() const;

	void        setId(std::string id);
	void        setType(EventType type);
	void        setStatus(EventStatus status);
	void        setPayload(void* payload);
	
	bool        isNull() const;

      private:
	std::string mId;
	EventType   mType;
	EventStatus mStatus;
	void*       mPayload;
    };

}
#endif
