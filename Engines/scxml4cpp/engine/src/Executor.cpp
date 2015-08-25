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
 * $Id: Executor.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */

#include "Executor.h"
#include "StateMachine.h"
#include "State.h"
#include "StateHistory.h"
#include "Transition.h"
#include "Event.h"
#include "Context.h"
#include "ExecutableContent.h"
#include "EventListener.h"
#include "StatusListener.h"
#include "Log.h"

#include <algorithm>

using namespace scxml4cpp;


/**
 * State machine executor. 
 * 
 * This class is based on the algorithm presented in Appendix B of the 
 * "State Chart (SCXML): State Machine Notation for Control Abstraction", 
 * W3C working draft, 2009-10-29.
 */

Executor::Executor(StateMachine* stateMachine, Context* context) :
    mStateMachine(stateMachine),
    mContinue(false),
    mFinal(false),
    mContext(context),
    mEventHandlingPolicy(EventHandlingPolicy::SILENT)
{
    FILE_LOG(logTRACE);
    // TBD check pointers!!!
}

Executor::~Executor() 
{
    FILE_LOG(logTRACE);
}

void Executor::setModel(StateMachine* stateMachine)
{
    FILE_LOG(logTRACE);
    mStateMachine = stateMachine;
}

void Executor::setContext(Context* context)
{
    FILE_LOG(logTRACE);
    mContext = context;
}


void Executor::setEventHandlingPolicy(const EventHandlingPolicy policy) 
{
    FILE_LOG(logTRACE);
    mEventHandlingPolicy = policy;
}

Executor::EventHandlingPolicy Executor::getEventHandlingPolicy() 
{
    FILE_LOG(logTRACE);
    return mEventHandlingPolicy;
}

void Executor::addEventListener(EventListener* eventListener) 
{
    FILE_LOG(logTRACE);
    if (eventListener != NULL) {
        mEventListeners.push_back(eventListener);
    }
}

void Executor::removeEventListener(EventListener* eventListener) 
{
    FILE_LOG(logTRACE);
    if (eventListener != NULL) {
        mEventListeners.remove(eventListener);
    }
}

void Executor::removeAllEventListener() 
{
    FILE_LOG(logTRACE);
    mEventListeners.clear();
}

void Executor::addStatusListener(StatusListener* statusListener) 
{
    FILE_LOG(logTRACE);
    if (statusListener != NULL) {
        mStatusListeners.push_back(statusListener);
    }
}

void Executor::removeStatusListener(StatusListener* statusListener) 
{
    FILE_LOG(logTRACE);
    if (statusListener != NULL) {
        mStatusListeners.remove(statusListener);
    }
}

void Executor::removeAllStatusListener() 
{
    FILE_LOG(logTRACE);
    mStatusListeners.clear();
}

StateComparator Executor::getStateComparator() 
{
    FILE_LOG(logTRACE);
    return mStateComparator;
}

std::string Executor::formatStatus() 
{
    FILE_LOG(logTRACE);
    return mHelper.formatStatus(mCurrentStatus);
}

std::string Executor::formatModel() 
{
    FILE_LOG(logTRACE);
    return mHelper.printStateMachine(mStateMachine);
}

std::set<State*> Executor::getStatus() 
{
    FILE_LOG(logTRACE);
    return mCurrentStatus;
}

void Executor::printStatus() 
{
    FILE_LOG(logTRACE);
    std::cout << "EXEC Status: " <<  formatStatus() << std:: endl;
}

