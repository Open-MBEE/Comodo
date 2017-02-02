# COMODO

COMODO is a model transformation tool based on Xpand/Xtend that takes as input a UML model and transform it different artifacts depending on the selected target.

The toolkit has been deleveloped by the European Southern Observatory (ESO) to develop Telescope and Instrument control and supervisor applications for different software platforms. 

The input model must be in the EMF UML XMI format and use COMODO profile.

Currently, the supported targets are:
* VLTSW: transform the input model into C++ application for the Very Large Telescope SW Platform
* ACS: transform the input model into Java application for the ALMA Common SW platform
* RMQ: transform the input model into Java application using RabbitMQ middleware
* JPF: transform the input model into Java application that can be model checked by Java Pathfinder

