#ifndef wsf2ex4ACTION_TRACE_H
#define wsf2ex4ACTION_TRACE_H
/*******************************************************************************
*    E.S.O. - VLT project
*
*    "@(#) $Id$"
*
* who                when       what
* ----------------  ----------  ----------------------------------------------
* COMODO            -           Created.
* 
*/

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#ifndef wsf2libACTION_TRACE_H
#include "wsf2libACTION_TRACE.h"
#endif


class wsf2ex4ACTION_MGR;    
class wsf2ex4CONFIG;
class wsf2ex4DATA;
class wsf2libCONTROL;


/** 
 * TRACE action class.    
 */
class wsf2ex4ACTION_TRACE : public wsf2libACTION_TRACE
{
  public:
    wsf2ex4ACTION_TRACE(wsf2ex4ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex4CONFIG& config, 
		       wsf2ex4DATA& data);
    virtual ~wsf2ex4ACTION_TRACE();
   
       
  private:
    wsf2ex4ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex4CONFIG&     mConfig;
    wsf2ex4DATA&       mData;

    wsf2ex4ACTION_TRACE(const wsf2ex4ACTION_TRACE&);
    wsf2ex4ACTION_TRACE& operator= (const wsf2ex4ACTION_TRACE&);
};

	

#endif // !wsf2ex4ACTION_TRACE_H

