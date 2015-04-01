#ifndef wsf2ex3APPLICATION_H
#define wsf2ex3APPLICATION_H
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


class wsf2ex3ACTION_MGR;

class wsf2ex3APPLICATION : public wsf2libAPPLICATION
{
  public:
    wsf2ex3APPLICATION();
    virtual ~wsf2ex3APPLICATION();

    ccsCOMPL_STAT Init(int argCount, char *arg[]);
    ccsCOMPL_STAT Run();

    wsf2ex3ACTION_MGR* GetActionMgr();
   
  private:
    wsf2ex3ACTION_MGR* mActionMgr;

    wsf2ex3APPLICATION(const wsf2ex3APPLICATION&);
    wsf2ex3APPLICATION& operator= (const wsf2ex3APPLICATION&);
};


#endif // !wsf2ex3APPLICATION_H

