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
 * $Id: Transition.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef TRANSITION_H
#define TRANSITION_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#ifndef HELPER_H
#include "Helper.h"
#endif

#ifndef EVENT_H
#include "Event.h"
#endif

#include <string>
#include <list>

namespace scxml4cpp
{

    class State;
    class ExecutableContent;
    class Action;
    class Context;
    class Helper;

    class Transition
    {
      public:
	Transition();
	virtual ~Transition();

	State* getSource();
	Event& getEvent();
	std::list<State*>& getTargets();

	void addTarget(State* s);
	void addAction(Action* a);
	void addCondition(Action* c);

	ExecutableContent* getActions();
	ExecutableContent* getConditions();

	bool isEnabled(Context* c);
	bool isEnabled(const Event& e, Context* c);

	void setSource(State* source);
	void setTargets(std::list<State*>& targets);
	void setEvent(const Event& e);

      private:
	Event              mEvent;
	ExecutableContent* mConditions;
	ExecutableContent* mActions;
	State*             mSource;
	std::list<State*>  mTargets;
	Helper             mHelper;

	Transition(const Transition&);             //! Disable copy constructor
	Transition& operator= (const Transition&); //! Disable assignment operator
    };

}
#endif

