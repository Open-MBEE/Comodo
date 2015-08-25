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
 * $Id: StateComparator.h 1061 2015-07-13 15:03:59Z landolfa $
 */


#ifndef StateComparator_H
#define StateComparator_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

namespace scxml4cpp
{

    class State;

    class StateComparator
    {

      public:
	StateComparator();
	StateComparator(const StateComparator& sc);   

	virtual ~StateComparator();

	bool operator()(const State* s1, const State* s2);

      private:
	StateComparator& operator= (const StateComparator&); //! Disable assignment operator

    };

}
#endif
