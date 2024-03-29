/* 
 * This code was generated by Comodo (https://github.com/Open-MBEE/Comodo) 
 * using the QPC-C Target Platform
 *
 * - Generated Class (Comodo Module):
 *      blinkyChoice
 *
 * - Generated State Machine (Comodo Component):
 *      RelativeTimerSM
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
#include <blinkyChoice_RelativeTimerSM.h>
#include <blinkyChoice_RelativeTimerSM_impl.h>





/**
 * blinkyChoice_RelativeTimerSM Constructor
 */
blinkyChoice_RelativeTimerSM *blinkyChoice_RelativeTimerSM_constructor (blinkyChoice_RelativeTimerSM *me, const char *objNameNew, blinkyChoice_RelativeTimerSM_impl *implObj, QActive *active) {
    QActive_ctor((QActive *)me, (QStateHandler )&blinkyChoice_RelativeTimerSM_initial);
    strncpy(me->objName, objNameNew, 128);
    strncat(me->objName, ":blinkyChoice_RelativeTimerSM", 128-strlen(me->objName));
    me->impl = implObj;
    if (0 == active) {  // self IS the active object
        me->active = (QActive *)me;
    } else {  // set containing machine as active object
        me->active = active;
    }
    blinkyChoice_RelativeTimerSM_impl_set_qactive(me->impl, me->active);  // give impl access to parent QActive

    // Timer events initialized here
    QTimeEvt_ctor(&(me->initStateRelativeTimer), INITSTATE_TIMER_SIG);

    // State is initially at TOP
    me->myState = BLINKYCHOICE_RELATIVETIMERSM__TOP__;

    return me;
}

blinkyChoice_RelativeTimerSM_impl *blinkyChoice_RelativeTimerSM_get_impl (blinkyChoice_RelativeTimerSM *me) {
    return me->impl;
}

blinkyChoice_RelativeTimerSM_state blinkyChoice_RelativeTimerSM_get_current_state (blinkyChoice_RelativeTimerSM *me) {
    return me->myState;
}

/**
 * initial state definition
 */
QState blinkyChoice_RelativeTimerSM_initial (blinkyChoice_RelativeTimerSM *me, QEvt const *e) {
    // Subscribe to all the signals to which this state machine needs to respond.
	if (me->active == (QActive *)me) {
		QActive_subscribe(me->active, BLINKYCHOICE_TURN_OFF_SIG);
		QActive_subscribe(me->active, _SIG_BLINKYCHOICE_RELATIVETIMERSM_COMPLETE_);
	}

    return Q_TRAN(&blinkyChoice_RelativeTimerSM_initState);
}

/**
 * Relative_state1 state definition
 */
QState blinkyChoice_RelativeTimerSM_Relative_state1 (blinkyChoice_RelativeTimerSM *me, QEvt const *e) {
    switch (e->sig) {
        case Q_ENTRY_SIG:
            me->myState = BLINKYCHOICE_RELATIVETIMERSM_RELATIVE_STATE1;
            return Q_HANDLED();

        case Q_EXIT_SIG:
            return Q_HANDLED();

        case BLINKYCHOICE_TURN_OFF_SIG:

            me->BLINKYCHOICE_RELATIVETIMERSM_COMPLETION_EVENT_.completion_evt.super.sig = _SIG_BLINKYCHOICE_RELATIVETIMERSM_COMPLETE_;
            me->BLINKYCHOICE_RELATIVETIMERSM_COMPLETION_EVENT_.completion_evt.substate = NULL;  // Comodo functionality not implemented
            QF_publish_(&(me->BLINKYCHOICE_RELATIVETIMERSM_COMPLETION_EVENT_.base_evt));
            return Q_TRAN(&blinkyChoice_RelativeTimerSM_finalState0);


    }
    return Q_SUPER(&QHsm_top);
}

/**
 * finalState0 state definition
 */
QState blinkyChoice_RelativeTimerSM_finalState0 (blinkyChoice_RelativeTimerSM *me, QEvt const *e) {
    switch (e->sig) {
        case _SIG_BLINKYCHOICE_RELATIVETIMERSM_COMPLETE_:
            return Q_HANDLED();

        case Q_ENTRY_SIG:
            me->myState = BLINKYCHOICE_RELATIVETIMERSM_FINALSTATE0;
            return Q_HANDLED();

        case Q_EXIT_SIG:
            return Q_HANDLED();


    }
    return Q_SUPER(&QHsm_top);
}

/**
 * initState state definition
 */
QState blinkyChoice_RelativeTimerSM_initState (blinkyChoice_RelativeTimerSM *me, QEvt const *e) {
    switch (e->sig) {
        case Q_ENTRY_SIG:
            me->myState = BLINKYCHOICE_RELATIVETIMERSM_INITSTATE;
            QTimeEvt_postIn(&(me->initStateRelativeTimer), me->active, 5s);
            return Q_HANDLED();

        case Q_EXIT_SIG:
            QTimeEvt_disarm(&(me->initStateRelativeTimer));
            return Q_HANDLED();

        case INITSTATE_TIMER_SIG:
            return Q_TRAN(&blinkyChoice_RelativeTimerSM_Relative_state1);


    }
    return Q_SUPER(&QHsm_top);
}
