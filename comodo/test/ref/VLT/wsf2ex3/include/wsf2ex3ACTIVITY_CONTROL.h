#ifndef wsf2ex3ACTIVITY_CONTROL_H
#define wsf2ex3ACTIVITY_CONTROL_H
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

#ifndef wsf2libSCXML_ACTIVITY_H
#include "wsf2libSCXML_ACTIVITY.h"
#endif

#ifndef eccsErr_H
#include "eccsErr.h"
#endif

class wsf2libFSM;
class evhTHREADS;
class wsf2libCONTROL;
class wsf2ex3CONFIG;
class wsf2ex3DATA;


/** 
 * CONTROL activity class.    
 */
class wsf2ex3ACTIVITY_CONTROL : public wsf2libSCXML_ACTIVITY, public eccsERROR_CLASS
{
  public:
    wsf2ex3ACTIVITY_CONTROL(const std::string& procName, wsf2libFSM* fsm, 
    evhTHREADS& threadsHandler, wsf2libCONTROL& control,
    wsf2ex3CONFIG& config, wsf2ex3DATA& data);
    virtual ~wsf2ex3ACTIVITY_CONTROL();

    virtual void run();
   
  private:
    wsf2libFSM* mFsm;
	wsf2libCONTROL& mControl;
	wsf2ex3CONFIG& mConfig;
	wsf2ex3DATA& mData;
	
    wsf2ex3ACTIVITY_CONTROL(const wsf2ex3ACTIVITY_CONTROL&);
    wsf2ex3ACTIVITY_CONTROL& operator= (const wsf2ex3ACTIVITY_CONTROL&);
};

#endif // !wsf2ex3ACTIVITY_CONTROL_H