void Executor::startSM() 
{
    FILE_LOG(logTRACE);

    /*
     * first perform in-place expansions of states by including
     * SCXML source referenced by URLs (see 3.13 Referencing External Files)
     * and change initial attributes to initial container children with empty
     * transitions to the state from the attribute.
     */
    // @TODO
    // expandScxmlSource(doc)
    
    /*
     * Then (optionally) validate the resulting SCXML, and throw an
     * exception if validation fails.
     */
    // @TODO
    // if (!valid(doc)) {fail with error}


    /*
     * moved to the constructor to make possible querying the state even
     * before starting the SM execution
     */

    mContinue = true;
    mFinal = false;
    //std::cout << "startSM: mContinue = " << mContinue << "\n";

    /*
     * Call executeTransitionContent on the initial transition that is a
     * child of scxml. Then call enterState on the initial transition.
     *	
     * Finally, start the interpreter's event loop.
     */

    //Transition initalTrans = mStateMachine.getInitialTrans();
    //List<Transition> transList = new LinkedList<Transition>();
    //transList.add(initalTrans);
	
    std::list<Transition*> transList = mStateMachine->getInitialTrans();
    if (transList.size() == 0) {
        // @TODO: throw exception no INITIAL state defined!
        return;
    }

    //cout << endl << "EXEC Initial Transition:" << endl;
    //mHelper.printTransitions(transList);

    executeTransitionContent(transList);
    enterStates(transList);

    /*
     * Upon entering the state machine, we take all internally enabled transitions,
     * namely those that don't require an event and those that are triggered by
     * internal events. (Internal events can only be generated by the state machine itself.)
     * When all such transitions have been taken, we move to the main event loop,
     * which is driven by external events.
     */

    bool initialStepComplete = false;
    while (initialStepComplete == false) {
        //std::list<Transition*> enabledTransitions = selectTransitions(NULL);
        std::list<Transition*> enabledTransitions = selectEventlessTransitions();

	if (enabledTransitions.size() == 0) {
	    if (mInternalEvents.size() != 0) {
	        Event internalEvent = mInternalEvents.front(); 
		mInternalEvents.pop();
		//datamodel.assignValue("event", internalEvent)
		enabledTransitions = selectTransitions(internalEvent);
	    }
	}
	if (enabledTransitions.size() != 0) {
	    microstep(enabledTransitions);
	} else {
	    initialStepComplete = true;
	}
    }

    //printStatus();
    notifyStatusListeners(mCurrentStatus);
}

void Executor::run() 
{
    FILE_LOG(logTRACE);

    //start();
    while (mContinue == true) {
        processEvent();
    }
    stopSM();
}

void Executor::start()
{
    FILE_LOG(logTRACE);
    FILE_LOG(logDEBUG) << "Started execution SCXML model.";
    startSM();
    //mThread = thread(&Executor::run, this);
}	

void Executor::stop() 
{
    FILE_LOG(logTRACE);

    stopSM();
    //mThread.join();
    mCurrentStatus.clear();
    mPreviousStatus.clear();

    std::queue<Event> emptyQueue;
    mInternalEvents = emptyQueue;
    
    mExternalEvents.clear();

    FILE_LOG(logDEBUG) << "Stopped execution SCXML model.";
}

void Executor::stopSM() 
{
    FILE_LOG(logTRACE);

    //printStatus();
    mContinue = false;
    exitInterpreter();
}


bool Executor::isRunning() 
{
    FILE_LOG(logTRACE);
    return mContinue;
}

bool Executor::isFinal() 
{
    FILE_LOG(logTRACE);
    return mFinal;
}

/*
 * Check wheter an event enables transitions or not.
 * This method can be used to implement event processing
 * policy such as: 
 * - SILENT simply ignore the event
 * - REJECT return an error message
 * - DEFERRED leave the event in the queue until it can be process
 */
bool Executor::isEventProcessable(const Event& e)
{
    FILE_LOG(logTRACE);
    std::list<Transition*> enabledTransitions = selectTransitions(e);
    return enabledTransitions.size() != 0;
}

void Executor::postEvent(const Event& e) 
{
    FILE_LOG(logTRACE);
    // mExternalEvents is of type EventQueue which is already protected by mutex
    mExternalEvents.add(e);
}

void Executor::processEvent(const Event& e) 
{
    FILE_LOG(logTRACE);
    mExternalEvents.add(e);
    processEvent();
}

