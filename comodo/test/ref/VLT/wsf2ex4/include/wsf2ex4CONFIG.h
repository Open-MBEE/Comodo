#ifndef wsf2ex4CONFIG_H
#define wsf2ex4CONFIG_H
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

class wsf2ex4CONFIG : public eccsERROR_CLASS
{
  public:
    wsf2ex4CONFIG(const dbSYMADDRESS& dbRoot, vltINT32 argCount, char* arg[]);
    ~wsf2ex4CONFIG();

    ccsCOMPL_STAT Init();

  private:
    wsf2ex4CONFIG(const wsf2ex4CONFIG&);
    wsf2ex4CONFIG& operator= (const wsf2ex4CONFIG&);
};

#endif // !wsf2ex4CONFIG_H

