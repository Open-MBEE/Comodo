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
 * $Id: Helper.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef HELPER_H
#define HELPER_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif


#include <string>
#include <set>
#include <list>


namespace scxml4cpp
{

    class Transition;
    class StateMachine;
    class State;
    class Action;
    class Event;


    class Helper
    {
      public:
	Helper();
	~Helper();

	std::string       printStateType(State* s); 
	std::string       printTransition(Transition* t);	
	std::string       printTransitions(std::list<Transition*>& transitions);
	std::string       printTargetState(Transition* transitions);
	std::string       printTargetStates(std::list<Transition*>& transitions);
	std::string       printActions(std::list<Action*>& actions);
	std::string       printStates(State* s);
	std::string       printStateMachine(StateMachine*);

	std::string       formatStatus(std::set<State*>& status);

	int               countParents(State*);
	bool              isDescendant(State*, State*);

	std::set<State*>  getAncestors(std::list<State*>& states);
	std::list<State*> getAncestorsList(std::set<State*>& states);
	std::set<State*>  getAncestors(State*);
	std::list<State*> getAncestorsList(State*);
	std::list<State*> getProperAncestors(State*, State*);

	State*            findLeastCommonAncestor(State*, State*);
	bool              eventMatch(const Event&, const Event&);

	std::list<State*> getAtomicStates(std::set<State*>& states);
	std::list<State*> getAtomicStates(std::list<State*>& states);

	bool              isPreempted(State*, std::list<Transition*>& transitions);

      private:
	Helper(const Helper&);             //! Disable copy constructor
	Helper& operator= (const Helper&); //! Disable assignment operator
    };

}
#endif /*!_H*/