void Executor::processEvent() 
{
    FILE_LOG(logTRACE);

    /*
     * This loop runs until we enter a top-level final state or an external 
     * entity cancels processing. 
     * In either case 'continue' will be set to false (see EnterStates, below, 
     * for termination by entering 
     * a top-level final state.)
     * 
     * Each iteration through the loop consists of three main steps: 
     * 1) execute any <invoke> tags for atomic states that we entered on the 
     *    last iteration through the loop 
     * 2) Wait for an external event and then execute any transitions that it triggers 
     * 3) Take any subsequent internally enabled transitions, namely those that 
     *    don't require an event or that 
     *    are triggered by an internal event.
     * 
     * This event loop thus enforces run-to-completion semantics, in which the 
     * system process an external event and then takes all the 'follow-up' 
     * transitions that the processing has enabled before looking for another 
     * external event. 
     * For example, suppose that the external event queue contains events e1 and 
     * e2 and the machine is in state s1. If processing e1 takes the machine to 
     * s2 and generates internal event e3, and s2 contains a transition t triggered 
     * by e3, the system is guaranteed to take t, no matter what transitions 
     * s2 or other states have that would be triggered by e2. Note that this is 
     * true even though e2 was already in the external event queue when e3 was 
     * generated. In effect, the algorithm treats the processing of e3 as 
     * finishing up the processing of e1.
     */
	
    // replaced while loop with processEvent method which has to be called
    // by the client every time there is an external event. This allows the
    // client to implement the event loop
	
    if (mContinue == false) {
        // @TODO throw exception: SM not started
        FILE_LOG(logTRACE) << "execution has been stopped (mContinue = false)";
        return;
    }
	
    while (mExternalEvents.size() != 0 || mInternalEvents.size() != 0) {
        // this call should blocks until an event is available
        if (mExternalEvents.size() != 0) {
	    Event externalEvent = mExternalEvents.remove();		
	    mContext->setLastEvent(externalEvent);
			
	    // @TODO
	    // datamodel.assignValue("event",externalEvent)
	    std::list<Transition*> enabledTransitions = selectTransitions(externalEvent);
	    if (enabledTransitions.size() != 0) {
	        microstep(enabledTransitions);
		// now take any newly enabled null transitions 
		// and any transitions triggered by internal events
		processInternalEvents();

		externalEvent.setStatus(Event::PROCESSED);
	    } else {
	        if (getEventHandlingPolicy() == EventHandlingPolicy::REJECT) {
	            externalEvent.setStatus(Event::REJECTED);
		} else {
	            externalEvent.setStatus(Event::IGNORED);
		}
	    }
	    notifyEventListeners(externalEvent);
	}
	if (mInternalEvents.size() != 0) {
	    processInternalEvents();
	}

	/*
	 * Note that invokation may rise internal events.
	 */
	std::set<State*>::iterator it;
	for (it = mStatesToInvoke.begin(); it != mStatesToInvoke.end(); it++) {
	    State* s = *it;
	    if (s->isAtomic() == true) {
	        s->startActivities();
	    }
	}
	mStatesToInvoke.clear();
		
	mPreviousStatus = mCurrentStatus;

	notifyStatusListeners(mCurrentStatus);
    }
}

void Executor::processInternalEvents() 
{
    FILE_LOG(logTRACE);
		
    bool macroStepComplete = false;
    while (macroStepComplete == false) {
	std::list<Transition*> enabledTransitions = selectEventlessTransitions();
	if (enabledTransitions.size() == 0) { 
	    if (mInternalEvents.size() != 0) {
	        Event internalEvent = mInternalEvents.front(); 
		mInternalEvents.pop();
		mContext->setLastEvent(internalEvent);
				
		// @TODO
		// datamodel.assignValue("event", internalEvent)

		enabledTransitions = selectTransitions(internalEvent);		
	    } else {
	        macroStepComplete = true;
	    }
	}
	if (enabledTransitions.size() != 0) {
	    microstep(enabledTransitions);
	}
    }
}

