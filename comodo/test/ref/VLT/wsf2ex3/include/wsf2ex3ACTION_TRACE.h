#ifndef wsf2ex3ACTION_TRACE_H
#define wsf2ex3ACTION_TRACE_H
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


class wsf2ex3ACTION_MGR;    
class wsf2ex3CONFIG;
class wsf2ex3DATA;
class wsf2libCONTROL;


/** 
 * TRACE action class.    
 */
class wsf2ex3ACTION_TRACE : public wsf2libACTION_TRACE
{
  public:
    wsf2ex3ACTION_TRACE(wsf2ex3ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex3CONFIG& config, 
		       wsf2ex3DATA& data);
    virtual ~wsf2ex3ACTION_TRACE();
   
       
  private:
    wsf2ex3ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex3CONFIG&     mConfig;
    wsf2ex3DATA&       mData;

    wsf2ex3ACTION_TRACE(const wsf2ex3ACTION_TRACE&);
    wsf2ex3ACTION_TRACE& operator= (const wsf2ex3ACTION_TRACE&);
};

	

#endif // !wsf2ex3ACTION_TRACE_H

