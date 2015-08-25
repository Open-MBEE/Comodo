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
 * $Id: ActionTrace.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include "ActionTrace.h"
#include "scxml4cpp/Log.h"

#include <iostream>

using namespace scxml4cpp;

ActionTrace::ActionTrace(const std::string& id)
    : Action(id)
{   
    FILE_LOG(logTRACE);
}

void ActionTrace::execute(Context* c)
{   
    FILE_LOG(logTRACE);
    std::cout << "ACTION " << getId() << std::endl;
}

bool ActionTrace::evaluate(Context* c)
{   
    FILE_LOG(logTRACE);
    return true;
}




