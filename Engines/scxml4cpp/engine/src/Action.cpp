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
 * $Id: Action.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include "Action.h"
#include "Context.h"
#include "Log.h"

using namespace scxml4cpp;

Action::Action(const std::string& id) :
    mId(id)
{
    FILE_LOG(logTRACE);
}

Action::~Action()
{
    FILE_LOG(logTRACE);
}

std::string Action::getId() const
{
    FILE_LOG(logTRACE);
    return mId;
}

std::string Action::getParam() const
{
    FILE_LOG(logTRACE);
    return mParam;
}

void Action::setId(const std::string& id)
{
    FILE_LOG(logTRACE);
    mId = id;
}

void Action::setParam(const std::string& p)
{
    FILE_LOG(logTRACE);
    mParam = p;
}




