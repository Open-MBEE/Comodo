#ifndef wsf2ex6ACTION_INIT_H
#define wsf2ex6ACTION_INIT_H
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


class wsf2ex6ACTION_MGR;    
class wsf2ex6CONFIG;
class wsf2ex6DATA;
class wsf2libCONTROL;


/** 
 * INIT action class.    
 */
class wsf2ex6ACTION_INIT : public wsf2libACTION_INIT
{
  public:
    wsf2ex6ACTION_INIT(wsf2ex6ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex6CONFIG& config, 
		       wsf2ex6DATA& data);
    virtual ~wsf2ex6ACTION_INIT();
   
       
  private:
    wsf2ex6ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex6CONFIG&     mConfig;
    wsf2ex6DATA&       mData;

    wsf2ex6ACTION_INIT(const wsf2ex6ACTION_INIT&);
    wsf2ex6ACTION_INIT& operator= (const wsf2ex6ACTION_INIT&);
};

	

#endif // !wsf2ex6ACTION_INIT_H
