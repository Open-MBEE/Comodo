#ifndef wsf2ex3DEFINES_H
#define wsf2ex3DEFINES_H
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
#define wsf2ex3EVENTS_FILENAME       "wsf2ex3Events.txt"
#define wsf2ex3MODEL_FILENAME        "wsf2ex3.xml"

/******************************************************************************
 * Dictionary for FITS log
 ******************************************************************************/
#define wsf2ex3FITS_DICT             ""


/******************************************************************************
 * Database point and attributes path
 ******************************************************************************/
#define wsf2ex3DB_ROOT_POINT         "Appl_data:wsf2ex3"
#define wsf2ex3DB_CMDH_DEST_POINT    "" 

#define wsf2ex3DB_CONTROL_POINT      ":control"

#define wsf2ex3DB_CONFIG_POINT       ":config"
/* 
 * Here go your configuration database attributes path 
 * ...
 */

#define wsf2ex3DB_DATA_POINT          ":data"
/* 
 * Here go your data database attributes path
 * ...
 */

#endif // !wsf2ex3DEFINES_H

