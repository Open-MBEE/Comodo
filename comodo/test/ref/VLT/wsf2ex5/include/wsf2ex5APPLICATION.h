#ifndef wsf2ex5APPLICATION_H
#define wsf2ex5APPLICATION_H
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


class wsf2ex5ACTION_MGR;

class wsf2ex5APPLICATION : public wsf2libAPPLICATION
{
  public:
    wsf2ex5APPLICATION();
    virtual ~wsf2ex5APPLICATION();

    ccsCOMPL_STAT Init(int argCount, char *arg[]);
    ccsCOMPL_STAT Run();

    wsf2ex5ACTION_MGR* GetActionMgr();
   
  private:
    wsf2ex5ACTION_MGR* mActionMgr;

    wsf2ex5APPLICATION(const wsf2ex5APPLICATION&);
    wsf2ex5APPLICATION& operator= (const wsf2ex5APPLICATION&);
};


#endif // !wsf2ex5APPLICATION_H

