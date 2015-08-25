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
 * $Id: SAXSCXMLReader.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include "SAXSCXMLReader.h"

#include "cppeng/Action.h"
#include "cppeng/Activity.h"
#include "cppeng/StateMachine.h"


using namespace scxml4cpp;


SAXSCXMLReader::SAXSCXMLReader() 
{
    // XML feature IDs
    NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";
    NAMESPACE_PREFIXES_FEATURE_ID = "http://xml.org/sax/features/namespace-prefixes";
    VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";
    SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";
    SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";
    HONOUR_ALL_SCHEMA_LOCATIONS_ID = "http://apache.org/xml/features/honour-all-schemaLocations";
    VALIDATE_ANNOTATIONS_ID = "http://apache.org/xml/features/validate-annotations";
    DYNAMIC_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/dynamic";
    XINCLUDE_FEATURE_ID = "http://apache.org/xml/features/xinclude";
    XINCLUDE_FIXUP_BASE_URIS_FEATURE_ID = "http://apache.org/xml/features/xinclude/fixup-base-uris";
    XINCLUDE_FIXUP_LANGUAGE_FEATURE_ID = "http://apache.org/xml/features/xinclude/fixup-language";

    // Default parser settings
    DEFAULT_PARSER_NAME = "org.apache.xerces.parsers.SAXParser";
    DEFAULT_REPETITION = 1;
    DEFAULT_MEMORY_USAGE = false;
    DEFAULT_TAGGINESS = false;

    DEFAULT_NAMESPACES = true;
    DEFAULT_NAMESPACE_PREFIXES = false;
    DEFAULT_VALIDATION = false;
    DEFAULT_SCHEMA_VALIDATION = false;
    DEFAULT_SCHEMA_FULL_CHECKING = false;
    DEFAULT_HONOUR_ALL_SCHEMA_LOCATIONS = false;
    DEFAULT_VALIDATE_ANNOTATIONS = false;
    DEFAULT_DYNAMIC_VALIDATION = false;
    DEFAULT_XINCLUDE = false;
    DEFAULT_XINCLUDE_FIXUP_BASE_URIS = true;
    DEFAULT_XINCLUDE_FIXUP_LANGUAGE = true;

/*
	NAMESPACE_SCXML = "http://www.w3.org/2005/07/scxml";
	NAMESPACE_COMMONS_SCXML = "http://commons.apache.org/scxml";
*/
	
    // Element names
    XML_SCXML = "scxml";
    XML_INITIAL = "initial";
    XML_STATE = "state";
    XML_PARALLEL = "parallel";
    XML_ = "";
    XML_ONENTRY = "onentry";
    XML_ONEXIT = "onexit";
    XML_TRANSITION = "transition";
    XML_HISTORY = "history";
    
/*
	XML_IZE = "ize";
	XML_DATAMODEL = "datamodel";
	XML_DONEDATA = "donedata";
	XML_CONTENT = "content";
	XML_DATA = "data";
	XML_INVOKE = "invoke";
	XML_PARAM = "param";
	XML_EXIT = "exit";
	XML_ASSIGN = "assign";
	XML_EVENT = "event";
	XML_SEND = "send";
	XML_RAISE = "raise";
	XML_CANCEL = "cancel";
	XML_IF = "if";
	XML_ELSEIF = "elseif";
	XML_ELSE = "else";
	XML_VAR = "var";
	XML_LOG = "log";
*/
	
    // Element's attributes names
    XML_ATTR_NAME = "name";
    XML_ATTR_ID = "id";
/*	
	XML_ATTR_INITIAL = "initial";
	XML_ATTR_XMLNS = "xmlns";
	XML_ATTR_VERSION = "version";
	XML_ATTR_PROFILE = "profile";
	XML_ATTR_EXMODE = "exmode";
	XML_ATTR_EVENT = "event";
	XML_ATTR_COND = "cond";
	XML_ATTR_TARGET = "target";
	XML_ATTR_ANCHOR = "anchor";
	XML_ATTR_TYPE = "type";
	XML_ATTR_LABEL = "label";
	XML_ATTR_EXPR = "expr";
	XML_ATTR_LEVEL = "level";
*/
    DEFAULT_SCXML_NAME = "DefaultSCXML";

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
    mSM = NULL;
}

SAXSCXMLReader::~SAXSCXMLReader()
{
}

SAXSCXMLReader::printResults(PrintWriter out, 
			     String uri, 
			     long time, 
			     long memory, 
			     boolean tagginess, 
			     int repetition)
{
}

StateMachine* SAXSCXMLReader::Read(std::string* filename, 
				   std::list<Action*>& actions, 
				   std::list<Activity*>& activities)
{

    try {
        XMLPlatformUtils::Initialize();
    } catch (const XMLException& toCatch) {
        char* message = XMLString::transcode(toCatch.getMessage());
	cout << "error: Unable to instantiate parser (" << DEFAULT_PARSER_NAME << "\n" <<
	     << message << "\n";
	XMLString::release(&message);
	return;
    }

    // create parser
    SAXParser* parser = new SAXParser();
}

