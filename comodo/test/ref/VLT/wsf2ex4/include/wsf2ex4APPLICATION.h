#ifndef wsf2ex4APPLICATION_H
#define wsf2ex4APPLICATION_H
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


class wsf2ex4ACTION_MGR;

class wsf2ex4APPLICATION : public wsf2libAPPLICATION
{
  public:
    wsf2ex4APPLICATION();
    virtual ~wsf2ex4APPLICATION();

    ccsCOMPL_STAT Init(int argCount, char *arg[]);
    ccsCOMPL_STAT Run();

    wsf2ex4ACTION_MGR* GetActionMgr();
   
  private:
    wsf2ex4ACTION_MGR* mActionMgr;

    wsf2ex4APPLICATION(const wsf2ex4APPLICATION&);
    wsf2ex4APPLICATION& operator= (const wsf2ex4APPLICATION&);
};


#endif // !wsf2ex4APPLICATION_H

