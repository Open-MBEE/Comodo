/* 
 * This code was generated by Comodo (https://github.com/Open-MBEE/Comodo) 
 * using the QPC-C Target Platform
 *
 * - Generated Class (Comodo Module):
 *      blinkyChoice
 *
 * - Generated State Machine (Comodo Component):
 *      mode
 * 
 */


 

#ifndef BLINKYCHOICE_MODE_IMPL_H
#define BLINKYCHOICE_MODE_IMPL_H

#include <qf_port.h>
#include <qassert.h>


typedef struct blinkyChoice_mode_impl {
    char machineName[128];
    /** Cache of pointer to the container QActive object, for ease of access */
    QActive *active;

    bool hey; 
    bool heyoooo; 
    bool hyiiiqqq; 
    bool uuuueee; 



} blinkyChoice_mode_impl;

blinkyChoice_mode_impl *blinkyChoice_mode_impl_constructor (blinkyChoice_mode_impl *mepl);  // Default constructor
void blinkyChoice_mode_impl_set_qactive (blinkyChoice_mode_impl *mepl, QActive *active);

////////////////////////////////////////////
// Action and guard implementation methods
////////////////////////////////////////////
bool blinkyChoice_mode_impl_hey (blinkyChoice_mode_impl *mepl); 
bool blinkyChoice_mode_impl_heyoooo (blinkyChoice_mode_impl *mepl, int32_t arg0, int32_t arg1); 
bool blinkyChoice_mode_impl_hyiiiqqq (blinkyChoice_mode_impl *mepl); 
bool blinkyChoice_mode_impl_uuuueee (blinkyChoice_mode_impl *mepl); 
void blinkyChoice_mode_impl_hello (blinkyChoice_mode_impl *mepl); 
void blinkyChoice_mode_impl_hiiiii (blinkyChoice_mode_impl *mepl, int32_t arg0, int32_t arg1); 


#endif  /* BLINKYCHOICE_MODE_IMPL_H */