void Executor::exitInterpreter() 
{
    FILE_LOG(logTRACE);

    /*
     * The purpose of this procedure is to exit the current SCXML process 
     * by exiting all active states. 
     * If the machine is in a top-level final state, a Done event is generated.
     */

    bool inFinalState = false;

    std::list<State*> statesToExit = mHelper.getAncestorsList(mCurrentStatus);

    // sort statesToExit in exitOrder
    statesToExit.sort(getStateComparator());

    std::list<State*>::iterator it;

    for (it = statesToExit.end(); it != statesToExit.begin();) {
        --it;
	State* s = *it;
	s->getExitActions()->execute(mContext);
	// @TODO
	// cancel invoke
	s->cancelActivities();
			
	if (s->isFinal() == true && s->getParent() == NULL) 
	    inFinalState = true;
		
	mCurrentStatus.erase(s);

	if (inFinalState) {
	    // @TODO
	    // sendDoneEvent(???);
	}
    }   
}

	
std::list<Transition*> Executor::selectEventlessTransitions() 
{
    FILE_LOG(logTRACE);

    /*
     * This function selects all transitions that are enabled in the current 
     * configuration that do not require an event trigger. 
     * First test if the state has been preempted by a transition that has 
     * already been selected and that will cause the state to be exited when 
     * the transition is taken. If the state has not been preempted, find a 
     * transition with no 'event' attribute whose condition evaluates to true. 
     * If multiple matching transitions are present, take the first in document 
     * order. If none are present, search in the state's ancestors in ancestory 
     * order until one is found. As soon as such a transition is found, add it 
     * to enabledTransitions, and proceed to the next atomic state in the 
     * configuration. If no such transition is found in the state or its ancestors, 
     * proceed to the next state in the configuration. When all atomic states have 
     * been visited and transitions selected, return the set of enabled transitions.
     */
    std::list<Transition*> enabledTransitions;
    std::list<State*> atomicStates = mHelper.getAtomicStates(mCurrentStatus);

    std::list<State*>::iterator it;
    for (it = atomicStates.begin(); it != atomicStates.end(); it++) {
        State* s = *it;
	if (mHelper.isPreempted(s, enabledTransitions) == false) {
	    std::list<State*> ancestors = mHelper.getAncestorsList(s);
	    // order ancestors in exitOrder
	    ancestors.sort(getStateComparator());
	    std::list<State*>::iterator it1;
	    for (it1 = ancestors.end(); it1 != ancestors.begin();) {
	        --it1;
		State* a = *it1;
		std::list<Transition*> transList = a->getTransitions();

		std::list<Transition*>::iterator it2;
		for (it2 = transList.begin(); it2 != transList.end(); it2++) {
		    Transition* t = *it2;
		    if (t->isEnabled(mContext) == true) {
		        enabledTransitions.push_back(t);
			break;
		    }
		}
	    }
	}
    }
    return enabledTransitions;
}


std::list<Transition*> Executor::selectTransitions(const Event& e) 
{
    FILE_LOG(logTRACE);

    /*
     * The purpose of the selectTransitions() procedure is to collect
     * the transitions that are enabled by this event in the current
     * configuration.
     * Create an empty set of enabledTransitions.
     * For each atomic state in the configuration, first check
     * if the event is the result of an <invoke> in this state.
     * If so, apply any <finalize> code in the state.
     * Next test if the state has been preempted by a transition that
     * has already been selected and that will cause the state to be
     * exited when the transition is taken. If the state has not been
     * preempted, find a transition whose 'event' attribute matches event
     * and whose condition evaluates to true. If multiple matching transitions
     * are present, take the first in document order. If none are present,
     * search in the state's ancestors in ancestory order until one is found.
     * As soon as such a transition is found, add it to enabledTransitions,
     * and proceed to the next atomic state in the configuration.
     * If no such transition is found in the state or its ancestors,
     * proceed to the next state in the configuration.
     * When all atomic states have been visited and transitions selected,
     * return the set of enabled transitions.
     */
    std::list<Transition*> enabledTransitions;
    std::list<State*> atomicStates = mHelper.getAtomicStates(mCurrentStatus);

    std::list<State*>::iterator it;
    for (it = atomicStates.begin(); it != atomicStates.end(); it++) {
        State* s = *it;
	// @TODO
	// event is the result of an <invoke> in this state
	// if (event.attribute('invokeid') != null && state.invokeid = event.invokeid) {
	//    applyFinalize(state, event);
	//    }
	if (mHelper.isPreempted(s, enabledTransitions) == false) {
	    std::list<State*> ancestors = mHelper.getAncestorsList(s);
	    // order ancestors in exitOrder
	    ancestors.sort(getStateComparator());
	    std::list<State*>::iterator it1;

	    bool foundTrans = false;
	    for (it1 = ancestors.end(); it1 != ancestors.begin();) {
	       --it1;
	       State* a = *it1;
	       std::list<Transition*> transList = a->getTransitions();	
	       std::list<Transition*>::iterator it2;
	       for (it2 = transList.begin(); it2 != transList.end(); it2++) {
	           Transition* t = *it2;
		   if (t->isEnabled(e, mContext) == true) {
                       /*
			* Care has to be taken not to add the SAME transition more than once.
			* Consider the case of 2 active substates (orthogonal regions) and a
			* common superstate with internal transition or self-transition: 
			* the transition should NOT be executed twice (one per region)! 
			*/
		        std::list<Transition*>::iterator pos = std::find(enabledTransitions.begin(),
									 enabledTransitions.end(), 
									 t);

			if (pos == enabledTransitions.end()) {
			    enabledTransitions.push_back(t);
			    foundTrans = true;
			    break;
			}
		   }
	       }
	       /*
		* one transition han been found, proceed with the next 
		* atomic state in the configuration.
		*/
	       if (foundTrans == true) break;
	    }
	}
    }
    return enabledTransitions;
}

