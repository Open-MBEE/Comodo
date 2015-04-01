#ifndef wsf2ex2ACTION_VERBOSE_H
#define wsf2ex2ACTION_VERBOSE_H
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


class wsf2ex2ACTION_MGR;    
class wsf2ex2CONFIG;
class wsf2ex2DATA;
class wsf2libCONTROL;


/** 
 * VERBOSE action class.    
 */
class wsf2ex2ACTION_VERBOSE : public wsf2libACTION_VERBOSE
{
  public:
    wsf2ex2ACTION_VERBOSE(wsf2ex2ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex2CONFIG& config, 
		       wsf2ex2DATA& data);
    virtual ~wsf2ex2ACTION_VERBOSE();
   
       
  private:
    wsf2ex2ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex2CONFIG&     mConfig;
    wsf2ex2DATA&       mData;

    wsf2ex2ACTION_VERBOSE(const wsf2ex2ACTION_VERBOSE&);
    wsf2ex2ACTION_VERBOSE& operator= (const wsf2ex2ACTION_VERBOSE&);
};

	

#endif // !wsf2ex2ACTION_VERBOSE_H

