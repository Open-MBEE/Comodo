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
 * $Id: Executor.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef EXECUTOR_H
#define EXECUTOR_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif



#ifndef STATECOMPARATOR_H
#include "StateComparator.h"
#endif

#ifndef EVENTQUEUE_H
#include "EventQueue.h"
#endif

#ifndef HELPER_H
#include "Helper.h"
#endif

#ifndef STATECOMPARATOR_H
#include "StateComparator.h"
#endif

#include <string>
#include <queue>
#include <set>


namespace scxml4cpp
{

    class StateMachine;
    class State;
    class Transition;
    class Event;
    class Context;
    class EventListener;
    class StatusListener;

    class Executor
    {
      public:
	enum EventHandlingPolicy {
	    SILENT = 0,
	    REJECT,
	    DEFFERRED
	};

	Executor(StateMachine*, Context*);
	~Executor();

	std::string        formatStatus();
	std::string        formatModel();
	std::set<State*>   getStatus();
	void               printStatus();

	void               setModel(StateMachine*);
	void               setContext(Context*);

	void               setEventHandlingPolicy(const EventHandlingPolicy policy);
	EventHandlingPolicy getEventHandlingPolicy();

	void               addEventListener(EventListener* eventListener);
	void               removeEventListener(EventListener* eventListener);
	void               removeAllEventListener();

	void               addStatusListener(StatusListener* statusListener);
	void               removeStatusListener(StatusListener* statusListener);
	void               removeAllStatusListener();

	void               startSM();
	void               stopSM();
	void               start();
	void               stop();

	void               run();

	void               postEvent(const Event& e);
	void               processEvent();
	void               processEvent(const Event& e);
	bool               isEventProcessable(const Event& e);

	bool               isRunning();
	bool               isFinal();

      private:
	StateMachine*      mStateMachine;
	std::set<State*>   mCurrentStatus;
	std::set<State*>   mPreviousStatus;
	std::set<State*>   mStatesToInvoke;
	std::queue<Event>  mInternalEvents;
	EventQueue         mExternalEvents;
	bool               mContinue; // is interpreted started
	bool               mFinal;    // is top level final state reached
	StateComparator    mStateComparator;
	Context*           mContext;
	Helper             mHelper;
	EventHandlingPolicy mEventHandlingPolicy;
	std::list<EventListener*> mEventListeners;
	std::list<StatusListener*> mStatusListeners;


	StateComparator    getStateComparator();
	void               processInternalEvents();
	void               exitInterpreter();
	std::list<Transition*> selectEventlessTransitions();
	std::list<Transition*> selectTransitions(const Event& e);
	void               microstep(std::list<Transition*>& enabledTransitions);
	void               exitStates(std::list<Transition*>& enabledTransitions);
	void               executeTransitionContent(std::list<Transition*>& enabledTransitions);
	void               enterStates(std::list<Transition*>& enabledTransitions);
	void               addStatesToEnter(State* s, State* root, 
					    std::list<State*>& statesToEnter, 
					    std::list<State*>& statesForDefaultEntry);

	void               notifyEventListeners(Event& e);
	void               notifyStatusListeners(std::set<State*>& status);

	Executor(const Executor&);             //! Disable copy constructor
	Executor& operator= (const Executor&); //! Disable assignment operator
    };

}
#endif
