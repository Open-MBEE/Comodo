# COMODO

COMODO is a model transformation toolkit based on Xpand/Xtend that takes as input a UML model and transforms it into different artifacts depending on the selected target platform.

The toolkit has been developed by the European Southern Observatory (ESO) to build Telescope and Instrument control and supervisor applications for different software platforms. 

The input model must be in the EMF UML XMI format (.uml) and it should comply with the COMODO UML profile (comododProfile).

Currently, the supported targets are:
* **SCXML**: transform the input model into SCXML document.
* **VLTSW**: transform the input model into C++ application for the Very Large Telescope SW Platform.
* **ACS**: transform the input model into Java application for the ALMA Common SW platform.
* **RMQ**: transform the input model into Java application using RabbitMQ middleware.
* **JPF**: transform the input model (limited to State Machines) into Java application that can be verified by Java Pathfinder model checker.

The repository is organized in the following directories:
* _comodo/_ COMODO toolkit sources and the libraries. The config subdirectory contains the comodoProfile in MagicDraw and .uml format.
* _comodogui/_ Prototype GUI front-end application for the COMODO toolkit. The application allows to execute the supported model transformations and to load and run the SCXML model. The SCXML interpreter is based on Apache Commons SCXML.
* _Tools/scxmltester/_ Prototype test utility to load and run SCXML models based on Apache Commons SCXML.
* _Tools/uml2scxml/_ Prototype test utility to convert .uml models in SCXML.
* _Engines/scxml4cpp/_ SCXML intepreter developed at ESO for C++. It does NOT implement the full W3C SCXML reccomandation.
* _Engines/scxml4py/_ SCXML intepreter developed at ESO for Python. It does NOT implement the full W3C SCXML reccomandation.
* _doc/_ COMODO documentation.
* _Models/_ Collection of models that can be re-used.