void Executor::microstep(std::list<Transition*>& enabledTransitions) 
{
    FILE_LOG(logTRACE);

    /*
     * The purpose of the microStep procedure is to process the set of transitions
     * enabled by an external event, an internal event, or by the presence or absence
     * of certain values in the dataModel at the current point in time.
     * The processing of the enabled transitions must be done in parallel ('lock step')
     * in the sense that their source states must first be exited, then their actions must
     * be executed, and finally their target states entered.
     */

    /*
      procedure microstep(enabledTransitions):
	   exitStates(enabledTransitions)
	   executeTransitionContent(enabledTransitions)
	   enterStates(enabledTransitions)
    */
    //mHelper.printTransitions(enabledTransitions);
    exitStates(enabledTransitions);
    executeTransitionContent(enabledTransitions);
    enterStates(enabledTransitions);
}

void Executor::exitStates(std::list<Transition*>& enabledTransitions) 
{
    FILE_LOG(logTRACE);

    /*
     * Create an empty statesToExit set.
     *
     * For each transition t in enabledTransitions, if t is targetLess then do nothing,
     * else let LCA be the least common ancestor state of the source state and target states of t.
     * Add to the statesToExit set all states in the configuration that are descendants of LCA.
     * Convert the statesToExit set to a list and sort it in exitOrder.
     *
     * For each state s in the list, if s has a deep history state h, set the history value
     * of h to be the list of all atomic descendants of s that are members in the current
     * configuration, else set its value to be the list of all immediate children of s that
     * are members of the current configuration.
     *
     * Again for each state s in the list, first execute any onExit handlers, then cancel any
     * ongoing invocations, and finally remove s from the current configuration.
     */

    /*
      procedure exitStates(enabledTransitions):
            statesToExit = new Set()
            for t in enabledTransitions:
               if (t.attribute('target') != null):
                  LCA = findLCA([t.parent()].append(getTargetStates(t)))
                  for s in configuration.toList():
                     if (isDescendant(s,LCA)):
                        statesToExit.add(s)
            statesToExit = statesToExit.toList().sort(exitOrder)
            for s in statesToExit:
               for h in s.history:
                  f = (h.attribute('type') == "deep") ?
                      lambda(s0): isAtomicState(s0) && isDescendant(s0,s) :
                      lambda(s0): s0.parent() == s
                  historyValue[h.attribute('id')] = configuration.toList().filter(f)
            for s in statesToExit:
               for content in s.onexit:
                  executeContent(content)
               for inv in s.invoke:
                  cancelInvoke(inv)
               configuration.delete(s)
    */
    std::list<State*> statesToExit;
    std::list<Transition*>::iterator it;
    for (it = enabledTransitions.begin(); it != enabledTransitions.end(); it++) {
        Transition* t = *it;
	std::list<State*> targets = t->getTargets();
	if (targets.size() != 0) {
	    State* LCA = mHelper.findLeastCommonAncestor(t->getSource(), targets.front());
	    std::set<State*>::iterator it1;
	    for (it1 = mCurrentStatus.begin(); it1 != mCurrentStatus.end(); it1++) {
	        State* s = *it1;
		if (mHelper.isDescendant(s, LCA) == true) {
		    statesToExit.push_back(s);
		}
	    }
	    // @ADDED_START
	    // self transition specified with target state -> change of state required
	    if (t->getSource() == targets.front() && t->getSource() != NULL) {
	        statesToExit.push_back(t->getSource());
	    }
	    // @ADDED_END
	}
    }

    std::list<State*>::iterator it1;

    /*
     * Remove from the list of activities to start the ones ... ???
     */
    for (it1 = statesToExit.begin(); it1 != statesToExit.end(); it1++) {
        State* s = *it1;
	mStatesToInvoke.erase(s);
    }

    statesToExit.sort(this->getStateComparator());
    statesToExit.reverse();

    /*
     * Update history stack (if any).
     */
    for (it1 = statesToExit.begin(); it1 != statesToExit.end(); it1++) {
        State* s = *it1;
	StateHistory* h = s->getHistory();
	if (h != NULL) {
	    if (h->getHistoryType() == StateHistory::Deep) {
	        std::set<State*>::iterator it2;
		for (it2 = mCurrentStatus.begin(); it2 != mCurrentStatus.end(); it2++) {
		    State* s0 = *it2;
		    if (s0->isAtomic() && mHelper.isDescendant(s0, s)) {
		        //h->getHistoryValues().push_back(s0);
		        h->pushHistoryValue(s0);
		    }
		}
	    } else {
	        std::set<State*>::iterator it2;
		for (it2 = mCurrentStatus.begin(); it2 != mCurrentStatus.end(); it2++) {
		    State* s0 = *it2;
		    if (s0->getParent() == s) {
		        //h->getHistoryValues().push_back(s0);
		        h->pushHistoryValue(s0);
		    }
		}
	    }
	}
    }

    std::list<State*>::iterator it3;
    for (it3 = statesToExit.begin(); it3 != statesToExit.end(); it3++) {
        State* s = *it3;

        FILE_LOG(logDEBUG) << "exit: " << s->getId() << " (" << mHelper.printStateType(s) << ")";

	s->getExitActions()->execute(mContext);

	s->cancelActivities();

	mCurrentStatus.erase(s);
    }
}


