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
 * $Id: State.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef STATE_H
#define STATE_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <string>
#include <list>

namespace scxml4cpp
{

    class State;
    class Action;
    class Activity;
    class ExecutableContent;
    class StateHistory;
    class Transition;
    class Event;

    class State
    {
      public:
	enum StateType {
	    Atomic = 0,
	    Compound,
	    Parallel,
	    History
	};

	State(const std::string& id, const StateType type);
	virtual ~State();

	std::string		getId() const;
	StateType 		getType() const;
	State* 		        getParent();
	StateHistory*	        getHistory();
	ExecutableContent* 	getEntryActions();
	ExecutableContent* 	getExitActions();
	std::list<Transition*>& getInitialTrans();
	State* 		        getInitialState();
	std::list<State*>& 	getSubstates();
	std::list<Transition*>& getTransitions();
	std::list<Activity*>& 	getActivities();

	void setId(const std::string& id);
	void setParent(State* parent);
	void setIsInitial(const bool isInitial);
	void setIsFinal(const bool isFinal);
	void setHistory(StateHistory* h);
	void setInitialState(State* initialState, Action* a);
	void setFinalState(State* finalState);
	void setSubstates(std::list<State*>& substates);
	void setTransitions(std::list<Transition*>& transitions);
	void setActivities(std::list<Activity*>& activities);

	bool isInitial();
	bool isFinal();
	bool isCompound();
	bool isParallel();
	bool isAtomic();
	bool isHistory();

	void startActivities();
	void cancelActivities();
    
	void addEntryAction(Action*);
	void addExitAction(Action*);
	void addSubstate(State*);
	void addTransition(std::list<State*>& targets, const Event& event, 
			   Action* condition, Action* action);
	void addTransition(State* target, const Event& event, Action* condition, Action* action);
	void addActivity(Activity* activity);

      private:
	std::string		mId;
	StateType 		mType;
	State* 		        mParent;
	std::list<State*> 	mSubstates;
	StateHistory*	        mHistory;
	std::list<Transition*> 	mTransitions;
	ExecutableContent* 	mOnEntry;
	ExecutableContent* 	mOnExit;
	std::list<Activity*> 	mActivities;
	std::list<Transition*> 	mInitialTrans;
	bool		        mIsInitial;
	bool		        mIsFinal;

	State(const State&);             //! Disable copy constructor
	State& operator= (const State&); //! Disable assignment operator
    };

}
#endif
