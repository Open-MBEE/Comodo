#ifndef wsf2ex1ACTION_STOP_H
#define wsf2ex1ACTION_STOP_H
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

#ifndef wsf2libACTION_STOP_H
#include "wsf2libACTION_STOP.h"
#endif


class wsf2ex1ACTION_MGR;    
class wsf2ex1CONFIG;
class wsf2ex1DATA;
class wsf2libCONTROL;


/** 
 * STOP action class.    
 */
class wsf2ex1ACTION_STOP : public wsf2libACTION_STOP
{
  public:
    wsf2ex1ACTION_STOP(wsf2ex1ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex1CONFIG& config, 
		       wsf2ex1DATA& data);
    virtual ~wsf2ex1ACTION_STOP();
   
       
  private:
    wsf2ex1ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex1CONFIG&     mConfig;
    wsf2ex1DATA&       mData;

    wsf2ex1ACTION_STOP(const wsf2ex1ACTION_STOP&);
    wsf2ex1ACTION_STOP& operator= (const wsf2ex1ACTION_STOP&);
};

	

#endif // !wsf2ex1ACTION_STOP_H

