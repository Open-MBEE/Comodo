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
 * $Id: StateComparator.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include "StateComparator.h"
#include "Helper.h"
#include "State.h"
#include "Log.h"

using namespace scxml4cpp;

StateComparator::StateComparator()
{
    FILE_LOG(logTRACE);
}

StateComparator::StateComparator(const StateComparator& sc)
{
    FILE_LOG(logTRACE);
}

StateComparator::~StateComparator()
{
    FILE_LOG(logTRACE);
}

/*
 * Operator <
 * Acording std::sort specification:
 *   returns true: first argument goes before the second argument in the specific 
 *                 strict weak ordering it defines,
 *   returns false: otherwise.
 */
bool StateComparator::operator()(const State* s1, const State* s2)
{
    FILE_LOG(logTRACE);

    Helper helper;
    if (s1 != s2) {
        int p1 = helper.countParents((State*)s1);
	int p2 = helper.countParents((State*)s2);
	if (p1 < p2 ) {
	   return true;
	} else {
	   return false;
	}
    }
    return false;
}


