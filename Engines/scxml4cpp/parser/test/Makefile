#
#    scampl4cpp/parser
#
#    Copyright by European Southern Observatory, 2012
#    All rights reserved
#
#    This library is free software; you can redistribute it and/or
#    modify it under the terms of the GNU Lesser General Public
#    License as published by the Free Software Foundation; either
#    version 2.1 of the License, or (at your option) any later version.
#
#    This library is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
#    Lesser General Public License for more details.
#
#    You should have received a copy of the GNU Lesser General Public
#    License along with this library; if not, write to the Free Software
#    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 
#    02111-1307 USA.
#

#
# $Id: Makefile 1061 2015-07-13 15:03:59Z landolfa $
#

### Makefile
#flags

CFLAGS=-Wall
CC=g++
SOURCES=ActionTest.cpp ActivityTest.cpp TestSimple.cpp TestParallel.cpp test.cpp
OBJECTS=$(SOURCES:.cpp=.o)
HEADERS=include
SOURCE=../src/
#LIBS += -lboost_thread-mt -ldl -lcppunit -lxerces-c -lcppeng -lcpppar
LIBS += -lboost_thread -ldl -lcppunit -lxerces-c -lcppeng -lcpppar

all: test

test: $(OBJECTS)
	$(CC) $(OBJECTS) $(LIBS) -o test -L$(INTROOT)/lib

#dependencies
%.o: %.cpp 
	$(CC) -c $(CFLAGS) -I./$(HEADERS) -I$(INTROOT)/include $<

clean:
	rm -f -v *.o *~ test
