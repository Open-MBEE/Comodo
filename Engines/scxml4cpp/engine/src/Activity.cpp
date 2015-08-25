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
 * $Id: Activity.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include "Activity.h"
#include "Log.h"

using namespace scxml4cpp;

Activity::Activity(const std::string& id) :
   mId(id),
   mRunning(false)
{
    FILE_LOG(logTRACE);
}

Activity::~Activity()
{
    FILE_LOG(logTRACE);
}


bool Activity::isRunning()
{
    FILE_LOG(logTRACE);
    return mRunning;
}

void Activity::setRunning(const bool running)
{
    FILE_LOG(logTRACE);
    mRunning = running;
}

std::string Activity::getId() const
{
    FILE_LOG(logTRACE);
    return mId;
}

void Activity::setId(const std::string& id)
{
    FILE_LOG(logTRACE);
    mId = id;
}