void Executor::executeTransitionContent(std::list<Transition*>& enabledTransitions) 
{
    FILE_LOG(logTRACE);

    /*
     * For each transition in the list of enabledTransitions,
     * execute its executable content.
     */
    std::list<Transition*>::iterator it;
    for (it = enabledTransitions.begin(); it != enabledTransitions.end(); it++) {
        Transition* t = *it;
	t->getActions()->execute(mContext);
    }
}

void Executor::enterStates(std::list<Transition*>& enabledTransitions) 
{
    FILE_LOG(logTRACE);

    std::list<State*> statesToEnter;
    std::list<State*> statesForDefaultEntry;

    /*
     * Build a list of the states to enter.
     */
    std::list<Transition*>::iterator it;
    for (it = enabledTransitions.begin(); it != enabledTransitions.end(); it++) {
        Transition* t = *it;
	std::list<State*> targets = t->getTargets();
	if (targets.size() != 0) {
	    // @TODO should be LCA between source and all targets !!! TOBEFIXED
	    // LCA = findLCA([t.parent()].append(getTargetStates(t)))
	    State* LCA = mHelper.findLeastCommonAncestor(t->getSource(), targets.front());
	    State* s = targets.front();
	    // for s in getTargetStates(t):
	    //     addStatesToEnter(s,LCA,statesToEnter,statesForDefaultEntry)
	    addStatesToEnter(s, LCA, statesToEnter, statesForDefaultEntry);
	}
    }

    /*
     * Reset history state after entering states containing history. 
     */
    std::list<State*>::iterator it1;
    for (it1 = statesToEnter.begin(); it1 != statesToEnter.end(); it1++) {
        State* s = *it1;
	StateHistory* h = s->getHistory();
	if (h != NULL) {
	    h->clearHistoryValues();
	}
    }

    /*
     * Update current status and execute entry and initial transition actions.
     */
    statesToEnter.sort(this->getStateComparator());
    //std::list<State*>::iterator it1;
    for (it1 = statesToEnter.begin(); it1 != statesToEnter.end(); it1++) {
        State* s = *it1;
	mCurrentStatus.insert(s);
	mStatesToInvoke.insert(s);
	s->getEntryActions()->execute(mContext);

	if (count(statesForDefaultEntry.begin(), statesForDefaultEntry.end(), s ) > 0) {
	    // @TODO
	    //executeContent(s.initial.transition.children())
	    //
	    std::list<Transition*> initTrans = s->getInitialTrans();
	    for (it = initTrans.begin(); it != initTrans.end(); it++) {
	        Transition* t = *it;
		t->getActions()->execute(mContext);
	    }
	}
	/*
	 * if s is a final state, generate relevant Done events
	 */
	if (s->isFinal() == true) {
	    /*
	      State parent = s.getParent();
	      State grandparent = parent.getParent();
	      // @TODO
	      Event doneEvent = new Event(parent.getId()+".Done", Event.EventType.CHANGE_EVENT);
	      mInternalEvents.add(doneEvent);
	      if (grandparent.isParallel() == true) {
	      List<State> substates = grandparent.getSubstates();
		        	for (Iterator<State> it1 = substates.iterator(); it1.hasNext();) {
                			State s1 = it1.next();
		        		if (isInFinalState(s) == true) {
		                        	Event doneEvent1 = new Event(s1.getParent().getId()+".Done", Event.EventType.CHANGE_EVENT);
						mInternalEvents.add(doneEvent1);
                			}
			        }
				}
	    */
	}
    }
    /*
     * If we have reached a top-level final state, exit the interpreter.
     */
    std::set<State*>::iterator it2;
    for (it2 = mCurrentStatus.begin(); it2 != mCurrentStatus.end(); it2++) {
        State* s = *it2;
	if (s->isFinal() == true && s->getParent() == NULL /* parent = StateMachine SCXML */) {
	    //mContinue = false;
	    mFinal = true;
	    //std::cout << "enterStates: reached top-level final state -> mFinal = true\n";
	}
    }


}

