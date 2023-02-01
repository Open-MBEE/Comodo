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


 

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <log_event.h>
#include <qf_port.h>
#include <qassert.h>
#include <assert.h>
#include <blinkyChoice_mode_impl.h>
#include <blinkyChoice_statechart_signals.h>


blinkyChoice_mode_impl *blinkyChoice_mode_impl_constructor (blinkyChoice_mode_impl *mepl) {
    strncpy(mepl->machineName, "blinkyChoice_mode", 128);
    mepl->machineName[128-1] = '\0';  // null-terminate to be sure

    // AttributeMapper_init(mepl);

    // AttributeMapper_set(mepl, "hey", 0); 
    // AttributeMapper_set(mepl, "heyoooo", 0); 
    // AttributeMapper_set(mepl, "hyiiiqqq", 0); 
    // AttributeMapper_set(mepl, "uuuueee", 0); 

    mepl->hey = 0; 
    mepl->heyoooo = 0; 
    mepl->hyiiiqqq = 0; 
    mepl->uuuueee = 0; 


    return mepl;
}

void blinkyChoice_mode_impl_set_qactive (blinkyChoice_mode_impl *mepl, QActive *active) {
    mepl->active = active;
}


////////////////////////////////////////////
// Action and guard implementation methods
////////////////////////////////////////////

bool blinkyChoice_mode_impl_hey (blinkyChoice_mode_impl *mepl) {
	printf("%s.hey() == %d", mepl->machineName, mepl->hey);
	return mepl->hey;
}

bool blinkyChoice_mode_impl_heyoooo (blinkyChoice_mode_impl *mepl, int32_t arg0, int32_t arg1) {
	printf("%s.heyoooo() == %d", mepl->machineName, mepl->heyoooo);
	return mepl->heyoooo;
}

bool blinkyChoice_mode_impl_hyiiiqqq (blinkyChoice_mode_impl *mepl) {
	printf("%s.hyiiiqqq() == %d", mepl->machineName, mepl->hyiiiqqq);
	return mepl->hyiiiqqq;
}

bool blinkyChoice_mode_impl_uuuueee (blinkyChoice_mode_impl *mepl) {
	printf("%s.uuuueee() == %d", mepl->machineName, mepl->uuuueee);
	return mepl->uuuueee;
}

void blinkyChoice_mode_impl_hello (blinkyChoice_mode_impl *mepl) {
	printf("%s.hello() default action implementation invoked\n", mepl->machineName);
}

void blinkyChoice_mode_impl_hiiiii (blinkyChoice_mode_impl *mepl, int32_t arg0, int32_t arg1) {
	printf("%s.hiiiii() default action implementation invoked\n", mepl->machineName);
}

