/*
 *    scampl4cpp/parser
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
 * $Id: ActionTrace.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef ACTIONTRACE_H
#define ACTIONTRACE_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#ifndef ACTION_H
#include "scxml4cpp/Action.h"
#endif

#include <string>

namespace scxml4cpp
{

    class Context;


    class ActionTrace : public Action
    {
      public:
	ActionTrace(const std::string& id);
	void execute(Context*);
	bool evaluate(Context*);

      private:
	ActionTrace(const ActionTrace&);             //! Disable copy constructor
	ActionTrace& operator= (const ActionTrace&); //! Disable assignment operator
    };

}
#endif 
