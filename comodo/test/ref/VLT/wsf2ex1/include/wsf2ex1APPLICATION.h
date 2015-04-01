#ifndef wsf2ex1APPLICATION_H
#define wsf2ex1APPLICATION_H
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


class wsf2ex1ACTION_MGR;

class wsf2ex1APPLICATION : public wsf2libAPPLICATION
{
  public:
    wsf2ex1APPLICATION();
    virtual ~wsf2ex1APPLICATION();

    ccsCOMPL_STAT Init(int argCount, char *arg[]);
    ccsCOMPL_STAT Run();

    wsf2ex1ACTION_MGR* GetActionMgr();
   
  private:
    wsf2ex1ACTION_MGR* mActionMgr;

    wsf2ex1APPLICATION(const wsf2ex1APPLICATION&);
    wsf2ex1APPLICATION& operator= (const wsf2ex1APPLICATION&);
};


#endif // !wsf2ex1APPLICATION_H

