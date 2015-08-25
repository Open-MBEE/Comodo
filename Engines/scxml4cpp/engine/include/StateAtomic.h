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
 * $Id: StateAtomic.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef STATEATOMIC_H
#define STATEATOMIC_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif


#ifndef STATE_H
#include "State.h"
#endif

#include <string>

namespace scxml4cpp
{

    class StateAtomic : public State
    {

      public:
	StateAtomic(const std::string& id);
	virtual ~StateAtomic();

      private:
	StateAtomic(const StateAtomic&);             //! Disable copy constructor
	StateAtomic& operator= (const StateAtomic&); //! Disable assignment operator

    };

}
#endif 
