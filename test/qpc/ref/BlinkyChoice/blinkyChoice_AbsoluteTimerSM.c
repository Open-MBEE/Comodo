/* 
 * This code was generated by Comodo (https://github.com/Open-MBEE/Comodo) 
 * using the QPC-C Target Platform
 *
 * - Generated Class (Comodo Module):
 *      blinkyChoice
 *
 * - Generated State Machine (Comodo Component):
 *      AbsoluteTimerSM
 * 
 *
 * ############### Requirements ###############
 *  Implements: 
 * - https://<traceability-service-hostname>/724776 
 * - https://<traceability-service-hostname>/724777 
 *
 * ############################################
 *
 */


 
#include <stdio.h>
#include <string.h>
#include <log_event.h>
#include <assert.h>
#include <blinkyChoice_AbsoluteTimerSM.h>
#include <blinkyChoice_AbsoluteTimerSM_impl.h>





/**
 * blinkyChoice_AbsoluteTimerSM Constructor
 */
blinkyChoice_AbsoluteTimerSM *blinkyChoice_AbsoluteTimerSM_constructor (blinkyChoice_AbsoluteTimerSM *me, const char *objNameNew, blinkyChoice_AbsoluteTimerSM_impl *implObj, QActive *active) {
    QActive_ctor((QActive *)me, (QStateHandler )&blinkyChoice_AbsoluteTimerSM_initial);
    strncpy(me->objName, objNameNew, 128);
    strncat(me->objName, ":blinkyChoice_AbsoluteTimerSM", 128-strlen(me->objName));
    me->impl = implObj;
    if (0 == active) {  // self IS the active object
        me->active = (QActive *)me;
    } else {  // set containing machine as active object
        me->active = active;
    }
    blinkyChoice_AbsoluteTimerSM_impl_set_qactive(me->impl, me->active);  // give impl access to parent QActive

    // Timer events initialized here
    QTimeEvt_ctor(&(me->initStateRelativeTimer), INITSTATE_TIMER_SIG);

    // State is initially at TOP
    me->myState = BLINKYCHOICE_ABSOLUTETIMERSM__TOP__;

    return me;
}

blinkyChoice_AbsoluteTimerSM_impl *blinkyChoice_AbsoluteTimerSM_get_impl (blinkyChoice_AbsoluteTimerSM *me) {
    return me->impl;
}

blinkyChoice_AbsoluteTimerSM_state blinkyChoice_AbsoluteTimerSM_get_current_state (blinkyChoice_AbsoluteTimerSM *me) {
    return me->myState;
}

/**
 * initial state definition
 */
QState blinkyChoice_AbsoluteTimerSM_initial (blinkyChoice_AbsoluteTimerSM *me, QEvt const *e) {
    // Subscribe to all the signals to which this state machine needs to respond.
	if (me->active == (QActive *)me) {
		QActive_subscribe(me->active, BLINKYCHOICE_TURN_OFF_SIG);
	}

    return Q_TRAN(&blinkyChoice_AbsoluteTimerSM_initState);
}

/**
 * Absolute_state2 state definition
 */
QState blinkyChoice_AbsoluteTimerSM_Absolute_state2 (blinkyChoice_AbsoluteTimerSM *me, QEvt const *e) {
    switch (e->sig) {
        case Q_ENTRY_SIG:
            me->myState = BLINKYCHOICE_ABSOLUTETIMERSM_ABSOLUTE_STATE2;
            return Q_HANDLED();

        case Q_EXIT_SIG:
            return Q_HANDLED();

        case BLINKYCHOICE_TURN_OFF_SIG:
            return Q_TRAN(&blinkyChoice_AbsoluteTimerSM_initState);


    }
    return Q_SUPER(&QHsm_top);
}

/**
 * initState state definition
 */
QState blinkyChoice_AbsoluteTimerSM_initState (blinkyChoice_AbsoluteTimerSM *me, QEvt const *e) {
    switch (e->sig) {
        case Q_ENTRY_SIG:
            me->myState = BLINKYCHOICE_ABSOLUTETIMERSM_INITSTATE;
            QTimeEvt_postIn(&(me->initStateRelativeTimer), me->active, 6s);
            return Q_HANDLED();

        case Q_EXIT_SIG:
            QTimeEvt_disarm(&(me->initStateRelativeTimer));
            return Q_HANDLED();

        case INITSTATE_TIMER_SIG:
            return Q_TRAN(&blinkyChoice_AbsoluteTimerSM_Absolute_state2);


    }
    return Q_SUPER(&QHsm_top);
}