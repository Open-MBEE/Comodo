# COMODO

COMODO is a model transformation toolkit based on Xpand/Xtend that takes as input a UML model and transforms it into different artifacts depending on the selected target.

The toolkit has been deleveloped by the European Southern Observatory (ESO) to build Telescope and Instrument control and supervisor applications for different software platforms. 

The input model must be in the EMF UML XMI format (.uml) and it should comply with the COMODO UML profile (comododProfile).

Currently, the supported targets are:
* SCXML: transform the input model into SCXML document
* VLTSW: transform the input model into C++ application for the Very Large Telescope SW Platform
* ACS: transform the input model into Java application for the ALMA Common SW platform
* RMQ: transform the input model into Java application using RabbitMQ middleware
* JPF: transform the input model (limited to State Machines) into Java application that can be model checked by Java Pathfinder

