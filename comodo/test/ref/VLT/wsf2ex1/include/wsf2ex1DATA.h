#ifndef wsf2ex1DATA_H
#define wsf2ex1DATA_H
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


class wsf2ex1CONFIG;


class wsf2ex1DATA : public eccsERROR_CLASS
{
  public:
    wsf2ex1DATA(const dbSYMADDRESS& dbRoot, wsf2ex1CONFIG& config);
    ~wsf2ex1DATA();

    ccsCOMPL_STAT Init();

  private:
    wsf2ex1CONFIG& mConfig;

    wsf2ex1DATA(const wsf2ex1DATA&);
    wsf2ex1DATA& operator= (const wsf2ex1DATA&);
};

#endif // !wsf2ex1DATA_H

