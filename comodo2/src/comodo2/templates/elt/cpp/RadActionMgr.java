package comodo2.templates.elt.cpp;

import com.google.common.collect.Iterables;
import comodo2.engine.Config;
import comodo2.queries.QClass;
import comodo2.queries.QStateMachine;
import comodo2.utils.Actions;
import comodo2.utils.FilesHelper;
import java.util.TreeSet;
import javax.inject.Inject;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.StateMachine;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.generator.IFileSystemAccess;
import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.xbase.lib.Extension;
import org.eclipse.xtext.xbase.lib.IteratorExtensions;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;
import org.apache.log4j.Logger;

@SuppressWarnings("all")
public class RadActionMgr implements IGenerator {
	
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
	 * into a RAD ActionMgr class.
	 */
	@Override
	public void doGenerate(final Resource input, final IFileSystemAccess fsa) {
		Iterable<org.eclipse.uml2.uml.Class> _filter = Iterables.<org.eclipse.uml2.uml.Class>filter(IteratorExtensions.<EObject>toIterable(input.getAllContents()), org.eclipse.uml2.uml.Class.class);
		for (final org.eclipse.uml2.uml.Class e : _filter) {
			if ((mQClass.isToBeGenerated(e) && mQClass.hasStateMachines(e))) {
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toHppFilePath("actionMgr")));
				fsa.generateFile(mFilesHelper.toHppFilePath("actionMgr"), this.generateHeader(Config.getInstance().getCurrentModule(), "ActionMgr"));

				Iterable<StateMachine> stateMachines = mQClass.getStateMachines(e);
				TreeSet<String> activityNames = new TreeSet<String>();
				TreeSet<String> actionNames = new TreeSet<String>();
				TreeSet<String> guardNames = new TreeSet<String>();
				for (final StateMachine sm : stateMachines) {
					TreeSet<String> allActivityNames = mQStateMachine.getAllActivityNames(sm);
					Iterables.<String>addAll(activityNames, allActivityNames);

					TreeSet<String> allActionNames = mQStateMachine.getAllActionNames(sm);
					Iterables.<String>addAll(actionNames, allActionNames);

					TreeSet<String> allGuardNames = mQStateMachine.getAllGuardNames(sm);
					Iterables.<String>addAll(guardNames, allGuardNames);
				}
				mFilesHelper.makeBackup(mFilesHelper.toAbsolutePath(mFilesHelper.toCppFilePath("actionMgr")));
				fsa.generateFile(mFilesHelper.toCppFilePath("actionMgr"), this.generateSource(Config.getInstance().getCurrentModule(), "ActionMgr", activityNames, actionNames, guardNames));
			}
		}
	}

	/**
	 * ActionMgr.hpp
	 */
	public CharSequence generateHeader(final String moduleName, final String className) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppActionMgr.stg");
			ST st = g.getInstanceOf("ActionMgrHeader");
			st.add("moduleName", moduleName);
			st.add("moduleNameUpperCase", moduleName.toUpperCase());
			st.add("moduleNameLowerCase", moduleName.toLowerCase());
			st.add("className", className);	
			st.add("classNameUpperCase", className.toUpperCase());	
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating header file for " + className + " class (" + throwable.getMessage() + ").");
		}
		return "";
	}

	/**
	 * ActionMgr.cpp
	 */
	public CharSequence generateSource(final String moduleName, final String className, final TreeSet<String> activityNames, final TreeSet<String> actionNames, final TreeSet<String> guardNames) {
		try {
			STGroup g = new STGroupFile("resources/tpl/EltRadCppActionMgr.stg");
			ST st = g.getInstanceOf("ActionMgrSource");
			st.add("moduleName", moduleName);
			st.add("moduleNameLowerCase", moduleName.toLowerCase());
			st.add("className", className);	
			st.add("classNameFilename", mFilesHelper.toFileName(className));
			st.add("actionsInclude", printActionsInclude(moduleName, actionNames, guardNames));
			st.add("activitiesInclude", printActivitiesInclude(moduleName, activityNames));
			st.add("createActions", printCreateActions(actionNames, guardNames));
			st.add("createActivities", printCreateActivities(activityNames));
			return st.render();
		} catch(Throwable throwable) {
			mLogger.error("Generating source file for " + className + " class (" + throwable.getMessage() + ").");
		}
		return "";
	}

	public CharSequence printActionsInclude(final String moduleName, final TreeSet<String> actionNames, final TreeSet<String> guardNames) {
		String str = "";
		TreeSet<String> mergeClassNames = mActions.getMergeClassNames(mActions.getClassNames(actionNames), mActions.getClassNames(guardNames));
		for (final String name : mergeClassNames) {
			str += "#include <" + moduleName + "/" + mFilesHelper.toFileName(name) + ".hpp>\n";
		}
		return str;
	}

	public CharSequence printActivitiesInclude(final String moduleName, final TreeSet<String> activityNames) {
		String str = "";
		for (final String name : activityNames) {
			str += "#include <" + moduleName + "/" + mFilesHelper.toFileName(name) + ".hpp>\n";
		}
		return str;
	}

	public CharSequence printCreateActions(final TreeSet<String> actionNames, final TreeSet<String> guardNames) {
		String str = "";
		TreeSet<String> mergeClassNames = mActions.getMergeClassNames(mActions.getClassNames(actionNames), mActions.getClassNames(guardNames));
		for (final String group : mergeClassNames) {
			str += "\n";
			str += group + "* my_" + group.toLowerCase() + " = new " + group + "(ios, sm, the_data);\n";
			str += "if (my_" + group.toLowerCase() + " == nullptr) {\n";
			str += "    LOG4CPLUS_ERROR(GetLogger(), \"Cannot create " + group + " object.\");\n";
			str += "    // @TODO throw exception\n";
			str += "    return;\n";
			str += "}\n";
			str += "AddActionGroup(my_" +  group.toLowerCase() + ");\n";
			TreeSet<String> methodNames = mActions.getMethodNames(actionNames, group);
			for(final String method : methodNames) {
				str += "\n";
				str += "the_action = new rad::ActionCallback(\"" + group + "." + method + "\",\n";
				str += "                                     std::bind(&" + group + "::" + method + ", my_" + group.toLowerCase() + ", _1));\n";
				str += "AddAction(the_action);\n";
			}
			TreeSet<String> methodNames2 = mActions.getMethodNames(guardNames, group);
			for(final String method2 : methodNames2) {
				str += "\n";
				str += "the_action = new rad::GuardCallback(\"" + group + "." + method2 + "\",\n";
				str += "                                    std::bind(&" + group + "::" + method2 + ", my_" + group.toLowerCase() + ", _1));\n";
				str += "AddAction(the_action);\n";
			}
		}
		return str;
	}

	public CharSequence printCreateActivities(final TreeSet<String> activityNames) {
		String str = "";
		if (activityNames.size() > 0) {
			str += "scxml4cpp::Activity* the_activity = nullptr;\n";
			for (final String name : activityNames) {
				str += "\n";
				str += "the_activity = new "  + name + "(\"" + name + "\", sm, the_data);\n";
				str += "AddActivity(the_activity);";
			}
		}
		return str;
	}

}
