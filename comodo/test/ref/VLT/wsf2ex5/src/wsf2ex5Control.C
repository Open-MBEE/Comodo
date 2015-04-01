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

/******************************************************************************
*   NAME
*   wsf2ex5Control - control control process
* 
*   SYNOPSIS
*   wsf2ex5Control [-r dbRoot] [-n processName] 
* 
*   DESCRIPTION
*
*   The optional argument <dbRoot> is the root point of the database branch. 
*   If omitted it is defaulted to the hard-coded value of ":Appl_data:TCS".
*
*   The optional argument <processName> is the name under which the process'
*   instance registers with the CCS and hence the destination process name 
*   for commands to it. If omitted, the instance registers with CCS with 
*   its UNIX process name, as passed by the shell in argv[0].
*
*   FILES
*   wsf2ex5_ERRORS - r - error message definitions
*
*   ENVIRONMENT
*
*   COMPILATION FLAGS
*
*   COMMANDS
*   all standard commands
*
*   RETURN VALUES
*   0 if everything OK
*   1 on error
*
*   CAUTIONS 
*
*   EXAMPLES
*
*   SEE ALSO
*
*   BUGS   
* 
*------------------------------------------------------------------------------
*/

#define _POSIX_SOURCE 1
#include "vltPort.h"

#include "wsf2ex5.h"
wsf2ex5WSF_RCSID("@(#) $Id: wsf2ex5Control.C 593 2012-04-22 12:12:12Z landolfa $")

#include "wsf2ex5APPLICATION.h"
#include "wsf2ex5Errors.h"

#include "wsf2libLOG.h"
#include "wsf2libHELPER.h"

#include "eccsTestTools.h"

#include <stdlib.h>


/**
 * Main Function
 */
int main(int argc, char *argv[])
{
    wsf2libLOG_TRACE();
    
    eccsLogSave(FALSE);

    // Create and initialize the application object
    wsf2ex5APPLICATION app;
    if (wsf2libHELPER::ObjectOk(&app, "APPLICATION") == ccsFALSE)
		{
		wsf2libLOG_DEBUG() << "Cannot create application object";
		errAdd(wsf2ex5MOD, wsf2ex5ERR_FATAL, __FILE_LINE__, "cannot create APPLICATION object");
        errCloseStack();
        return EXIT_FAILURE;
		}
    if (app.Init(argc, argv) == FAILURE)
        {
		wsf2libLOG_DEBUG() << "Cannot initialize application object";
        errAdd(wsf2ex5MOD, wsf2ex5ERR_INIT, __FILE_LINE__, "cannot initialize the application");
        errCloseStack();
        return EXIT_FAILURE;
        } 

    // Run the event loop
    if (app.Run() == FAILURE)
		{
		wsf2libLOG_DEBUG() << "Cannot start main loop";
        errAdd(wsf2ex5MOD, wsf2ex5ERR_INIT, __FILE_LINE__, "cannot start the application");
        errCloseStack();
		return EXIT_FAILURE;
		} 

    return EXIT_SUCCESS;
}

// __oOo__

