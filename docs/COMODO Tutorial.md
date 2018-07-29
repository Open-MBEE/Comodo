# COMODO Tutorial

## Adding Support for New Target Platform

### Supported Model Formats 
COMODO supports EMF UML2 XMI 2.x or greater (file extension is .uml). Among the tools able to export in EMF UML2 XMI, there are:
*	Papirus: natively supports EMF UML2 XMI 2.x 
*	MagicDraw: use the option File -> Export To -> Eclipse UML2 (v2.x) XMI 

Note that COMODO test models were created from MagicDraw. For Papirus, some additional work may be needed.

Models should comply with COMODO profile located in comodo/config directory.

### Adding support for a new SW platform
For each SW platform there is MWE workflow file (extension .mwe). All workflows are in comodo/src/comodo/mwe directory. In order to add a new platform: 
*	Create a new MWE file (simply take an existing one as template, for example SCXMLWorkflow.mwe) 
*	Add in comodo/src/comodo/util/BaseStaticConfig.java class a constant string to identify the platform (for example for the VLT SW platform the constant is TARGET_PLATFORM_VLT = "VLT"). 
*	Add in comodo/src/comodo/util/BaseStaticConfig.java class a constant string with the name of the MWE file (for example for the VLT SW platform it is MWE_VLT = MWE_BASE_DIR + "/VLTWorkflow.mwe"). Filename should follow the convetion: Workflow.mwe with = the SW platform identifier. 
*	Add in comodo/src/comodo/gen/COMODO.java class the support for the new SW platform option (-t). 

SW Platform identifier alredy in use: 
*	RMQ = Java, RabbitMQ middleware 
*	RMQSA = Java, RabbitMQ middleware, State Analysis 
*	VLT = VLTSW, C++ 
*	ACS = ACS, Java 
*	SCXML = generates only SCXML file from a UML State Machine model 
*	JPFSC = Java Pathfinder Statechart 

The MWE workflow receives from the comodo/COMODO.java class the following parameters: 
*	profileFileURI filename/path of a valid UML profile. 
*	modelFileURI filename/path of a valid UML model to transform. 
*	moduleName name of the UML <> to process. 
*	generationMode flag to indicate whether to generate all files or only the ones that can be overwritten by the generator. 
*	ouputFolderURI the path to the output directory where all generated files are stored 

#### Workflow (MWE)
The MWE workflow file should be structured as follows: 
*	Two standard initializations: the first one to se the current directory and the second one to setup the framework for UML2.
~~~~
<bean class="org.eclipse.emf.mwe.utils.StandaloneSetup" platformUri=".." />
<bean class="org.eclipse.xtend.typesystem.uml2.Setup" standardUML2Setup="true"/>
~~~~
*	Profile registration: the filename to a valid UML2 profile has to be passed (comodoProfile.uml for example) via the "profileFileURI" parameter. This section can be skipped if no special profile is used and the default UML2 profile is used to check/transform the model.
~~~~
    <!-- Register UML2 profile - metamodel -->
   <bean id="default_profile" class="org.eclipse.xtend.typesystem.uml2.profile.ProfileMetaModel">
      <profile value="${profileFileURI}"/>
   </bean>
~~~~   
*	Loading the UML model to transform: the filename to a valid UML2 model has to be be passed via the "modelFileURI" parameter
~~~~
   <component class="org.eclipse.emf.mwe.utils.Reader">
        <uri value="${modelFileURI}" />
        <modelSlot value="model" />
    </component>
~~~~
*	Validation of the UML model. One for each Check file to apply to the Model. 
~~~~
   <component class="org.eclipse.xtend.check.CheckComponent">
      <metaModel idRef="default_profile"/>
      <globalVarDef name="ModuleName" value="'${moduleName}'"/>
      <checkFile value="comodo/src/comodo/constraints/ModuleConstraints"/>
      <emfAllChildrenSlot value="model"/>
   </component>
