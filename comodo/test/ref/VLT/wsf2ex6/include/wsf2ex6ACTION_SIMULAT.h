#ifndef wsf2ex6ACTION_SIMULAT_H
#define wsf2ex6ACTION_SIMULAT_H
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

#ifndef wsf2libACTION_SIMULAT_H
#include "wsf2libACTION_SIMULAT.h"
#endif


class wsf2ex6ACTION_MGR;    
class wsf2ex6CONFIG;
class wsf2ex6DATA;
class wsf2libCONTROL;


/** 
 * SIMULAT action class.    
 */
class wsf2ex6ACTION_SIMULAT : public wsf2libACTION_SIMULAT
{
  public:
    wsf2ex6ACTION_SIMULAT(wsf2ex6ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex6CONFIG& config, 
		       wsf2ex6DATA& data);
    virtual ~wsf2ex6ACTION_SIMULAT();
   
       
  private:
    wsf2ex6ACTION_MGR& mActionMgr;
    wsf2libCONTROL&     mControl;
    wsf2ex6CONFIG&     mConfig;
    wsf2ex6DATA&       mData;

    wsf2ex6ACTION_SIMULAT(const wsf2ex6ACTION_SIMULAT&);
    wsf2ex6ACTION_SIMULAT& operator= (const wsf2ex6ACTION_SIMULAT&);
};

	

#endif // !wsf2ex6ACTION_SIMULAT_H

