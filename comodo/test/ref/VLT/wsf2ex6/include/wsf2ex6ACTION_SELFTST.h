#ifndef wsf2ex6ACTION_SELFTST_H
#define wsf2ex6ACTION_SELFTST_H
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

#ifndef wsf2libACTION_SELFTST_H
#include "wsf2libACTION_SELFTST.h"
#endif


class wsf2ex6ACTION_MGR;    
class wsf2ex6CONFIG;
class wsf2ex6DATA;
class wsf2libCONTROL;


/** 
 * SELFTST action class.    
 */
class wsf2ex6ACTION_SELFTST : public wsf2libACTION_SELFTST
{
  public:
    wsf2ex6ACTION_SELFTST(wsf2ex6ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex6CONFIG& config, 
		       wsf2ex6DATA& data);
    virtual ~wsf2ex6ACTION_SELFTST();
   
       
  private:
    wsf2ex6ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex6CONFIG&     mConfig;
    wsf2ex6DATA&       mData;

    wsf2ex6ACTION_SELFTST(const wsf2ex6ACTION_SELFTST&);
    wsf2ex6ACTION_SELFTST& operator= (const wsf2ex6ACTION_SELFTST&);
};

	

#endif // !wsf2ex6ACTION_SELFTST_H

