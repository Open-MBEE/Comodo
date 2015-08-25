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
 * $Id: ActionTest.cpp 1061 2015-07-13 15:03:59Z landolfa $
 */


#include "ActionTest.h"

ActionTest::ActionTest(const string id)
	: Action(id)
{
	mExecuteCounter = 0;
	mEvaluateCounter = 0;
	mEvaluateResult = false;
}

void ActionTest::execute(Context* c)
{
	mExecuteCounter++;
	Event e = c->getLastEvent();
	cout << "ACTION " << getId() << " processing event: " << e.getId() << endl;
}

bool ActionTest::evaluate(Context* c)
{
	mEvaluateCounter++;
	return mEvaluateResult;
}

void ActionTest::setEvaluateResult(bool evaluateResult)
{
	mEvaluateResult = evaluateResult;
}

int ActionTest::getExecuteCounter()
{
	return mExecuteCounter;
}

int ActionTest::getEvaluateCounter()
{
	return mEvaluateCounter;
}


