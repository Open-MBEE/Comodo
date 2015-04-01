#ifndef wsf2ex5ACTION_OFF_H
#define wsf2ex5ACTION_OFF_H
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

#ifndef wsf2libACTION_OFF_H
#include "wsf2libACTION_OFF.h"
#endif


class wsf2ex5ACTION_MGR;    
class wsf2ex5CONFIG;
class wsf2ex5DATA;
class wsf2libCONTROL;


/** 
 * OFF action class.    
 */
class wsf2ex5ACTION_OFF : public wsf2libACTION_OFF
{
  public:
    wsf2ex5ACTION_OFF(wsf2ex5ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex5CONFIG& config, 
		       wsf2ex5DATA& data);
    virtual ~wsf2ex5ACTION_OFF();
   
       
  private:
    wsf2ex5ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex5CONFIG&     mConfig;
    wsf2ex5DATA&       mData;

    wsf2ex5ACTION_OFF(const wsf2ex5ACTION_OFF&);
    wsf2ex5ACTION_OFF& operator= (const wsf2ex5ACTION_OFF&);
};

	

#endif // !wsf2ex5ACTION_OFF_H

