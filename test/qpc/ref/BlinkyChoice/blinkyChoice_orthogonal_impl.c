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
 */


 

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <log_event.h>
#include <qf_port.h>
#include <qassert.h>
#include <assert.h>
#include <blinkyChoice_orthogonal_impl.h>
#include <blinkyChoice_statechart_signals.h>


blinkyChoice_orthogonal_impl *blinkyChoice_orthogonal_impl_constructor (blinkyChoice_orthogonal_impl *mepl) {
    strncpy(mepl->machineName, "blinkyChoice_orthogonal", 128);
    mepl->machineName[128-1] = '\0';  // null-terminate to be sure

    // AttributeMapper_init(mepl);


    return mepl;
}

void blinkyChoice_orthogonal_impl_set_qactive (blinkyChoice_orthogonal_impl *mepl, QActive *active) {
    mepl->active = active;
}


////////////////////////////////////////////
// Action and guard implementation methods
////////////////////////////////////////////

void blinkyChoice_orthogonal_impl_do_this (blinkyChoice_orthogonal_impl *mepl, int32_t arg0) {
	printf("%s.do_this() default action implementation invoked\n", mepl->machineName);
}

