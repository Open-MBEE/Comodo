#ifndef wsf2ex4ACTION_STANDBY_H
#define wsf2ex4ACTION_STANDBY_H
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

#ifndef wsf2libACTION_STANDBY_H
#include "wsf2libACTION_STANDBY.h"
#endif


class wsf2ex4ACTION_MGR;    
class wsf2ex4CONFIG;
class wsf2ex4DATA;
class wsf2libCONTROL;


/** 
 * STANDBY action class.    
 */
class wsf2ex4ACTION_STANDBY : public wsf2libACTION_STANDBY
{
  public:
    wsf2ex4ACTION_STANDBY(wsf2ex4ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex4CONFIG& config, 
		       wsf2ex4DATA& data);
    virtual ~wsf2ex4ACTION_STANDBY();
   
       
  private:
    wsf2ex4ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex4CONFIG&     mConfig;
    wsf2ex4DATA&       mData;

    wsf2ex4ACTION_STANDBY(const wsf2ex4ACTION_STANDBY&);
    wsf2ex4ACTION_STANDBY& operator= (const wsf2ex4ACTION_STANDBY&);
};

	

#endif // !wsf2ex4ACTION_STANDBY_H

