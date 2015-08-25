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
 * $Id: StatusListener.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef STATUSLISTENER_H
#define STATUSLISTENER_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <set>

namespace scxml4cpp
{
    class State;

    class StatusListener
    {

      public:
	StatusListener() {}
	virtual ~StatusListener() {}

	virtual void notifyStatus(std::set<State*>& status) = 0;

      private:
	StatusListener(const StatusListener&);             //! Disable copy constructor
	StatusListener& operator= (const StatusListener&); //! Disable assignment operator
    };
}

#endif
