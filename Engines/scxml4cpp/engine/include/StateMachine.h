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
 * $Id: StateMachine.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef STATEMACHINE_H
#define STATEMACHINE_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <string>
#include <list>

namespace scxml4cpp
{

    class Transition;
    class State;
    class Helper;
    class Action;

    class StateMachine
    {
      public:
	StateMachine(const std::string& id);
	virtual ~StateMachine();

	const std::string&  getId() const;
	std::list<Transition*>& getInitialTrans();
	std::list<State*>& 	getSubstates();
	std::list<State*>& 	getParallel();

	void setId(const std::string& id);
	void setInitialState(State*, Action*);
	void setFinalState(State*);
	void setSubstates(std::list<State*>& substates);
	void setParallel(std::list<State*>& parallel);

	void addSubstate(State* s);
	void addParallel(State* s);

      private:
	std::string 	mId;
	std::list<Transition*> mInitialTrans;
	std::list<State*> 	mSubstates;
	std::list<State*> 	mParallel;

	StateMachine(const StateMachine&);             //! Disable copy constructor
	StateMachine& operator= (const StateMachine&); //! Disable assignment operator

    };

}
#endif /*!_H*/
