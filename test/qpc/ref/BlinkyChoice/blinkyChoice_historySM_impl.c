/* 
 * This code was generated by Comodo (https://github.com/Open-MBEE/Comodo) 
 * using the QPC-C Target Platform
 *
 * - Generated Class (Comodo Module):
 *      blinkyChoice
 *
 * - Generated State Machine (Comodo Component):
 *      historySM
 * 
 */


 

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <log_event.h>
#include <qf_port.h>
#include <qassert.h>
#include <assert.h>
#include <blinkyChoice_historySM_impl.h>
#include <blinkyChoice_statechart_signals.h>


blinkyChoice_historySM_impl *blinkyChoice_historySM_impl_constructor (blinkyChoice_historySM_impl *mepl) {
    strncpy(mepl->machineName, "blinkyChoice_historySM", 128);
    mepl->machineName[128-1] = '\0';  // null-terminate to be sure

    // AttributeMapper_init(mepl);

    // AttributeMapper_set(mepl, "hue", 0); 

    mepl->hue = 0; 


    return mepl;
}

void blinkyChoice_historySM_impl_set_qactive (blinkyChoice_historySM_impl *mepl, QActive *active) {
    mepl->active = active;
}


////////////////////////////////////////////
// Action and guard implementation methods
////////////////////////////////////////////

bool blinkyChoice_historySM_impl_hue (blinkyChoice_historySM_impl *mepl) {
	printf("%s.hue() == %d", mepl->machineName, mepl->hue);
	return mepl->hue;
}


