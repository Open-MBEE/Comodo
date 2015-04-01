#ifndef wsf2ex2DEFINES_H
#define wsf2ex2DEFINES_H
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
#define wsf2ex2EVENTS_FILENAME       "wsf2ex2Events.txt"
#define wsf2ex2MODEL_FILENAME        "wsf2ex2.xml"

/******************************************************************************
 * Dictionary for FITS log
 ******************************************************************************/
#define wsf2ex2FITS_DICT             ""


/******************************************************************************
 * Database point and attributes path
 ******************************************************************************/
#define wsf2ex2DB_ROOT_POINT         "Appl_data:wsf2ex2"
#define wsf2ex2DB_CMDH_DEST_POINT    "" 

#define wsf2ex2DB_CONTROL_POINT      ":control"

#define wsf2ex2DB_CONFIG_POINT       ":config"
/* 
 * Here go your configuration database attributes path 
 * ...
 */

#define wsf2ex2DB_DATA_POINT          ":data"
/* 
 * Here go your data database attributes path
 * ...
 */

#endif // !wsf2ex2DEFINES_H

