#ifndef wsf2ex2ACTION_SETUP_H
#define wsf2ex2ACTION_SETUP_H
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

#ifndef wsf2libACTION_H
#include "wsf2libACTION.h"
#endif


class wsf2ex2ACTION_MGR;    
class wsf2ex2CONFIG;
class wsf2ex2DATA;
class wsf2libCONTROL;


/** 
 * SETUP action class.    
 */
class wsf2ex2ACTION_SETUP : public wsf2libACTION
{
  public:
    wsf2ex2ACTION_SETUP(wsf2ex2ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex2CONFIG& config, 
		       wsf2ex2DATA& data);
    virtual ~wsf2ex2ACTION_SETUP();

    virtual ccsCOMPL_STAT Init();
    virtual ccsCOMPL_STAT Execute();
    virtual ccsCOMPL_STAT Complete();
    virtual ccsCOMPL_STAT Abort();
    virtual ccsCOMPL_STAT Reject();
    virtual ccsCOMPL_STAT Supersede();
   
  private:
    wsf2ex2ACTION_MGR& mActionMgr;
    wsf2libCONTROL&    mControl;
    wsf2ex2CONFIG&     mConfig;
    wsf2ex2DATA&       mData;

    wsf2ex2ACTION_SETUP(const wsf2ex2ACTION_SETUP&);
    wsf2ex2ACTION_SETUP& operator= (const wsf2ex2ACTION_SETUP&);
};

	

#endif // !wsf2ex2ACTION_SETUP_H

