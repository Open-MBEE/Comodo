/*
 *    scampl4cpp/engine
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
 * $Id: test.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include <stdexcept>
#include <cppunit/TestResult.h>
#include <cppunit/TestResultCollector.h>
#include <cppunit/BriefTestProgressListener.h>
#include <cppunit/TestRunner.h>
#include <cppunit/CompilerOutputter.h>
#include <cppunit/extensions/TestFactoryRegistry.h>

using namespace CppUnit;

int main (int argc, char* argv[])
{	
	std::string testPath = (argc > 1) ? std::string(argv[1]) : "";
	//event manager and test controller
	TestResult controller;
	//Add a listener that colllects test result
	TestResultCollector collectedresults;
     	controller.addListener(&collectedresults);
	//Add a listener that print dots as test run
	BriefTestProgressListener progress;
	controller.addListener(&progress);
	//Add the top suite to the test runner
	TestRunner runner;
	runner.addTest(TestFactoryRegistry::getRegistry().makeTest());
	try
	{
		std::cout << " Running: "  <<  testPath ;
		runner.run( controller, testPath );
		std::cerr << std::endl;
 		// Print test in a compiler compatible format.
		CompilerOutputter outputter( &collectedresults, std::cerr);
		outputter.write();                      
	}
	catch(std::invalid_argument &e)  // Test path not resolved
	{
		std::cerr  <<  std::endl  
		<<  "ERROR: "  <<  e.what()
		<< std::endl;
		return 0;
	}
return collectedresults.wasSuccessful() ? 0 : 1;
};

