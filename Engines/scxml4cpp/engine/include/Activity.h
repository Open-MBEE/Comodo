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
 * $Id: Activity.h 1061 2015-07-13 15:03:59Z landolfa $
 */

#ifndef ACTIVITY_H
#define ACTIVITY_H

#ifndef __cplusplus
#error This is a C++ include file and cannot be used from plain C
#endif

#include <string>

namespace scxml4cpp 
{

    class Activity
    {
      public:
	Activity(const std::string& id);
	virtual 	        ~Activity();

	virtual void	start() = 0;
	virtual void	stop() = 0;
	virtual void 	run() = 0;

	bool 		isRunning();
	void                setRunning(const bool running);
	std::string         getId() const;
	void                setId(const std::string& id);

      private:
	std::string         mId;
	bool 	        mRunning;

	Activity(const Activity&);             //! Disable copy constructor
	Activity& operator= (const Activity&); //! Disable assignment operator
    };

}
#endif
