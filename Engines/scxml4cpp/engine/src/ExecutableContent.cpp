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
 * $Id: ExecutableContent.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include <ExecutableContent.h>
#include "Log.h"

using namespace scxml4cpp;

ExecutableContent::ExecutableContent()
{    
    FILE_LOG(logTRACE);
}

ExecutableContent::~ExecutableContent()
{
    FILE_LOG(logTRACE);
}

std::list<Action*>& ExecutableContent::getActions()
{    
    FILE_LOG(logTRACE);
    return mActions;
}

void ExecutableContent::addAction(Action* a)
{    
    FILE_LOG(logTRACE);
    mActions.push_back(a);
}

void ExecutableContent::execute(Context* c)
{    
    FILE_LOG(logTRACE);

    Action* a;
    std::list<Action*>::iterator it;
    for (it = mActions.begin(); it != mActions.end(); it++) {
        a = *it;
	FILE_LOG(logDEBUG) << "Executing action: " << a->getId() << " (param=" << a->getParam() << ")";
	a->execute(c);
    }
}

bool ExecutableContent::evaluate(Context *c)
{
    FILE_LOG(logTRACE);

    bool res = true;
    Action* a;
    std::list<Action*>::iterator it;
    for (it = mActions.begin(); it != mActions.end(); it++) {
        a = *it;
	FILE_LOG(logDEBUG) << "Evaluating condition: " << a->getId();
	if (a->evaluate(c) == false) {
	    res = false;
	    break;
	}
    }	
    return res;
}


