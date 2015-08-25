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
# $Id: Makefile 1062 2015-07-14 13:36:55Z landolfa $
#

TARGET=scxml4cpppar
SONAME=libscxml4cpppar.so

ifndef INSTALL_LIB
INSTALL_LIB=/usr/local/lib
endif

ifndef INSTALL_INC
INSTALL_INC=/usr/local/include
endif

CFLAGS=-Wall -g
CC=g++
SOURCES=DOMSCXMLReader.cpp ActionTrace.cpp
OBJECTS=$(SOURCES:.cpp=.o)
HEADERS=../include
LIBS += -ldl -lc -lscxml4cppeng

#dependencies
%.o: %.cpp 
	$(CC) -c -fPIC $(CFLAGS) -I$(HEADERS) -I$(INSTALL_INC) $<

$(TARGET): $(OBJECTS) 
	$(CC) -L$(INSTALL_LIB) -shared -Wl,-soname,$(SONAME).1 -o $(SONAME).1.0 $(OBJECTS) $(LIBS) 

all: $(TARGET)

clean:
	rm -f -v *.o *~ $(SONAME).1.0 $(SONAME).1

install:
	cp -v $(SONAME).1.0 $(INSTALL_LIB)
	ln -sf $(SONAME).1.0 $(INSTALL_LIB)/$(SONAME)
	ln -sf $(SONAME).1.0 $(INSTALL_LIB)/$(SONAME).1
	mkdir -p $(INSTALL_INC)/scxml4cpp
	cp -v ../include/*.h $(INSTALL_INC)/scxml4cpp
