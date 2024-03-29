/* 
 * This code was generated by Comodo (https://github.com/Open-MBEE/Comodo) 
 * using the QPC-C Target Platform
 *
 * - Generated Class (Comodo Module):
 *      blinkyChoice
 *
 * - Generated State Machine (Comodo Component):
 *      orthogonal
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


 

#ifndef BLINKYCHOICE_ORTHOGONAL_H_
#define BLINKYCHOICE_ORTHOGONAL_H_

#include <stdbool.h>
#include <qf_port.h>
#include <qassert.h>
#include <blinkyChoice_statechart_signals.h>
#include <blinkyChoice_orthogonal_states.h>
#include <blinkyChoice_orthogonal_impl.h>


/* Forward declarations for orthogonal-region struct field of containing SM */
typedef struct orthogonalState_region1 orthogonalState_region1;
typedef struct orthogonalState_region2 orthogonalState_region2;

/**
 * Declare the state machine struct, encapsulating the extended state variables.
 * It tracks any timers, owned orthogonal regions, history states, substates.
 */
typedef struct blinkyChoice_orthogonal {
    QActive super;  // C-style inheritance
    QActive *active;  // containing machine if this is a submachine instance
    char objName[128];
    blinkyChoice_orthogonal_impl *impl;
    enum blinkyChoice_orthogonal_state myState;

    /* Orthogonal region instance pointers */
    orthogonalState_region1 *region1;
    orthogonalState_region2 *region2;

    /* Completion event for exiting from composite/orthogonal/submachine state */
    QCompletionEvt BLINKYCHOICE_ORTHOGONAL_COMPLETION_EVENT_;

    /* Bail event to terminate orthogonal/submachine substate */
    QEvt BLINKYCHOICE_ORTHOGONAL_BAIL_EVENT_;

    /* TimeEvents object instances (only relative timers are supported) */
    QTimeEvt state2RelativeTimer;


} blinkyChoice_orthogonal;


/** 
 * blinkyChoice_orthogonal Constructor
 *
 * This State machine constructor is responsible for initializing
 * the object, allocating and initializing any orthogonal regions, 
 * and initializing the timers.
 */
blinkyChoice_orthogonal *blinkyChoice_orthogonal_constructor (blinkyChoice_orthogonal *me, const char *objNameNew, blinkyChoice_orthogonal_impl *implObj, QActive *active);

/**
  * Returns the instance of the Implementation class for this QActive.
  */
blinkyChoice_orthogonal_impl *blinkyChoice_orthogonal_get_impl (blinkyChoice_orthogonal *me);

/**
 * Returns the unique enum representing the current state of this machine.
 */
blinkyChoice_orthogonal_state blinkyChoice_orthogonal_get_current_state (blinkyChoice_orthogonal *me);

/**
 * Method to initialize state machine (equivalent to initial pseudostate)
 */
QState blinkyChoice_orthogonal_initial (blinkyChoice_orthogonal *me, QEvt const *e);

/**
 * State methods
 */
QState blinkyChoice_orthogonal_init(blinkyChoice_orthogonal *me, QEvt const *e); 
QState blinkyChoice_orthogonal_orthogonalState(blinkyChoice_orthogonal *me, QEvt const *e); 
QState blinkyChoice_orthogonal_orthogonalState_region1_state1(orthogonalState_region1 *me, QEvt const *e); 
QState blinkyChoice_orthogonal_orthogonalState_region1_finalState0(orthogonalState_region1 *me, QEvt const *e); 
QState blinkyChoice_orthogonal_orthogonalState_region2_state2(orthogonalState_region2 *me, QEvt const *e); 
QState blinkyChoice_orthogonal_orthogonalState_region2_finalState1(orthogonalState_region2 *me, QEvt const *e); 



/**
 * Declare the state machine struct, encapsulating the extended state variables.
 * It tracks any timers, owned orthogonal regions, history states, substates.
 */
struct orthogonalState_region1 {
    QHsm super;  // C-style inheritance
    QActive *active;  // containing machine if this is a submachine instance
    blinkyChoice_orthogonal *parent;  // parent active machine for access to timers
    blinkyChoice_orthogonal_impl *impl;
    enum blinkyChoice_orthogonal_state myState;
};  

/** 
 * Orthogonal-region constructor for orthogonalState_region1
 *
 * This State machine constructor is responsible for initializing
 * the object, allocating and initializing any orthogonal regions, 
 * and initializing the timers.
 */
orthogonalState_region1 *orthogonalState_region1_constructor (orthogonalState_region1 *me, blinkyChoice_orthogonal_impl *implObj, QActive *active, blinkyChoice_orthogonal *parent);

/**
 * Returns the unique enum representing current state within this region.
 */
orthogonal_state orthogonalState_region1_get_current_state (orthogonalState_region1 *me);

/**
 * Re-initializes a region, invoked upon exit from region.
 */
void orthogonalState_region1_reinit (orthogonalState_region1 *me);

/**
 * Method to initialize region to initial pseudostates
 */
QState blinkyChoice_orthogonal_orthogonalState_region1_initial (orthogonalState_region1 *me, QEvt const *e);

/**
 * Method representing state machine final state
 * This will get autogenerated even if the rergion doesn't have a final node.
 */
QState blinkyChoice_orthogonal_orthogonalState_region1_final (orthogonalState_region1 *me, QEvt const *e); 

/**
 * Declare the state machine struct, encapsulating the extended state variables.
 * It tracks any timers, owned orthogonal regions, history states, substates.
 */
struct orthogonalState_region2 {
    QHsm super;  // C-style inheritance
    QActive *active;  // containing machine if this is a submachine instance
    blinkyChoice_orthogonal *parent;  // parent active machine for access to timers
    blinkyChoice_orthogonal_impl *impl;
    enum blinkyChoice_orthogonal_state myState;
};  

/** 
 * Orthogonal-region constructor for orthogonalState_region2
 *
 * This State machine constructor is responsible for initializing
 * the object, allocating and initializing any orthogonal regions, 
 * and initializing the timers.
 */
orthogonalState_region2 *orthogonalState_region2_constructor (orthogonalState_region2 *me, blinkyChoice_orthogonal_impl *implObj, QActive *active, blinkyChoice_orthogonal *parent);

/**
 * Returns the unique enum representing current state within this region.
 */
orthogonal_state orthogonalState_region2_get_current_state (orthogonalState_region2 *me);

/**
 * Re-initializes a region, invoked upon exit from region.
 */
void orthogonalState_region2_reinit (orthogonalState_region2 *me);

/**
 * Method to initialize region to initial pseudostates
 */
QState blinkyChoice_orthogonal_orthogonalState_region2_initial (orthogonalState_region2 *me, QEvt const *e);

/**
 * Method representing state machine final state
 * This will get autogenerated even if the rergion doesn't have a final node.
 */
QState blinkyChoice_orthogonal_orthogonalState_region2_final (orthogonalState_region2 *me, QEvt const *e); 





#endif /* BLINKYCHOICE_ORTHOGONAL_H_ */
