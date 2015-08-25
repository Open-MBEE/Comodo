/*
 *    scampl4cpp/parser
 *
 *    Copyright by European Southern Observatory, 2012
 *    All rights reserved
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation; either
 *    version 2.1 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 *
 *    You should have received a copy of the GNU Lesser General Public
 *    License along with this library; if not, write to the Free Software
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
 *    02111-1307 USA.
 */

/*
 * $Id: SAXSCXMLReader.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef SAXSCXMLREADER_H
#define SAXSCXMLREADER_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif


#ifndef SCXMLREADER_H
#include "SCXMLReader.h"
#endif

#include <string>
#include <list>
#include <xercesc/parsers/SAXParser.hpp>
#include <xercesc/sax/HandlerBase.hpp>
#include <xercesc/util/XMLString.hpp>

using namespace xercesc;

namespace scxml4cpp
{

    class Action;
    class Activity;
    class StateMachine;


    class SAXSCXMLReader : public SCXMLReader 
    {
      public:
	SAXSCXMLReader();
	~SAXSCXMLReader();

	printResults(PrintWriter out, 
		     String uri, 
		     long time, 
		     long memory, 
		     boolean tagginess, 
		     int repetition);

	StateMachine* Read(const std::string& filename, 
			   std::list<Action*>& actions, 
			   std::list<Activity*>& activities);


      protected:
	// XML feature IDs
	static std::string NAMESPACES_FEATURE_ID;
	static std::string NAMESPACE_PREFIXES_FEATURE_ID;
	static std::string VALIDATION_FEATURE_ID;
	static std::string SCHEMA_VALIDATION_FEATURE_ID;
	static std::string SCHEMA_FULL_CHECKING_FEATURE_ID;
	static std::string HONOUR_ALL_SCHEMA_LOCATIONS_ID;
	static std::string VALIDATE_ANNOTATIONS_ID;
	static std::string DYNAMIC_VALIDATION_FEATURE_ID;
	static std::string XINCLUDE_FEATURE_ID;
	static std::string XINCLUDE_FIXUP_BASE_URIS_FEATURE_ID;
	static std::string XINCLUDE_FIXUP_LANGUAGE_FEATURE_ID;

	// Default parser settings
	static std::string DEFAULT_PARSER_NAME;
	static int DEFAULT_REPETITION;
	static bool DEFAULT_MEMORY_USAGE;
	static bool DEFAULT_TAGGINESS;

	static bool DEFAULT_NAMESPACES;
	static bool DEFAULT_NAMESPACE_PREFIXES;
	static bool DEFAULT_VALIDATION;
	static bool DEFAULT_SCHEMA_VALIDATION;
	static bool DEFAULT_SCHEMA_FULL_CHECKING;
	static bool DEFAULT_HONOUR_ALL_SCHEMA_LOCATIONS;
	static bool DEFAULT_VALIDATE_ANNOTATIONS;
	static bool DEFAULT_DYNAMIC_VALIDATION;
	static bool DEFAULT_XINCLUDE;
	static bool DEFAULT_XINCLUDE_FIXUP_BASE_URIS;
	static bool DEFAULT_XINCLUDE_FIXUP_LANGUAGE;
    
/*
  static string NAMESPACE_SCXML = "http://www.w3.org/2005/07/scxml";
  static string NAMESPACE_COMMONS_SCXML = "http://commons.apache.org/scxml";
*/
	
	// Element names
	static std::string XML_SCXML;
	static std::string XML_INITIAL;
	static std::string XML_STATE;
	static std::string XML_PARALLEL;
	static std::string XML_;
	static std::string XML_ONENTRY;
	static std::string XML_ONEXIT;
	static std::string XML_TRANSITION;
	static std::string XML_HISTORY;

/*
  static std::string XML_IZE = "ize";
  static std::string XML_DATAMODEL = "datamodel";
  static std::string XML_DONEDATA = "donedata";
  static std::string XML_CONTENT = "content";
  static std::string XML_DATA = "data";
  static std::string XML_INVOKE = "invoke";
  static std::string XML_PARAM = "param";
  static std::string XML_EXIT = "exit";
  static std::string XML_ASSIGN = "assign";
  static std::string XML_EVENT = "event";
  static std::string XML_SEND = "send";
  static std::string XML_RAISE = "raise";
  static std::string XML_CANCEL = "cancel";
  static std::string XML_IF = "if";
  static std::string XML_ELSEIF = "elseif";
  static std::string XML_ELSE = "else";
  static std::string XML_VAR = "var";
  static std::string XML_LOG = "log";
*/
	
	// Element's attributes names
	static std::string XML_ATTR_NAME;
	static std::string XML_ATTR_ID;
/*	
	static std::string XML_ATTR_INITIAL = "initial";
	static std::string XML_ATTR_XMLNS = "xmlns";
	static std::string XML_ATTR_VERSION = "version";
	static std::string XML_ATTR_PROFILE = "profile";
	static std::string XML_ATTR_EXMODE = "exmode";
	static std::string XML_ATTR_EVENT = "event";
	static std::string XML_ATTR_COND = "cond";
	static std::string XML_ATTR_TARGET = "target";
	static std::string XML_ATTR_ANCHOR = "anchor";
	static std::string XML_ATTR_TYPE = "type";
	static std::string XML_ATTR_LABEL = "label";
	static std::string XML_ATTR_EXPR = "expr";
	static std::string XML_ATTR_LEVEL = "level";
*/
	static std::string DEFAULT_SCXML_NAME;

	// Data
	StateMachine* mSM = NULL;
	
	// Set<State> mStates = null;

	// @TODO remove this stuff
	long fElements;	/** Number of elements. */
	long fAttributes; /** Number of attributes. */
	long fCharacters; /** Number of characters. */
	long fIgnorableWhitespace; /** Number of ignorable whitespace characters. */
	long fTagCharacters; /** Number of characters of tags. */
	long fOtherCharacters; /** Number of other content characters for the "tagginess" calculation. */

      private:
	SAXSCXMLReader(const SAXSCXMLReader&);             //! Disable copy constructor
	SAXSCXMLReader& operator= (const SAXSCXMLReader&); //! Disable assignment operator
    };

}
#endif 
