#ifndef wsf2ex2ACTION_VERSION_H
#define wsf2ex2ACTION_VERSION_H
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

#ifndef wsf2libACTION_VERSION_H
#include "wsf2libACTION_VERSION.h"
#endif


class wsf2ex2ACTION_MGR;    
class wsf2ex2CONFIG;
class wsf2ex2DATA;
class wsf2libCONTROL;


/** 
 * VERSION action class.    
 */
class wsf2ex2ACTION_VERSION : public wsf2libACTION_VERSION
{
  public:
    wsf2ex2ACTION_VERSION(wsf2ex2ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex2CONFIG& config, 
		       wsf2ex2DATA& data);
    virtual ~wsf2ex2ACTION_VERSION();
   
    virtual ccsCOMPL_STAT Execute();
       
  private:
    wsf2ex2ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex2CONFIG&     mConfig;
    wsf2ex2DATA&       mData;

    wsf2ex2ACTION_VERSION(const wsf2ex2ACTION_VERSION&);
    wsf2ex2ACTION_VERSION& operator= (const wsf2ex2ACTION_VERSION&);
};

	

#endif // !wsf2ex2ACTION_VERSION_H

