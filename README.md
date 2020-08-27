COMODO2
=======
COMODO2 is a Java tool that allows to transform a UML/SysML model into code for different software platforms.

Installation
------------
To install COMODO2 from GIT repository:

    $ git lfs install
    $ git clone https://gitlab.eso.org/ifw/comodo2.git comodo2  
    $ cd comodo2
    $ git lfs pull
    $ waf configure
    $ waf install

The installation of Large File Storage (lfs) is required to retrieve the JAR files used by COMODO2. This step (`git lfs install`) should be executed only once per account.

JARs (including comodo2.jar) are installed by waf in `$PREFIX/lib` directory.

A wrapper script, comodo, that sets the correct classpath is installed in `$PREFIX/bin` directory.

Execution
---------
COMODO2 can be executed by invoking a wrapper script which takes care to set the proper path for the required JAR files.

    $ comodo <parameters>

For example:

    $ comodo -i comodo2/test/hello/model/EELT_ICS_ApplicationFramework.uml -o ./gen -m "hellomalif hellomal" -t ELT-RAD -g ALL -a -d

The supported options are:

    -h, --help, Print help.
    -d, --debug, Enable printing of debug information.
    -i, --input-model, File path of the model to transform. Model should be in EMF XMI (.uml) format.
    -o, --output-path, Output directory path.
    -m, --modules, Specify the module(s) to generate.
    -t, --target-platform, Target platform [SCXML|ELT-RAD|ELT-MAL].		
    -c, --target-platform-config, Configuration parameters specific to a target platform.
    -g, --generation-mode, Generation mode [DEFAULT|UPDATE|ALL].
    -n, --no-backup, Disable automatic backup for generated files (i.e. generated files may overwrite existing files with the same name).
    -a, --avoid-fully-qualified, Avoid using fully qualified names.
     
Input Model
-----------
The input model should comply with COMODO profile and be stored in EMF XMI 5.x format.

For example, in MagicDrow, use the option: File -> Export to -> Eclipse UML2 XMI 5.x to export the model.

Target Platforms
----------------
The following target platforms are supported:

  - *SCXML* Transforms the input model into SCXML document(s). The generated SCXML document can be executed by an SCXML interpreted.
  - *ELT-MAL* Transforms the input model into XML/MAL ICD. The generated XML file can be transformed into code using CII tools.
  - *ELT-RAD* Transforms the input model into C++ code, SCXML, XML/MAL ICD, for the Rapid Application Development toolkit (RAD). The generated code can be executed on a machine installed with the ELT Development Environment and RAD.
   
Generation Modes
----------------
The following generation modes are supported:

  - *ALL* To be used only the very first time, it generates all files including actions, do-activities, actionMgr. If -n option is used, generated files will overwrite existing files without backup.
  - *UPDATE* To be used when new actions or do activities are added, it generates new action classes, new do-activities classes, and regenerate the actionMgr. If -n option is used, the actionMgr.cpp file will be overwritten without backup.
  - *DEFAULT* This is the default mode. It generates only files that should never be edited by the developer. E.g. the SCXML file, MAL/ICD, .rad.ev, etc.
  
