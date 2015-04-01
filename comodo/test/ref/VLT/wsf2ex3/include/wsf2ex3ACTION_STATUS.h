#ifndef wsf2ex3ACTION_STATUS_H
#define wsf2ex3ACTION_STATUS_H
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

#ifndef wsf2libACTION_STATUS_H
#include "wsf2libACTION_STATUS.h"
#endif


class wsf2ex3ACTION_MGR;    
class wsf2ex3CONFIG;
class wsf2ex3DATA;
class wsf2libCONTROL;


/** 
 * STATUS action class.    
 */
class wsf2ex3ACTION_STATUS : public wsf2libACTION_STATUS
{
  public:
    wsf2ex3ACTION_STATUS(wsf2ex3ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex3CONFIG& config, 
		       wsf2ex3DATA& data);
    virtual ~wsf2ex3ACTION_STATUS();
   
       
  private:
    wsf2ex3ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex3CONFIG&     mConfig;
    wsf2ex3DATA&       mData;

    wsf2ex3ACTION_STATUS(const wsf2ex3ACTION_STATUS&);
    wsf2ex3ACTION_STATUS& operator= (const wsf2ex3ACTION_STATUS&);
};

	

#endif // !wsf2ex3ACTION_STATUS_H

