/*******************************************************************************
 *    ALMA - Atacama Large Millimiter Array
 *
 *    (c) European Southern Observatory, 2002
 *    Copyright by ESO (in the framework of the ALMA collaboration)
 *    and Cosylab 2002, All rights reserved
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
 *    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA
 *
 * "@(#) $Id$" 
 *
 * who                when       what
 * ----------------  ----------  ----------------------------------------------
 * COMODO                        Created.
 * 
 */


#include <cppunit/TestFixture.h>
#include <cppunit/TestCaller.h>
#include <maciSimpleClient.h>

#include "SORTCommonC.h"
#include "CommonOperationsC.h"
#include "InstrumentC.h"

maci::SimpleClient client;

class InstrumentImplTest: public CppUnit::TestFixture 
{
	private:
	
		SORT::CommonOperations *CommonOperationsComponent;
		SORT::Instrument *InstrumentComponent;
		
	public:
	
		void setUp() 
		{
			CommonOperationsComponent = client.getComponent<SORT::CommonOperations>("testInstanceCommonOperations", 0, true);
			InstrumentComponent = client.getComponent<SORT::Instrument>("testInstanceInstrument", 0, true);
		}//

		void tearDown()
		{
			client.releaseComponent("testInstanceCommonOperations");
			client.releaseComponent("testInstanceInstrument");
		}//
		
		///////////////////////////////////////////////////////////
		// Test methods
		///////////////////////////////////////////////////////////
		
		void testTakeImage()
		{
			// TODO (generator autoimplementation)
		}//
		void testCameraOn()
		{
			// TODO (generator autoimplementation)
		}//
		void testCameraOff()
		{
			// TODO (generator autoimplementation)
		}//
		void testSetRGB()
		{
			// TODO (generator autoimplementation)
		}//
		void testSetPixelBias()
		{
			// TODO (generator autoimplementation)
		}//
		void testSetResetLevel()
		{
			// TODO (generator autoimplementation)
		}//
};

int main(int argc, char *argv[]) 
{
	client.init(argc, argv);
	
	client.login();
	
	// testTakeImage
	CppUnit::TestCaller<InstrumentImplTest> TakeImageTest ("testInstanceInstrumentImpl", &InstrumentImplTest::testTakeImage);
	
	// testCameraOn
	CppUnit::TestCaller<InstrumentImplTest> CameraOnTest ("testInstanceInstrumentImpl", &InstrumentImplTest::testCameraOn);
	
	// testCameraOff
	CppUnit::TestCaller<InstrumentImplTest> CameraOffTest ("testInstanceInstrumentImpl", &InstrumentImplTest::testCameraOff);
	
	// testSetRGB
	CppUnit::TestCaller<InstrumentImplTest> SetRGBTest ("testInstanceInstrumentImpl", &InstrumentImplTest::testSetRGB);
	
	// testSetPixelBias
	CppUnit::TestCaller<InstrumentImplTest> SetPixelBiasTest ("testInstanceInstrumentImpl", &InstrumentImplTest::testSetPixelBias);
	
	// testSetResetLevel
	CppUnit::TestCaller<InstrumentImplTest> SetResetLevelTest ("testInstanceInstrumentImpl", &InstrumentImplTest::testSetResetLevel);
	
	
	client.logout();
}
