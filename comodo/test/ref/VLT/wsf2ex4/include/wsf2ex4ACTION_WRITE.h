#ifndef wsf2ex4ACTION_WRITE_H
#define wsf2ex4ACTION_WRITE_H
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


class wsf2ex4ACTION_MGR;    
class wsf2ex4CONFIG;
class wsf2ex4DATA;
class wsf2libCONTROL;


/** 
 * WRITE action class.    
 */
class wsf2ex4ACTION_WRITE : public wsf2libACTION
{
  public:
    wsf2ex4ACTION_WRITE(wsf2ex4ACTION_MGR& actionMgr,
		       wsf2libCONTROL& control, 
		       wsf2ex4CONFIG& config, 
		       wsf2ex4DATA& data);
    virtual ~wsf2ex4ACTION_WRITE();

    virtual ccsCOMPL_STAT Init();
    virtual ccsCOMPL_STAT Execute();
    virtual ccsCOMPL_STAT Complete();
    virtual ccsCOMPL_STAT Abort();
    virtual ccsCOMPL_STAT Reject();
    virtual ccsCOMPL_STAT Supersede();
   
  private:
    wsf2ex4ACTION_MGR& mActionMgr;
    wsf2libCONTROL&    mControl;
    wsf2ex4CONFIG&     mConfig;
    wsf2ex4DATA&       mData;

    wsf2ex4ACTION_WRITE(const wsf2ex4ACTION_WRITE&);
    wsf2ex4ACTION_WRITE& operator= (const wsf2ex4ACTION_WRITE&);
};

	

#endif // !wsf2ex4ACTION_WRITE_H

