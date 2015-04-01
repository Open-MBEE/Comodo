#ifndef wsf2ex6APPLICATION_H
#define wsf2ex6APPLICATION_H
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


#ifndef wsf2libAPPLICATION_H
#include "wsf2libAPPLICATION.h"
#endif


class wsf2ex6ACTION_MGR;

class wsf2ex6APPLICATION : public wsf2libAPPLICATION
{
  public:
    wsf2ex6APPLICATION();
    virtual ~wsf2ex6APPLICATION();

    ccsCOMPL_STAT Init(int argCount, char *arg[]);
    ccsCOMPL_STAT Run();

    wsf2ex6ACTION_MGR* GetActionMgr();
   
  private:
    wsf2ex6ACTION_MGR* mActionMgr;

    wsf2ex6APPLICATION(const wsf2ex6APPLICATION&);
    wsf2ex6APPLICATION& operator= (const wsf2ex6APPLICATION&);
};


#endif // !wsf2ex6APPLICATION_H

