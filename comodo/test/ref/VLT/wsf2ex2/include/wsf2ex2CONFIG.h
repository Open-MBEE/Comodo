#ifndef wsf2ex2CONFIG_H
#define wsf2ex2CONFIG_H
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
 * Shared configuration data.
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

class wsf2ex2CONFIG : public eccsERROR_CLASS
{
  public:
    wsf2ex2CONFIG(const dbSYMADDRESS& dbRoot, vltINT32 argCount, char* arg[]);
    ~wsf2ex2CONFIG();

    ccsCOMPL_STAT Init();

  private:
    wsf2ex2CONFIG(const wsf2ex2CONFIG&);
    wsf2ex2CONFIG& operator= (const wsf2ex2CONFIG&);
};

#endif // !wsf2ex2CONFIG_H

