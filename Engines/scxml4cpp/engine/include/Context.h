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
 * $Id: Context.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef CONTEXT_H
#define CONTEXT_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif


#ifndef EVENT_H
#include "Event.h"
#endif

#include <iostream>
#include <string>
#include <list>

namespace scxml4cpp
{

    class Context
    {
      public:
	Context();
	Context(const Context& c);
	Context& operator=(const Context& c);
	~Context();

	std::string getName() const;
	std::string getSessionId() const;
	const Event& getLastEvent() const;

	void setName(const std::string& name);
	void setSessionId(const std::string& id);
	void setLastEvent(const Event& e);

      private:
	std::string mName;	
	std::string mSessionId;	
	Event       mLastEvent;

    };

}
#endif /*!_H*/