void Executor::addStatesToEnter(State* s, State* root, std::list<State*>& statesToEnter, 
				std::list<State*>& statesForDefaultEntry) 
{
    FILE_LOG(logTRACE);
    FILE_LOG(logDEBUG) << "enter: " << s->getId() << " (" << mHelper.printStateType(s) << ")";

    if (s->isHistory()) {
        /*
	 * Transition to a history state.
	 */
        StateHistory* h = (StateHistory*) s;
	std::list<State*> values = h->getHistoryValues();
	if (values.size() != 0) {
	    /*
	     * Add the states in the history stack to the list of states
	     * to enter.
	     */
	    std::list<State*>::iterator it;
	    for (it = values.begin(); it != values.end(); it++) {
	        State* s0 = *it;
		addStatesToEnter(s0, root, statesToEnter, statesForDefaultEntry);
	    }
	} else {
	    /*
	     * If history stack is empty, take the (mandatory) transition from
	     * the history state to a default initial state.
	     */
	    State* n = (State*) h;
	    std::list<Transition*> transitions = n->getTransitions();
	    std::list<Transition*>::iterator it1;
	    for (it1 = transitions.begin(); it1 != transitions.end(); it1++) {
	        Transition* t = *it1;
		std::list<State*> targets = t->getTargets();
		std::list<State*>::iterator it;
		for (it = targets.begin(); it != targets.end(); it++) {
		    State* s0 = *it;
		    this->addStatesToEnter(s0, root, statesToEnter, statesForDefaultEntry); 
		}
	    }
	}		
    } else {
        statesToEnter.push_back(s);
	if (s->isParallel()) {
	    std::list<State*> subStates = s->getSubstates();
	    std::list<State*>::iterator it;
	    for (it = subStates.begin(); it != subStates.end(); it++) {
	        State* c = *it;
		this->addStatesToEnter(c, s, statesToEnter, statesForDefaultEntry);
	    }
	} else if (s->isCompound()) {
	    statesForDefaultEntry.push_back(s);
	    addStatesToEnter(s->getInitialState(), s, statesToEnter, statesForDefaultEntry);
	}
	std::list<State*> ancestors = mHelper.getProperAncestors(s, root);
	std::list<State*>::iterator it1;
	for (it1 = ancestors.begin(); it1 != ancestors.end(); it1++) {
	    State* anc = *it1;
	    if (count(statesToEnter.begin(), statesToEnter.end(), anc) == 0) {
	        statesToEnter.push_back(anc);
		if (anc->isParallel()) {
		    std::list<State*> substates = anc->getSubstates();
		    std::list<State*>::iterator it2;
		    for (it2 = substates.begin(); it2 != substates.end(); it2++) {
		        State* pChild = *it2;
			if (mHelper.isDescendant(pChild, anc) == false) {
			    this->addStatesToEnter(pChild, anc, statesToEnter, 
						   statesForDefaultEntry);
			}
		    }
		}
	    }
	}
    }
}

void Executor::notifyEventListeners(Event& e)
{
    FILE_LOG(logTRACE);

    std::list<EventListener*>::iterator it;
    for (it = mEventListeners.begin(); it != mEventListeners.end(); it++) {
        (*it)->notifyEvent(e);
    }
}

void Executor::notifyStatusListeners(std::set<State*>& status)
{
    FILE_LOG(logTRACE);

    std::list<StatusListener*>::iterator it;
    for (it = mStatusListeners.begin(); it != mStatusListeners.end(); it++) {
        (*it)->notifyStatus(status);
    }
}

