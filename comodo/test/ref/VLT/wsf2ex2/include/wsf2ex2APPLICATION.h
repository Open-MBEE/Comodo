#ifndef wsf2ex2APPLICATION_H
#define wsf2ex2APPLICATION_H
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


class wsf2ex2ACTION_MGR;

class wsf2ex2APPLICATION : public wsf2libAPPLICATION
{
  public:
    wsf2ex2APPLICATION();
    virtual ~wsf2ex2APPLICATION();

    ccsCOMPL_STAT Init(int argCount, char *arg[]);
    ccsCOMPL_STAT Run();

    wsf2ex2ACTION_MGR* GetActionMgr();
   
  private:
    wsf2ex2ACTION_MGR* mActionMgr;

    wsf2ex2APPLICATION(const wsf2ex2APPLICATION&);
    wsf2ex2APPLICATION& operator= (const wsf2ex2APPLICATION&);
};


#endif // !wsf2ex2APPLICATION_H

