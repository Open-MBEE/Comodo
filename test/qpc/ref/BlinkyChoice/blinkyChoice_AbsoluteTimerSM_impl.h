/* 
 * This code was generated by Comodo (https://github.com/Open-MBEE/Comodo) 
 * using the QPC-C Target Platform
 *
 * - Generated Class (Comodo Module):
 *      blinkyChoice
 *
 * - Generated State Machine (Comodo Component):
 *      absoluteTimerSM
 * 
 */


 

#ifndef BLINKYCHOICE_ABSOLUTETIMERSM_IMPL_H
#define BLINKYCHOICE_ABSOLUTETIMERSM_IMPL_H

#include <qf_port.h>
#include <qassert.h>


typedef struct blinkyChoice_AbsoluteTimerSM_impl {
    char machineName[128];
    /** Cache of pointer to the container QActive object, for ease of access */
    QActive *active;



} blinkyChoice_AbsoluteTimerSM_impl;

blinkyChoice_AbsoluteTimerSM_impl *blinkyChoice_AbsoluteTimerSM_impl_constructor (blinkyChoice_AbsoluteTimerSM_impl *mepl);  // Default constructor
void blinkyChoice_AbsoluteTimerSM_impl_set_qactive (blinkyChoice_AbsoluteTimerSM_impl *mepl, QActive *active);

////////////////////////////////////////////
// Action and guard implementation methods
////////////////////////////////////////////

#endif  /* BLINKYCHOICE_ABSOLUTETIMERSM_IMPL_H */

