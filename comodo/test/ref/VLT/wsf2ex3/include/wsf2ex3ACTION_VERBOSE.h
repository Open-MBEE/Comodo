#ifndef wsf2ex3ACTION_VERBOSE_H
#define wsf2ex3ACTION_VERBOSE_H
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

#ifndef wsf2libACTION_VERBOSE_H
#include "wsf2libACTION_VERBOSE.h"
#endif


class wsf2ex3ACTION_MGR;    
class wsf2ex3CONFIG;
class wsf2ex3DATA;
class wsf2libCONTROL;


/** 
 * VERBOSE action class.    
 */
class wsf2ex3ACTION_VERBOSE : public wsf2libACTION_VERBOSE
{
  public:
    wsf2ex3ACTION_VERBOSE(wsf2ex3ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex3CONFIG& config, 
		       wsf2ex3DATA& data);
    virtual ~wsf2ex3ACTION_VERBOSE();
   
       
  private:
    wsf2ex3ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex3CONFIG&     mConfig;
    wsf2ex3DATA&       mData;

    wsf2ex3ACTION_VERBOSE(const wsf2ex3ACTION_VERBOSE&);
    wsf2ex3ACTION_VERBOSE& operator= (const wsf2ex3ACTION_VERBOSE&);
};

	

#endif // !wsf2ex3ACTION_VERBOSE_H

