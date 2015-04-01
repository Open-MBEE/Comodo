#ifndef wsf2ex5ACTION_VERSION_H
#define wsf2ex5ACTION_VERSION_H
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


class wsf2ex5ACTION_MGR;    
class wsf2ex5CONFIG;
class wsf2ex5DATA;
class wsf2libCONTROL;


/** 
 * VERSION action class.    
 */
class wsf2ex5ACTION_VERSION : public wsf2libACTION_VERSION
{
  public:
    wsf2ex5ACTION_VERSION(wsf2ex5ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex5CONFIG& config, 
		       wsf2ex5DATA& data);
    virtual ~wsf2ex5ACTION_VERSION();
   
    virtual ccsCOMPL_STAT Execute();
       
  private:
    wsf2ex5ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex5CONFIG&     mConfig;
    wsf2ex5DATA&       mData;

    wsf2ex5ACTION_VERSION(const wsf2ex5ACTION_VERSION&);
    wsf2ex5ACTION_VERSION& operator= (const wsf2ex5ACTION_VERSION&);
};

	

#endif // !wsf2ex5ACTION_VERSION_H

