#ifndef wsf2ex4DATA_H
#define wsf2ex4DATA_H
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

/****************************************************************************
 * Shared runtime data.
 *---------------------------------------------------------------------------
 */

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#ifndef eccs_H
#include "eccs.h"
#endif

#ifndef eccsDB_ATTR_H
#include "eccsDB_ATTR.h"
#endif


class wsf2ex4CONFIG;


class wsf2ex4DATA : public eccsERROR_CLASS
{
  public:
    wsf2ex4DATA(const dbSYMADDRESS& dbRoot, wsf2ex4CONFIG& config);
    ~wsf2ex4DATA();

    ccsCOMPL_STAT Init();

  private:
    wsf2ex4CONFIG& mConfig;

    wsf2ex4DATA(const wsf2ex4DATA&);
    wsf2ex4DATA& operator= (const wsf2ex4DATA&);
};

#endif // !wsf2ex4DATA_H