~~~~   
*	Applying the transformation to the UML model. One for each Xpand transformation entry point. 
~~~~
   <component id="COMODO_COMPONENT" class="org.eclipse.xpand2.Generator" skipOnErrors="false">
      <fileEncoding value="UTF-8" />
      <metaModel idRef="default_profile"/>
      <expand value="comodo::templates::vlt::Root::Root FOR model"/>
      <globalVarDef name="ModuleName" value="'${moduleName}'"/>
      <globalVarDef name="GenerationMode" value="'${generationMode}'"/>
      <outlet path="${ouputFolderURI}">
         <postprocessor class="org.eclipse.xpand2.output.JavaBeautifier" />
      </outlet>
      <!-- prSrcPaths value="${ouputFolderURI}"/ !-->
   </component>
~~~~

#### Verifying UML Models (Check) 
Check files have extension .chk and contains rules that are applied to the model. All rules in a file are applied to the model and if one fails, an ERROR message is returned by the MWE workflow. 
In order to add new rules: 
*	Create a new file with extension .chk in comodo/src/comodo/constraints. 
*	Update the MWE workflow file with a new to invoke the Check fie. 

#### Navigating UML Models (Xtend) 
Xtend functions required to navigate a specific UML element are grouped in a file and the filename should reflect the UML element name. For example artifatc.ext contains all the Xtend functions to navigate UML Artifact elements. Other Xtend files are: 
*	profile.ext and profilestereotype.ext deal with stereotypes defined in the comodoProfile. 
*	scxmlutil.ext contains all the functions to navigate UML State Machine. 

Xtend files related to a given profile are grouped in the same directory. For example Xtend files to navigate comodoProfile based models are in comodo/src/comodo/xtend/cmdo/ directory. For additional profiles, new directories should to be added. For example for the State Analysis profile, the comodo/src/comodo/xtend/sa/ directory has been created. 

Xtend functions can be "extended" using Java code which should be located in comodo/src/comodo/util/ 

#### Writing templates to generate the code (Xpand) 
Xpand templates are in comodo/src/comodo/templates directory. They are organized in subdirectories: one per SW platform and within a SW platform one directory per output language: comodo/src/comodo/templates/. For example directory comodo/src/comodo/templates/vlt/cpp contains all Xpand templates used to generate C++ code for the VLT platform. The programming language identifiers used so far are: 
*	txt = generate text files including makefiles, scripts, etc. 
*	cpp = C++ source and header files (using _C and _H postfix to distiguish between source and header file) 
*	java = Java files 
*	py = Python files 
*	tcl = TCL/TK files 
*	xml = XML files 
*	idl = IDL files 

#### Adding a new test/example 
*	In test/models create a directory with the name of the example (for example myNewTest) 
*	In test/models/myNewTest export from MagicDraw the model in EMF UML2 XMI format 
*	In test/models/myNewTest create the prepared-impl directory that should contains hand made code (if any) for the example. The code should be separated per SW platform. In case of VLT the code should go into est/models/myNewTest/prepared-impl/VLT directory 
*	Add to test/build.xml a new target to generate the code for the myNewTest. Make sure you properly define platform, modules, model, and mode (mode = "ALL" -> all files are generated even the ones that should not be overwritten by the generator (i.e. files that could be modify by the developers; mode = "NORMAL" -> only files that should never be modify by the developer are generated) 
~~~~
   <target name="myNewTest" description="Generates myNewTestcode from myNewTest model for VLT platform" >
       <antcall target="generate">
           <param name="MODEL" value="${MOD_DIR}/myNewTest/myNewTest.uml"/>
           <param name="PLATFORM" value="VLT"/>
           <param name="MODE" value="all"/>  
           <param name="MODULES" value="myNewTestPackage"/>
       </antcall>
    </target>
~~~~    
*	Run the ANT task and verify what is generated in test/output/VLT directory 
*	When the output has been verified it can be copied to test/ref/VLT directory to update the reference files 
