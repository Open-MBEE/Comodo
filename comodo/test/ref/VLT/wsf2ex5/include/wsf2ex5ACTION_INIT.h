#ifndef wsf2ex5ACTION_INIT_H
#define wsf2ex5ACTION_INIT_H
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

#ifndef wsf2libACTION_INIT_H
#include "wsf2libACTION_INIT.h"
#endif


class wsf2ex5ACTION_MGR;    
class wsf2ex5CONFIG;
class wsf2ex5DATA;
class wsf2libCONTROL;


/** 
 * INIT action class.    
 */
class wsf2ex5ACTION_INIT : public wsf2libACTION_INIT
{
  public:
    wsf2ex5ACTION_INIT(wsf2ex5ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex5CONFIG& config, 
		       wsf2ex5DATA& data);
    virtual ~wsf2ex5ACTION_INIT();
   
       
  private:
    wsf2ex5ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex5CONFIG&     mConfig;
    wsf2ex5DATA&       mData;

    wsf2ex5ACTION_INIT(const wsf2ex5ACTION_INIT&);
    wsf2ex5ACTION_INIT& operator= (const wsf2ex5ACTION_INIT&);
};

	

#endif // !wsf2ex5ACTION_INIT_H

