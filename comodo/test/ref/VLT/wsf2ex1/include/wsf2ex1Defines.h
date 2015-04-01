#ifndef wsf2ex1DEFINES_H
#define wsf2ex1DEFINES_H
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


/******************************************************************************
 * Configuration files
 ******************************************************************************/
#define wsf2ex1EVENTS_FILENAME       "wsf2ex1Events.txt"
#define wsf2ex1MODEL_FILENAME        "wsf2ex1.xml"

/******************************************************************************
 * Dictionary for FITS log
 ******************************************************************************/
#define wsf2ex1FITS_DICT             ""


/******************************************************************************
 * Database point and attributes path
 ******************************************************************************/
#define wsf2ex1DB_ROOT_POINT         "Appl_data:wsf2ex1"
#define wsf2ex1DB_CMDH_DEST_POINT    "" 

#define wsf2ex1DB_CONTROL_POINT      ":control"

#define wsf2ex1DB_CONFIG_POINT       ":config"
/* 
 * Here go your configuration database attributes path 
 * ...
 */

#define wsf2ex1DB_DATA_POINT          ":data"
/* 
 * Here go your data database attributes path
 * ...
 */

#endif // !wsf2ex1DEFINES_H

