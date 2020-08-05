package comodo2.templates.elt.cpp;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import comodo2.queries.QClass;
import comodo2.queries.QStateMachine;
import comodo2.utils.Actions;
import comodo2.utils.FilesHelper;
import java.util.TreeSet;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

public class RadAction implements IGenerator {

	private static final Logger mLogger = Logger.getLogger(comodo2.engine.Main.class);

	@Inject
	@Extension
	private QClass mQClass;

	@Inject
	@Extension
	private QStateMachine mQStateMachine;

	@Inject
	@Extension
	private FilesHelper mFilesHelper;

	@Inject
	@Extension
	private Actions mActions;

	/**
	 * Transform UML State Machine associated to a class (classifier behavior)
	 * into RAD actions classes.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if ((mQClass.isToBeGenerated(e) && mQClass.hasStateMachines(e))) {
				TreeSet<String> actionNames = new TreeSet<String>();
				TreeSet<String> guardNames = new TreeSet<String>();
				for (final StateMachine sm : mQClass.getStateMachines(e)) {
					Iterables.<String>addAll(actionNames, mQStateMachine.getAllActionNames(sm));
					Iterables.<String>addAll(guardNames, mQStateMachine.getAllGuardNames(sm));
				}

				TreeSet<String> mergeClassNames = mActions.getMergeClassNames(mActions.getClassNames(actionNames), mActions.getClassNames(guardNames));
				for (final String c : mergeClassNames) {
					if ((((Config.getInstance().hasTargetPlatformCfgOption(Config.ELT_RAD_OPT_NOACTIONSTD)).booleanValue() == true) || 
							(((Config.getInstance().hasTargetPlatformCfgOption(Config.ELT_RAD_OPT_NOACTIONSTD)).booleanValue() == false) && 
									(!Objects.equal(c, "ActionsStd"))))) {
						if (mFilesHelper.skipFile(mFilesHelper.toAbsolutePath(mFilesHelper.toHppFilePath(c))) == false) {
							mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toHppFilePath(c)));
							fsa.generateFile(mFilesHelper.toHppFilePath(c), this.generateHeader(Config.getInstance().getCurrentModule(), c, actionNames, guardNames));
						}
						if (mFilesHelper.skipFile(mFilesHelper.toAbsolutePath(mFilesHelper.toCppFilePath(c))) == false) {
							mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toCppFilePath(c)));
							fsa.generateFile(mFilesHelper.toCppFilePath(c), this.generateSource(Config.getInstance().getCurrentModule(), c, actionNames, guardNames));
						}
					}
				}
			}
		}
	}

	/**
	 * Actions.hpp
	 */
	public CharSequence generateHeader(final String moduleName, final String className, final TreeSet<String> actionNames, final TreeSet<String> guardNames) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppAction.stg");
			ST st = g.getInstanceOf("ActionHeader");
			st.add("moduleName", moduleName);
			st.add("moduleNameUpperCase", moduleName.toUpperCase());
			st.add("moduleNameLowerCase", moduleName.toLowerCase());
			st.add("className", className);	
			st.add("classNameUpperCase", className.toUpperCase());	
			st.add("actionMethodsDeclaration" , printActionMethodsDeclaration(mActions.getMethodNames(actionNames, className)));
			st.add("guardMethodsDeclaration" , printGuardMethodsDeclaration(mActions.getMethodNames(guardNames, className)));			
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating header file for " + className + " class (" + throwable.getMessage() + ").");
		}
		return "";
	}

	public CharSequence printActionMethodsDeclaration(final TreeSet<String> actionNames) {
		String str = "";
		for (final String name : actionNames) {
			str += "\n";
			str += "/**\n";
		    str += " * Method implementing the " + name + " action.\n";		    
		    str += " * @param[in] c Context containing the last event received by the State Machine.\n";
		    str += " */\n";
		    str += "void " + name + "(scxml4cpp::Context* c);";
		}
		return str;		
	}

	public CharSequence printGuardMethodsDeclaration(final TreeSet<String> guardNames) {
		String str = "";
		for (final String name : guardNames) {
		    str += "\n";
		    str += "/**\n";
		    str += " * Method implementing the " + name + " guard.\n";		    
		    str += " * @param[in] c Context containing the last event received by the State Machine.\n";
		    str += " * @return true If the guard is satisfied, false otherwise.\n";
		    str += " */\n";
		    str += "bool " + name + "(scxml4cpp::Context* c);";
		}
		return str;		
	}

	/**
	 * Actions.cpp
	 */
	public CharSequence generateSource(final String moduleName, final String className, final TreeSet<String> actionNames, final TreeSet<String> guardNames) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppAction.stg");
			ST st = g.getInstanceOf("ActionSource");
			st.add("moduleName", moduleName);
			st.add("moduleNameLowerCase", moduleName.toLowerCase());
			st.add("className", className);	
			st.add("classFileName", mFilesHelper.toFileName(className));
			st.add("actionMethodsImpl", printActionMethodsImpl(className, mActions.getMethodNames(actionNames, className)));
			st.add("guardMethodsImpl", printGuardMethodsImpl(className, mActions.getMethodNames(guardNames, className)));
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating source file for " + className + " class (" + throwable.getMessage() + ").");
		}
		return "";
	}
	

	public CharSequence printActionMethodsImpl(final String className, final TreeSet<String> actionNames) {
		String str = "";
		for (final String name : actionNames) {
			str += "\n";
			str += "void " + className + "::" + name + "(scxml4cpp::Context* c) {\n";
			str += "    RAD_TRACE(GetLogger());\n";
			str += "}\n";
		}
		return str;		
	}

	public CharSequence printGuardMethodsImpl(final String className, final TreeSet<String> guardNames) {
		String str = "";
		for (final String name : guardNames) {
			str += "\n";
			str += "bool " + className + "::" + name + "(scxml4cpp::Context* c) {\n";
			str += "    RAD_TRACE(GetLogger());\n";
			str += "    return false;\n";			
			str += "}\n";
		}
		return str;		
	}
}
